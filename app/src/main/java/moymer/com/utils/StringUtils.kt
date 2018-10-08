package moymer.com.utils

import java.text.Normalizer


/**
 * Created by gabriellins @ moymer
 * on 25/07/18.
 */
class StringUtils {

    companion object {

        fun unaccent(src: String): String {
            return Normalizer
                    .normalize(src, Normalizer.Form.NFD)
                    .replace("[^\\p{ASCII}]".toRegex(), "")
        }

        fun removeWhitespaces(src: String) : String {
            return src.replace("\\s".toRegex(), "")
        }

    }

}