package moymer.com.category

import android.content.Context
import moymer.com.data.CategoryRepository
import moymer.com.data.PLCallback
import moymer.com.db.Category
import moymer.com.utils.FileUtils
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
class PLCategoryVisionPresenter : PLCategoryVisionContract.Presenter {

    private var mCategoryVisionView: PLCategoryVisionContract.View? = null
    private var mCategoryVisionAdapterView: PLCategoryVisionContract.Adapter? = null

    private var mCategoryList = ArrayList<Category>(0)

    override fun takeView(view: PLCategoryVisionContract.View) {
        mCategoryVisionView = view
    }

    override fun dropView() {
        mCategoryVisionView = null
    }

    override fun takeAdapterView(adapterView: PLCategoryVisionContract.Adapter) {
        mCategoryVisionAdapterView = adapterView

    }

    override fun getCategories() {
        mCategoryList = CategoryRepository.instance.getCategoriesFromJson()
        mCategoryVisionAdapterView?.setCategoryList(mCategoryList)
    }

    override fun dropAdapterView() {
        mCategoryVisionAdapterView = null
    }

    override fun getCount(): Int {
        return mCategoryList.size + 1       //incluindo o header no count
    }

    override fun getLocale(): String {
        val locale = Locale.getDefault().language
        return if (locale != "") {
            locale
        } else {
            "en"
        }
    }

    override fun uploadToCloud(context: Context) {

        val fileUpload = File(FileUtils.getCloudDirectory(context))

        mCategoryVisionView?.showLoading(true)

        CategoryRepository.instance.uploadDirToCloud(fileUpload, object : PLCallback<String>(true) {
            override fun onSuccess(result: String) {

                mCategoryVisionView?.showLoading(false)
                mCategoryVisionView?.showToast(result)
            }

            override fun onFailure(errorDescription: String) {

                mCategoryVisionView?.showLoading(false)
                mCategoryVisionView?.showToast(errorDescription)
            }
        })
    }
}