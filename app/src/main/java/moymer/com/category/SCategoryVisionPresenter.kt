package moymer.com.category

import moymer.com.data.CategoryRepository
import moymer.com.db.Category
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
class SCategoryVisionPresenter: SCategoryVisionContract.Presenter {

    private var mCategoryVisionView: SCategoryVisionContract.View? = null
    private var mCategoryVisionAdapterView: SCategoryVisionContract.Adapter? = null

    private var mCategoryList = ArrayList<Category>(0)

    override fun takeView(view: SCategoryVisionContract.View) {
        mCategoryVisionView = view
    }

    override fun dropView() {
        mCategoryVisionView = null
    }

    override fun takeAdapterView(adapterView: SCategoryVisionContract.Adapter) {
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
}