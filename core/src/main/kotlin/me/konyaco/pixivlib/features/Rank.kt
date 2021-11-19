package me.konyaco.pixivlib.features

import me.konyaco.pixivlib.exception.PixivException

/**
 * @throws Throws [PixivException] on failure.
 */
interface Rank : Iterable<Rank.RankArtwork> {
    interface RankArtwork {
        val artwork: Artwork
        val indexInRank: Int
    }

    suspend fun getArtworkCount(): Int
    suspend fun getArtwork(rankIndex: Int): Artwork
    suspend fun getArtworks(): List<RankArtwork>
}