package com.atwzj.pixivlib.features

interface Pixiv {
    suspend fun getRank(rankMode: RankMode): Rank
    suspend fun getArtwork(id: String): Artwork
}