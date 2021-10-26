package me.konyaco.pixivlib

import io.ktor.client.*
import me.konyaco.pixivlib.features.Pixiv
import me.konyaco.pixivlib.features.PixivImpl

object PixivFactory {
    fun create(engine: HttpClient): Pixiv {
        return PixivImpl(engine)
    }
}