package me.konyaco.pixivutil

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.java.*
import kotlinx.cli.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.konyaco.pixivlib.PixivFactory
import me.konyaco.pixivlib.exception.PixivException
import me.konyaco.pixivlib.features.Artwork
import me.konyaco.pixivlib.features.RankMode
import me.konyaco.pixivlib.model.Image
import java.io.File
import java.io.PrintStream

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
    println(artworks.size)

    val channels = produce<Triple<Int, Artwork, Image>> {
        artworks.forEachIndexed { index, artwork ->
            artwork.getImages().forEach { image ->
                send(Triple(index, artwork, image))
            }
        }
    }

    repeat(thread) {
        launch {
            while (!channels.isClosedForReceive) {
                val (index, artwork, image) = channels.receive()
                val fileName = Utils.buildFileName(
                    index,
                    artwork.getId(),
                    artwork.getTitle(),
                    artwork.getAuthor(),
                    image.page
                )
                downloader.download(fileName, image.urls.original)
            }
        }
    }
}