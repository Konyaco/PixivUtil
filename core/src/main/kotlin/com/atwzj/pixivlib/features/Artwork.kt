package com.atwzj.pixivlib.features

import com.atwzj.pixivlib.model.Image

/**
 * @throws Throws [PixivException] on failure.
 */
interface Artwork {
    suspend fun getAuthor(): String
    suspend fun getTitle(): String
    suspend fun getCreateDate(): String
    suspend fun getPageCount(): Int
    suspend fun getImages(): List<Image>
    suspend fun getId(): String
    suspend fun getDescription(): String
}