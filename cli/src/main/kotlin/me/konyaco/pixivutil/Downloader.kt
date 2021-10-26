package me.konyaco.pixivutil

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.nio.file.Paths

class Downloader(
    private val httpClient: HttpClient,
    private var defaultPath: String
) {
    suspend fun download(filename: String, url: String, path: String = defaultPath) {
        val postfix = url.substringAfterLast('.')
        Paths.get(path).toFile().mkdirs()
        val file = Paths.get(path, "$filename.$postfix").toFile()
        println("Downloading $filename to $path: $url")
        withContext(Dispatchers.IO) {
            val content = httpClient.get<ByteArray>(url) {
                header("Referer", "https://app-api.pixiv.net/")
            }
            file.writeBytes(content)
        }
        println("Download succeed!")
    }
}