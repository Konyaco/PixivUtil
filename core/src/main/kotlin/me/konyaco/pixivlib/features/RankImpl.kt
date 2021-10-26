package me.konyaco.pixivlib.features

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.konyaco.pixivlib.exception.PixivException
import org.jsoup.Jsoup

internal class RankImpl(
    private val pixiv: Pixiv,
    private val engine: HttpClient,
    private val rankMode: RankMode
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
     * @throws [PixivException]
     */
    private suspend fun parseInfo()= withContext<Unit>(Dispatchers.IO) {
        val response: String = try {
            engine.get("https://www.pixiv.net/ranking.php?mode=${rankMode.modeName}")
        } catch (e: Exception) {
            throw PixivException("Failed to get rank website", e)
        }
        try {
            val body = response.substringAfter("<body>").substringBefore("</body>")
            Jsoup.parse(body)
                .getElementsByClass("ranking-item")
                .forEach {
                    val id = it.attr("data-id")
                    artworks.add(pixiv.getArtwork(id))
                }
        } catch (e: Exception) {
            throw PixivException("Failed to parse rank website", e)
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