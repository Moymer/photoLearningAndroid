package moymer.com.data

import android.util.Log
import com.google.gson.GsonBuilder
import java.lang.reflect.Modifier

class CategoryRepository {

    fun getCategoriesFromJson() {

        val json by lazy {
            val filename = "model.json"
            val jsonString = application.assets.open(filename).bufferedReader().use {
                it.readText()
            }

            val builder = GsonBuilder()
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
            builder.excludeFieldsWithoutExposeAnnotation()
            builder.setLenient()

            val create = builder.create()
            create.fromJson(jsonString, Array<Any>::class.java).asList()
        }
    }
}