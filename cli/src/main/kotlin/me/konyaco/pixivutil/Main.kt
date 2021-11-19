package me.konyaco.pixivutil

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.java.*
import kotlinx.cli.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.konyaco.pixivlib.PixivFactory
import me.konyaco.pixivlib.features.RankMode
import me.konyaco.pixivlib.model.Image

private lateinit var httpClient: HttpClient

val parser = ArgParser("pixivutil")
val opt_proxy by parser.option(ArgType.String, "proxy", "p", "HTTP proxy url.")
val download = Download()
val rank = Rank()

@ExperimentalCli
fun main(args: Array<String>): Unit = runBlocking {
    parser.subcommands(download, rank)
    parser.parse(args)
}

class Download : Subcommand("download", "Download an artwork") {
    val id by argument(ArgType.String, "id", "Artwork ID.")
    val outputDirectory by option(ArgType.String, "out_dir", "o", "Output directory.").default("download")

    override fun execute() {
        downloadSingleArtwork(id, outputDirectory)
    }
}

class Rank : Subcommand("rank", "Get and download rank") {
    val mode by argument(ArgType.Choice(listOf("daily", "weekly", "monthly"), { it }), "mode", "Rank mode")
    val thread by option(ArgType.Int, "thread", "t", "Download thread.").default(6)
    val outputDirectory by option(ArgType.String, "out_dir", "o", "Output directory.")

    override fun execute() = runBlocking {
        val rankMode = when (mode) {
            "daily" -> RankMode.DAILY
            "weekly" -> RankMode.WEEKLY
            "monthly" -> RankMode.MONTHLY
            else -> error("Unknown rank mode")
        }
        downloadRank(rankMode, thread)
    }
}

private fun getHttpClient() {
    httpClient = HttpClient(Java) {
        engine {
            opt_proxy?.let {
                proxy = ProxyBuilder.http(it)
            }
        }
    }
}

fun downloadSingleArtwork(id: String, outDir: String) = runBlocking {
    getHttpClient()
    val downloader = Downloader(httpClient, outDir)
    val artwork = PixivFactory.create(httpClient).getArtwork(id)

    artwork.getImages().forEach {
        val fileName = Utils.buildArtName(id, it.page)
        downloader.download(fileName, it.urls.original)
    }
}

fun downloadRank(rankMode: RankMode, thread: Int) = runBlocking {
    getHttpClient()
    val downloader = Downloader(httpClient, "download/" + Utils.buildDownloadDirName(rankMode))
    val rank = PixivFactory.create(httpClient).getRank(rankMode)

    println("Getting ${rankMode.modeName} rank..")
    println("Got ${rank.getArtworkCount()} artworks!") // Got 50 artworks!

    val artworks = rank.getArtworks()
    var artworksChannel = produce { artworks.forEach { send(it) } }

    val middleChannel = Channel<Pair<me.konyaco.pixivlib.features.Rank.RankArtwork, Image>>(Channel.UNLIMITED)

    launch {
        val failedArtworks = mutableListOf<me.konyaco.pixivlib.features.Rank.RankArtwork>()
        val mutex = Mutex()

        while (true) {
            coroutineScope {
                repeat(6) {
                    launch {
                        for (artwork in artworksChannel) {
                            try {
                                artwork.artwork.getImages().forEach { image ->
                                    middleChannel.send(artwork to image)
                                }
                            } catch (e: Exception) {
                                mutex.withLock { failedArtworks.add(artwork) }
                                println("Failed to get image of ${artwork.artwork.id}: ${e.message}")
                            }
                        }
                    }
                }
                joinAll()
            }

            if (failedArtworks.size == 0) {
                middleChannel.close()
            } else {
                print("There are ${failedArtworks.size} artworks failed to resolve, try again? (Y/n) ")
                if (readLine()?.equals("n", true) == true) {
                    middleChannel.close()
                    break
                } else {
                    val temp = mutex.withLock { failedArtworks.toList() }
                    artworksChannel = produce { temp.forEach { send(it) } }
                }
            }
        }
    }

    var imageChannel: ReceiveChannel<Pair<me.konyaco.pixivlib.features.Rank.RankArtwork, Image>> = middleChannel

    while (true) {
        val mutex = Mutex()
        val errorArtwork = mutableListOf<Pair<me.konyaco.pixivlib.features.Rank.RankArtwork, Image>>()

        coroutineScope {
            repeat(thread) {
                launch {
                    for ((item, image) in imageChannel) {
                        try {
                            val fileName = Utils.buildFileName(
                                item.indexInRank,
                                item.artwork.id,
                                item.artwork.getTitle(),
                                item.artwork.getAuthor(),
                                image.page
                            )
                            downloader.download(fileName, image.urls.original)
                        } catch (e: CancellationException) {
                            throw e
                        } catch (e: Exception) {
                            println("Download failed: ${e.message}")
                            mutex.withLock {
                                errorArtwork.add(item to image)
                            }
                        }
                    }
                }
            }
            joinAll()
        }

        if (errorArtwork.isEmpty()) {
            println("All tasks are completed!")
            return@runBlocking
        } else {
            print("There are ${errorArtwork.size} artworks haven't been downloaded, retry? (Y/n) ")
            if (readLine()?.equals("n", true) == true) break
            else {
                imageChannel = produce {
                    errorArtwork.forEach { send(it) }
                }
            }
        }
    }
}