package moymer.com.cloud

enum class FileContentType(extension: String, contentType: String) {

    png("png", "image/png"),
    gif("gif", "image/gif"),
    jpeg("jpeg", "image/jpeg"),
    videoTs("ts", "video/MP2T"),
    videoMpeg("mp4", "video/mp4"),
    m3u8("m3u8", "application/x-mpegURL"),
    html("html", "text/html"),
    m4a("m4a", "audio/m4a");

    private val mExtension = extension
    private val mContentType = contentType

    companion object {

        fun fromFileExtesion(fileExtension: String): FileContentType {
            when (fileExtension) {
                ".jpeg" -> return jpeg
                ".ts" -> return videoTs
                ".mp4" -> return videoMpeg
                ".m3u8" -> return m3u8
                ".m4a" -> return m4a
                else -> {
                }
            }
            return videoMpeg
        }
    }

    fun toExtension(): String {
        return this.mExtension
    }

    fun toContentType(): String {
        return this.mContentType
    }

    override fun toString(): String {
        return this.mContentType
    }
}