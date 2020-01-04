package com.atwzj.pixivlib.features

interface Rank : Iterable<Artwork> {
    /**
     * @throws [InterruptedException]
     */
    suspend fun getArtworkCount(): Int

    /**
     * @throws [InterruptedException]
     */
    suspend fun getArtwork(rankIndex: Int): Artwork

    /**
     * @throws [InterruptedException]
     */
    suspend fun getArtworks(): List<Artwork>
}