package moymer.com.learnedwords

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.frag_learned_words_vision.*
import moymer.com.learnedwords.recyclerview.SLearnedWordsVisionAdapter
import moymer.com.photolearning.R

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
class SLearnedWordsVisionFragment: Fragment(), SLearnedWordsVisionContract.View {
    var layoutManager: LinearLayoutManager? = null

    var mPresenter: SLearnedWordsVisionContract.Presenter = SLearnedWordsVisionPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_learned_words_vision, container, false)
        mPresenter.takeView(this)

        val bundle = arguments
        if (bundle != null && bundle.containsKey("categoryId")) {
            mPresenter.setCategory(bundle.getString("categoryId"))
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerViewLayout()
        tv_category_title.text = mPresenter.getCategoryTitle()
        setCount()

        iv_back.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        mPresenter.dropView()
        super.onDestroy()
    }

    private fun setRecyclerViewLayout() {
        rv_words.adapter = SLearnedWordsVisionAdapter(this.context, mPresenter)
        layoutManager = LinearLayoutManager(context)
        rv_words.layoutManager = layoutManager
    }

    private fun setCount() {
        val totalCount = mPresenter.getCount()
        val learnedCount = mPresenter.getLearnedWordsCount()
        val count = "$learnedCount/$totalCount"
        val ss1 = SpannableString(count)
        ss1.setSpan(RelativeSizeSpan(1.5f), 0, learnedCount.toString().length, 0)
        ss1.setSpan(ForegroundColorSpan(Color.BLACK), 0, learnedCount.toString().length, 0)
        tv_category_count.text = ss1
    }
}
