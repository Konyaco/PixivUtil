package com.atwzj.pixivlib

import com.atwzj.pixivlib.features.PixivImpl
import com.atwzj.pixivlib.engine.EngineFactory
import com.atwzj.pixivlib.engine.Pixiv

inline fun <reified T : EngineFactory> Pixiv(): Pixiv {
    return PixivFactory.create(T::class.objectInstance!!)
}

object PixivFactory {
    fun create(engineFactory: EngineFactory): Pixiv {
        return PixivImpl(engineFactory.create())
    }
}