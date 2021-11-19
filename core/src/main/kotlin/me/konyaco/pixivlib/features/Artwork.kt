package me.konyaco.pixivlib.features

import me.konyaco.pixivlib.model.Image

/**
 * @throws Throws [PixivException] on failure.
 */
interface Artwork {
    val id: String
    suspend fun getAuthor(): String
    suspend fun getTitle(): String
    suspend fun getCreateDate(): String
    suspend fun getPageCount(): Int
    suspend fun getImages(): List<Image>
    suspend fun getDescription(): String
}