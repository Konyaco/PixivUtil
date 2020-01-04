package com.atwzj.pixivlib.features

import com.atwzj.pixivlib.engine.Engine
import com.atwzj.pixivlib.engine.Pixiv
import org.jsoup.Jsoup

internal class RankImpl(
    private val pixiv: Pixiv,
    private val engine: Engine,
    private val rankMode: String
) : Rank {
    private var parsed = false

    private val artworks = emptyList<Artwork>().toMutableList()

    private suspend fun parse() {
        if (parsed) return
        parseInfo()
        parsed = true
    }

    private suspend fun reparse() {
        parsed = false
        parse()
    }

    /**
     * @throws InterruptedException
     */
    private suspend fun parseInfo() {
        val response: String = engine.httpGet("https://www.pixiv.net/ranking.php?mode=$rankMode")

        try {
            val body = response.substringAfter("<body>").substringBefore("</body>")
            Jsoup.parse(body)
                .getElementsByClass("ranking-item")
                .forEach {
                    val id = it.attr("data-id")
                    artworks.add(pixiv.getArtwork(id))
                }
        } catch (e: Exception) {
            e.printStackTrace()
            throw InterruptedException("Parsing rank failed: $e")
        }
    }

    override suspend fun getArtworkCount(): Int {
        parse()
        return artworks.size
    }

    override suspend fun getArtwork(rankIndex: Int): Artwork {
        parse()
        return artworks[rankIndex]
    }

    override suspend fun getArtworks(): List<Artwork> {
        parse()
        return artworks
    }

    override fun iterator(): Iterator<Artwork> {
        return artworks.iterator()
    }
}