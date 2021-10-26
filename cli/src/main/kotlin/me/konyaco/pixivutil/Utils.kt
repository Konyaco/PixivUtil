package me.konyaco.pixivutil

import me.konyaco.pixivlib.features.RankMode
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    /**
     * Filename may be illegal. Optimize the filename.
     */
    private fun optimizeFilename(filename: String): String {
        var result = filename
        """<>:"/\|?*""".forEach {
            result = result.replace(it, '-')
        }
        return result
    }

    /**
     * Build a name of the download directory, in format of RankMode-Date, for example: Daily-19-12-03, Weekly-19-12-Week1
     */
    fun buildDownloadDirName(rankMode: RankMode): String {
        val date = Date()
        val ft = when (rankMode) {
            RankMode.DAILY -> {
                SimpleDateFormat("YY-MM-dd").format(date)
            }
            RankMode.WEEKLY -> {
                SimpleDateFormat("YY-MM-'Week'W").format(date)
            }
            RankMode.MONTHLY -> {
                SimpleDateFormat("YY-MM").format(date)
            }
            else -> error("Unknown rank")
        }
        return "${rankMode.modeName}-$ft"
    }

    fun buildFileName(rank: Int, id: String, title: String, author: String, page: Int): String {
        return optimizeFilename("${id}_p${page}") // RankN_Title_Author
    }

    fun buildArtName(id: String, page: Int): String {
        return "${id}_p${page}"
    }
}