package com.atwzj.pixivlib.jvm

import com.atwzj.pixivlib.Pixiv
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.async

fun main() = runBlocking<Unit> {
    val pixiv = Pixiv<JVM>()
    val job = GlobalScope.launch {
        println("Getting daily rank..")
        val rank = pixiv.getRank("daily")
        println("Got ${rank.getArtworkCount()} artworks!") // Got 50 artworks!
        val artworks = rank.getArtworks()

        println(artworks.size)
        for ((index, artwork) in artworks.withIndex()) {
            try {
                println("Getting top ${index + 1}...") // Getting top 1...

                val author = async { artwork.getAuthor() }
                val createDate = async { artwork.getCreateDate() }
                val pageCount = async { artwork.getPageCount() }
                val title = async { artwork.getTitle() }
                val description = async { artwork.getDescription() }

                println("author: ${author.await()}, title: ${title.await()}, createDate: ${createDate.await()}, pageCount: ${pageCount.await()}, description: ${description.await()}")
                artwork.getImages().forEach { image ->
                    println(image.urls.original) // https://.....
                    // Download it, just like DownloadQueue.add(url)
                }
            } catch (e: Exception) {
                println("Getting top ${index + 1} failed: ${e.message}")
                continue
            }
        }
    }
    job.join()
    println("End")
}