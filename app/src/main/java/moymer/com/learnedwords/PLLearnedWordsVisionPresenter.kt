package moymer.com.learnedwords

import moymer.com.data.CategoryRepository
import moymer.com.db.Category
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
class PLLearnedWordsVisionPresenter: PLLearnedWordsVisionContract.Presenter {

    private var mLearnedWordsVisionView: PLLearnedWordsVisionContract.View? = null
    private var mLearnedWordsVisionAdapterView: PLLearnedWordsVisionContract.Adapter? = null

    private var mLearnedWordsList = ArrayList<String>(0)
    private var mCategory: Category = Category()

    override fun takeView(view: PLLearnedWordsVisionContract.View) {
        mLearnedWordsVisionView = view
    }

    override fun dropView() {
        mLearnedWordsVisionView = null
    }

    override fun takeAdapterView(adapterView: PLLearnedWordsVisionContract.Adapter) {
        mLearnedWordsVisionAdapterView = adapterView
        setLearnedWords(mCategory.labels as ArrayList<String>)
    }

    override fun dropAdapterView() {
        mLearnedWordsVisionAdapterView = null
    }

    private fun setLearnedWords(wordsList: ArrayList<String>) {
        mLearnedWordsList = wordsList
        mLearnedWordsVisionAdapterView?.setLearnedWordsList(mLearnedWordsList)
    }

    override fun setCategory(categoryId: String) {
        mCategory = CategoryRepository.instance.getCategoryFromId(categoryId)
    }

    override fun getLearnedWordsCount(): Int? {
        return mLearnedWordsList.size
    }

    override fun getCount(): Int {
        return mCategory.labels.size
    }

    override fun getCategoryTitle(): String {
        val language = getLocale()
        mCategory.title[language]?.let {
            return it
        }
        return ""
    }

    override fun getCategoryId(): String {
        return mCategory.categoryId
    }

    private fun getLocale(): String {
        val locale = Locale.getDefault().language
        return if (locale != "") {
            locale
        } else {
            "en"
        }
    }
}