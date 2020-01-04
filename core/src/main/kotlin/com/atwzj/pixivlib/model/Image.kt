package com.atwzj.pixivlib.model

import com.atwzj.pixivlib.features.Artwork

data class Image(
    val artwork: Artwork,
    val page: Int = 0,
    val urls: Size
) {
    data class Size(
        val mini: String = "",
        val thumb: String = "",
        val small: String = "",
        val regular: String = "",
        val original: String = ""
    )
}