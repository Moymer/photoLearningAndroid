package moymer.com.category

import moymer.com.data.CategoryRepository
import moymer.com.db.Category
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
class SCategoryVisionPresenter constructor(private val mCategoryRepository: CategoryRepository)
                                            : SCategoryVisionContract.Presenter {

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
        mCategoryVisionView?.showCategories(false)
        mCategoryVisionView?.showNoInternet(false)
        mCategoryVisionView?.showLoading(true)
        mCategoryRepository.getCategories(object: SCallback<ArrayList<Category>>(true) {
            override fun onSuccess(result: ArrayList<Category>) {

                mCategoryList = result
                val locale = getLocale()

                mCategoryList.sortWith(Comparator { o1: Category, o2: Category ->

                    //TODO Ver num jeito de substituir os !!
                    o1.title[locale]!!.compareTo(o2.title[locale]!!)
                })

                mCategoryVisionView?.showLoading(false)
                mCategoryVisionView?.showCategories(true)
                mCategoryVisionAdapterView?.setCategoryList(mCategoryList)

            }

            override fun onFailure(errorDescription: String) {
                mCategoryVisionView?.showLoading(false)
                mCategoryVisionView?.showNoInternet(true)
            }
        })
    }

    override fun dropAdapterView() {
        mCategoryVisionAdapterView = null
    }

    override fun getCount(): Int {
        return mCategoryList.size + 1       //incluindo o header no count
    }
}