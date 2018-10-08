package moymer.com.utils

class CloudUtils {

    companion object {

        fun getPhotoKeyBase(categoryId: String, label: String): String {
            val currentTimeMillis = System.currentTimeMillis()
            return  StringUtils.unaccent("learning/$categoryId/$label/$currentTimeMillis.jpg")
        }
    }
}