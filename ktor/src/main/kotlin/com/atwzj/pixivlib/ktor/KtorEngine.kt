package com.atwzj.pixivlib.ktor

import com.atwzj.pixivlib.engine.Engine
import com.atwzj.pixivlib.engine.EngineFactory
import com.atwzj.pixivlib.exception.PixivException
import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.engine.apache.Apache
import io.ktor.client.engine.http
import io.ktor.client.request.get
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@KtorExperimentalAPI
object Ktor : EngineFactory {
    override fun create(): Engine {
        return KtorEngine()
    }
}

@KtorExperimentalAPI
class KtorEngine : Engine {
    /**
     * @throws [PixivException] on failure
     */
    override suspend fun httpGet(url: String): String {
        val httpClient = HttpClient(Apache) {
            engine {
                proxy = ProxyBuilder.http("http://127.0.0.1:1080")
            }
        }
        var response = ""
        try {
            // Try twice
            for (i in 1..3) {
                response = withContext(Dispatchers.IO) {
                    httpClient.get<String>(url)
                }
                break
            }
        } catch (e: Exception) {
//                    e.printStackTrace()
            throw PixivException("Failed to get response", e)
        }
        return response
    }
}