package com.moymer.spoken.usercases.main.vision.category

import com.moymer.spoken.SPreferences
import com.moymer.spoken.data.SCallback
import com.moymer.spoken.data.user.UserRepository
import com.moymer.spoken.data.vision.VisionRepository
import com.moymer.spoken.db.entities.LanguageInfo
import com.moymer.spoken.db.entities.VisionCategory
import com.moymer.spoken.utils.InternetUtils
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
class SCategoryVisionPresenter @Inject constructor(private val mVisionRepository: VisionRepository,
                                                   private val mUserRepository: UserRepository,
                                                   private val mPreferences: SPreferences,
                                           private val mInternetUtils: InternetUtils)
                                            : SCategoryVisionContract.Presenter {

    private var mCategoryVisionView: SCategoryVisionContract.View? = null
    private var mCategoryVisionAdapterView: SCategoryVisionContract.Adapter? = null

    private var mCategoryList = ArrayList<VisionCategory>(0)

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
        mVisionRepository.getCategories(object: SCallback<ArrayList<VisionCategory>>(true) {
            override fun onSuccess(result: ArrayList<VisionCategory>) {

                mCategoryList = result
                val locale = getLocale()

                mCategoryList.sortWith(Comparator { o1: VisionCategory, o2: VisionCategory ->

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

    override fun getLocale(): String {
        val locale = Locale.getDefault().language
        return if (locale != "") {
            locale
        } else {
            "en"
        }
    }

    override fun setCategoryOpened(category: VisionCategory) {
        mVisionRepository.insertCategory(category)
    }

    override fun setCategoryIdInPreferences(categoryId: String) {
        mUserRepository.getLanguageInfoAsync(object : SCallback<LanguageInfo>(false) {
            override fun onSuccess(result: LanguageInfo) {
                mPreferences.storeSelectedVisionCategoryId(categoryId, result.language)
            }

            override fun onFailure(errorDescription: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun getCategoryIdFromPreferences(): String {
        var categoryId = ""
        mUserRepository.getLanguageInfoAsync(object : SCallback<LanguageInfo>(true) {
            override fun onSuccess(result: LanguageInfo) {
                categoryId = mPreferences.retrievesSelectedVisionCategoryId(result.language)
            }

            override fun onFailure(errorDescription: String) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        return categoryId
    }
}