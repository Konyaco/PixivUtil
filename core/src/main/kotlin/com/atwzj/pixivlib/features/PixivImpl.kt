package com.atwzj.pixivlib.features

import com.atwzj.pixivlib.engine.*

internal class PixivImpl(private val engine: Engine) : Pixiv {
    override suspend fun getRank(rankMode: RankMode): Rank {
        return RankImpl(this, engine, rankMode)
    }

    override suspend fun getArtwork(id: String): Artwork {
        return ArtworkImpl(engine, id)
    }
}