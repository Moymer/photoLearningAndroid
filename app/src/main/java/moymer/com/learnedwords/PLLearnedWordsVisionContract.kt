package moymer.com.learnedwords

import com.moymer.spoken.di.qualifiers.utils.BasePresenter
import com.moymer.spoken.di.qualifiers.utils.BaseView
import moymer.com.db.Category
import java.util.ArrayList

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
interface PLLearnedWordsVisionContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter: BasePresenter<View> {
        fun takeAdapterView(adapterView: Adapter)
        fun dropAdapterView()
        fun getCount(): Int
        fun getLearnedWordsCount(): Int?
        fun setCategory(categoryId: String)
        fun getCategoryTitle(): String
        fun getCategoryId(): String
    }


    interface Adapter : BaseView<Presenter> {
        fun setLearnedWordsList(categories: ArrayList<String>)
        fun getLearnedWordsCount(): Int?
    }

}