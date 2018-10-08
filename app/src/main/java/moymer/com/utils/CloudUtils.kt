package moymer.com.utils

class CloudUtils {

    companion object {

        fun getPhotoKeyBase(filePath: String): String {
            return  StringUtils.unaccent("learning/$filePath/")
        }
    }
}