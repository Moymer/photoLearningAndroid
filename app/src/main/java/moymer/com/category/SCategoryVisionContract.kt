package moymer.com.category

import android.content.Context
import com.moymer.spoken.di.qualifiers.utils.BasePresenter
import com.moymer.spoken.di.qualifiers.utils.BaseView
import moymer.com.db.Category
import java.util.ArrayList

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
interface SCategoryVisionContract {

    interface View : BaseView<Presenter> {
        fun getContext(): Context?
        fun showLoading(show: Boolean)
        fun showNoInternet(show: Boolean)
        fun showCategories(show: Boolean)
    }

    interface Presenter: BasePresenter<View> {
        fun takeAdapterView(adapterView: Adapter)
        fun dropAdapterView()
        fun getCount(): Int
        fun getLocale(): String?
        fun getCategories()
        fun setCategoryOpened(category: Category)
        fun setCategoryIdInPreferences(categoryId: String)
        fun getCategoryIdFromPreferences(): String
    }


    interface Adapter : BaseView<Presenter> {
        fun setCategoryList(categories: ArrayList<Category>)
    }

}