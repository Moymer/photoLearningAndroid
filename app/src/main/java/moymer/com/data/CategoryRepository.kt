package moymer.com.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import moymer.com.PLApplication
import moymer.com.cloud.FileUploader
import moymer.com.cloud.SCloudStorageCallback
import moymer.com.db.Category
import moymer.com.utils.CloudUtils
import java.io.File
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

    fun getCategoryFromId(categoryId: String): Category {
        for (category in mCategories) {
            if (category.categoryId == categoryId)
                return category
        }
        return Category()
    }

    fun uploadDirToCloud(file: File, callback: SCallback<String>) {

        val key = CloudUtils.getPhotoKeyBase()
        FileUploader.instance.uploadDir(key, file, object: SCloudStorageCallback<String>(false) {
            override fun uploadedDir(success: Boolean) {
                takeIf { success }?.apply {
                    callback.onSuccessThread("Fotos enviadas com sucesso")
                } ?: callback.onFailureThread("Não foi possivel enviar as fotos")
            }

            override fun uploadedFile(result: String, bytes: Long) {
                takeIf { result.isNotEmpty() }?.apply {
                    callback.onSuccessThread("Fotos enviadas com sucesso")
                } ?: callback.onFailureThread("Não foi possivel enviar as fotos")
            }
        })
    }
}