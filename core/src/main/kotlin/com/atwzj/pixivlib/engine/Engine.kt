package com.atwzj.pixivlib.engine

interface Engine {
    /**
     * @throws [InterruptedException] when failed.
     */
    suspend fun httpGet(url: String): String
}