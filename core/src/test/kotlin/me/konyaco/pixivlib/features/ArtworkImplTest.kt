package me.konyaco.pixivlib.features

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.java.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

internal class ArtworkImplTest {
    @Test
    fun test() {
        runBlocking {
            val art = ArtworkImpl(HttpClient(Java) {
                engine {
                    proxy = ProxyBuilder.http("http://localhost:10809")
                }
            }, "93680405")
            println(art.getTitle())
            println(art.getAuthor())
            println(art.getDescription())
            art.getImages().forEachIndexed { index, image ->
                println("p${image.page} ${image.urls}")
            }
        }
    }
}