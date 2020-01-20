package com.atwzj.pixivlib.jvm

import com.atwzj.pixivlib.engine.Engine
import com.atwzj.pixivlib.engine.EngineFactory
import com.atwzj.pixivlib.exception.PixivException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object JVM : EngineFactory {
    override fun create(): Engine {
        return JVMEngine()
    }
}

class JVMEngine : Engine {
    /**
     * @throws [PixivException] on failure
     */
    override suspend fun httpGet(url: String): String {
        try {
            val content = withContext(Dispatchers.IO) {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()
                connection.inputStream.readAllBytes().toString(Charsets.UTF_8)
            }
            return content
        } catch (e: Exception) {
            throw PixivException("Failed to get response", e)
        }
    }
}