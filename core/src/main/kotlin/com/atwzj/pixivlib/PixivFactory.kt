package com.atwzj.pixivlib

import com.atwzj.pixivlib.engine.EngineFactory
import com.atwzj.pixivlib.features.Pixiv
import com.atwzj.pixivlib.features.PixivImpl

inline fun <reified T : EngineFactory> Pixiv(): Pixiv {
    return PixivFactory.create(T::class.objectInstance!!)
}

object PixivFactory {
    fun create(engineFactory: EngineFactory): Pixiv {
        return PixivImpl(engineFactory.create())
    }
}