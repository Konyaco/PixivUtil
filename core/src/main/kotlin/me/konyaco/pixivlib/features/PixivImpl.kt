package me.konyaco.pixivlib.features

import io.ktor.client.*

internal class PixivImpl(private val engine: HttpClient) : Pixiv {
    override suspend fun getRank(rankMode: RankMode): Rank {
        return RankImpl(this, engine, rankMode)
    }

    override suspend fun getArtwork(id: String): Artwork {
        return ArtworkImpl(engine, id)
    }
}