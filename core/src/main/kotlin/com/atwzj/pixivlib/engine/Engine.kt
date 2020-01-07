package com.atwzj.pixivlib.engine

import com.atwzj.pixivlib.exception.PixivException

interface Engine {
    /**
     * @throws Throws [PixivException] on failure
     */
    @Throws(PixivException::class)
    suspend fun httpGet(url: String): String
}