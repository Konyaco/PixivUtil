package com.atwzj.pixivlib.features

import com.alibaba.fastjson.JSONObject
import com.atwzj.pixivlib.engine.Engine
import com.atwzj.pixivlib.exception.PixivException
import com.atwzj.pixivlib.model.Image
import java.lang.Exception

internal class ArtworkImpl(private val engine: Engine, private val id: String) :
    Artwork {
    private var parsed = false

    private var author: String = ""
    private var title: String = ""
    private var createDate: String = ""
    private var pageCount: Int = -1
    private var description: String = ""
    private val images = emptyList<Image>().toMutableList()

    private suspend fun parse() {
        if (parsed) return
        images.clear()
        parseInfo()
        parsed = true
    }

    private suspend fun reparse() {
        parsed = false
        parse()
    }

    private suspend fun parseInfo() {
        val response = try {
            engine.httpGet("https://www.pixiv.net/artworks/$id")
        } catch (e: Exception) {
            throw PixivException("Failed to get pixiv website", e)
        }

        val jsonSrc = response.substringAfter("""<meta name="preload-data" id="meta-preload-data" content='""")
            .substringBefore("""'>""")

        val jsonObject = JSONObject.parseObject(jsonSrc)
        val illustObject = jsonObject.getJSONObject("illust")
        val realObject = illustObject.getJSONObject(id)

        this.author = realObject.getString("userName")
        this.pageCount = realObject.getIntValue("pageCount")
        this.title = realObject.getString("title")
        this.createDate = realObject.getString("createDate")
        this.description = realObject.getString("description")

        // 解析 image URL
        val urls = realObject.getJSONObject("urls")
        val miniUrl = urls.getString("mini")
        val thumbUrl = urls.getString("thumb")
        val smallUrl = urls.getString("small")
        val regularUrl = urls.getString("regular")
        val originalUrl =
            urls.getString("original") //https://i.pximg.net/img-original/img/2019/12/26/00/00/15/78491726_p0.png

        // 获取到的 URL 只有 p0，需要手动处理成多 p
        // 具体流程：获取 p0 前的 url，和 p0 后的 url（即扩展名），替换 p0 为 p1 p2
        fun String.pre() = this.substringBeforeLast("p0")

        fun String.post() = this.substringAfterLast("p0")

        val originalPre = originalUrl.pre()
        val originalPost = originalUrl.post()

        val miniPre = miniUrl.pre()
        val miniPost = miniUrl.post()

        val thumbPre = thumbUrl.pre()
        val thumbPost = thumbUrl.post()

        val smallPre = smallUrl.pre()
        val smallPost = smallUrl.post()

        val regularPre = regularUrl.pre()
        val regularPost = regularUrl.post()

        this.images.clear()
        for (i in 0 until pageCount) {
            val sizes = Image.Size(
                mini = "${miniPre}p$i${miniPost}",
                thumb = "${thumbPre}p$i${thumbPost}",
                small = "${smallPre}p$i${smallPost}",
                regular = "${regularPre}p$i${regularPost}",
                original = "${originalPre}p$i${originalPost}"
            )
            this.images.add(Image(this, i, sizes))
        }
    }

    override suspend fun getId(): String {
        return id
    }

    override suspend fun getAuthor(): String {
        parse()
        return author
    }

    override suspend fun getTitle(): String {
        parse()
        return title
    }

    override suspend fun getCreateDate(): String {
        parse()
        return createDate
    }

    override suspend fun getDescription(): String {
        parse()
        return description
    }

    override suspend fun getPageCount(): Int {
        parse()
        return pageCount
    }

    override suspend fun getImages(): List<Image> {
        parse()
        return images
    }

    override fun iterator(): Iterator<Image> {
        return images.iterator()
    }
}