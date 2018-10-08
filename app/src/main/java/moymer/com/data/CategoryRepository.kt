package moymer.com.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import moymer.com.PLApplication
import moymer.com.db.Category
import java.lang.reflect.Modifier

class CategoryRepository private constructor(){

    private object Holder { val INSTANCE = CategoryRepository() }

    companion object {
        val instance: CategoryRepository by lazy { Holder.INSTANCE }
    }

    private val mCategories by lazy {
        val filename = "model.json"
        val jsonString = PLApplication.applicationContext().assets.open(filename).bufferedReader().use {
            it.readText()
        }

        val builder = GsonBuilder()
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
        builder.excludeFieldsWithoutExposeAnnotation()
        builder.setLenient()

        val create = builder.create()
        create.fromJson<java.util.ArrayList<Category>>(jsonString, object : TypeToken<java.util.ArrayList<Category>>() {}.type)
//        create.fromJson(jsonString, Array<Category>::class.java).asList()
    }

    fun getCategoriesFromJson(): ArrayList<Category> {
        return mCategories as ArrayList<Category>
    }


}