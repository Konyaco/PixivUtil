package com.atwzj.pixivlib.ktor

import com.atwzj.pixivlib.engine.Engine
import com.atwzj.pixivlib.engine.EngineFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.ProxyBuilder
import io.ktor.client.engine.apache.Apache
import io.ktor.client.engine.http
import io.ktor.client.request.get
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
object Ktor : EngineFactory {
    override fun create(): Engine {
        return KtorEngine()
    }
}

@KtorExperimentalAPI
class KtorEngine : Engine {
    private val httpClient = HttpClient(Apache) {
        engine {
            proxy = ProxyBuilder.http("http://127.0.0.1:1080")
        }
    }

    override suspend fun httpGet(url: String): String {
        var response = ""
        try {
            // Try twice
            for (i in 1..3) {
                response = httpClient.get(url)
                break
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw InterruptedException("Failed to get response: ${e.message}")
        }
        return response
    }
}