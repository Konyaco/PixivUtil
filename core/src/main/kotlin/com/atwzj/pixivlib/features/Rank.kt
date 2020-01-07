package com.atwzj.pixivlib.features

import com.atwzj.pixivlib.exception.PixivException

/**
 * @throws Throws [PixivException] on failure.
 */
interface Rank : Iterable<Artwork> {
    suspend fun getArtworkCount(): Int
    suspend fun getArtwork(rankIndex: Int): Artwork
    suspend fun getArtworks(): List<Artwork>
}