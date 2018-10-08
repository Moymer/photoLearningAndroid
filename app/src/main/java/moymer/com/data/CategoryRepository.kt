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

    fun getCategories(callback: SCallback<ArrayList<VisionCategory>>, forceDownload: Boolean = false) {

        mUserRepository.getLanguageInfoAsync(object : SCallback<LanguageInfo>(false) {
            override fun onSuccess(result: LanguageInfo) {

                result.let {

                    val categories = mVisionLocalDataSource.getCategories(result.language)

                    categories?.let {

                        val currentTime = System.currentTimeMillis()
                        val shouldUpdatePeriod = 604800000 //1 semana

                        if (it.isEmpty() || (currentTime - categories[0].updateDate > shouldUpdatePeriod) || forceDownload) {

                            mVisionRemoteDataSource.getModelsJson(result.language, object : SCallback<ArrayList<VisionCategory>>(false) {
                                override fun onSuccess(serverCategoriesList: ArrayList<VisionCategory>) {
                                    if (serverCategoriesList.isNotEmpty()) {

                                        val localCategoriesMap = HashMap<String, VisionCategory>()
                                        for (localCategory in it) {
                                            localCategoriesMap[localCategory.categoryId] = localCategory
                                        }

                                        for (serverCategory in serverCategoriesList) {
                                            serverCategory.updateDate = currentTime
                                            serverCategory.language = result.language

                                            localCategoriesMap[serverCategory.categoryId]?.let { category ->
                                                serverCategory.learnedLabels = category.learnedLabels
                                                serverCategory.opened = category.opened
                                            }
                                        }

                                        mVisionLocalDataSource.insertCategories(serverCategoriesList)
                                        callback.onSuccessThread(serverCategoriesList)
                                    }
                                }

                                override fun onFailure(errorDescription: String) {
                                    if (it.isNotEmpty()) {
                                        callback.onSuccessThread(it)
                                    } else {
                                        callback.onFailureThread(errorDescription)
                                    }
                                }
                            })
                        } else {
                            callback.onSuccessThread(it)
                        }
                    }

                }
            }

            override fun onFailure(errorDescription: String) {
                Log.d("getLoggedUser failure ", errorDescription)
            }
        })
    }


}