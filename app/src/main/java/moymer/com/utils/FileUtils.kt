package moymer.com.utils

import android.content.Context
import java.io.File

class FileUtils {

    companion object {

        const val base = "/dialetto/learning/"

        fun getDirectoryWithPath(path: String?, context: Context): String {
            path?.let {
                val dir = context.filesDir
                val dirName = if (dir == null) "" else dir.absolutePath + it

                val temp = File(dirName)
                if (!temp.exists()) {
                    temp.mkdirs()
                }

                return dirName
            }

            return ""
        }

        fun getCloudDirectory(context: Context): String {
            return getDirectoryWithPath(base,context)
        }

        fun saveImageDirectory(path: String?, context: Context): String {
            return getDirectoryWithPath("$base$path", context)
        }
    }
}