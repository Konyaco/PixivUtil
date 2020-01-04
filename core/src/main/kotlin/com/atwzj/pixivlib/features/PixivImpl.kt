package com.atwzj.pixivlib.features

import com.atwzj.pixivlib.engine.*

internal class PixivImpl(private val engine: Engine) : com.atwzj.pixivlib.engine.Pixiv {
    override suspend fun getRank(rankMode: String): Rank {
        return RankImpl(this, engine, rankMode)
    }

    override suspend fun getArtwork(id: String): Artwork {
        return ArtworkImpl(engine, id)
    }
}