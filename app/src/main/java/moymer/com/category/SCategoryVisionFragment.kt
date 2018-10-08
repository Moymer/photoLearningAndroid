package com.moymer.spoken.usercases.main.vision.category

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.moymer.spoken.R
import com.moymer.spoken.usercases.main.vision.category.recyclerview.SCategoryVisionAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.frag_category_vision.*
import javax.inject.Inject

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
class SCategoryVisionFragment @Inject constructor() : DaggerFragment(), SCategoryVisionContract.View {
    var layoutManager: LinearLayoutManager? = null

    @Inject
    lateinit var mPresenter: SCategoryVisionContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_category_vision, container, false)
        mPresenter.takeView(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerViewLayout()

        if (mPresenter.getCategoryIdFromPreferences() == "") {
            iv_back_categories.visibility = View.GONE
        }

        iv_back_categories.setOnClickListener {
            activity?.onBackPressed()
        }
        tv_try_again.setOnClickListener {
            mPresenter.getCategories()
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getCategories()
    }

    override fun onDestroy() {
        mPresenter.dropView()
        super.onDestroy()
    }

    private fun setRecyclerViewLayout() {
        rv_category.adapter = SCategoryVisionAdapter(this.context, mPresenter)
        layoutManager = LinearLayoutManager(context)
        rv_category.layoutManager = layoutManager
    }

    override fun showCategories(show: Boolean) {
        if (show) {
            rv_category.visibility = View.VISIBLE
        } else {
            rv_category.visibility = View.GONE
        }
    }

    override fun showLoading(show: Boolean) {
        if (show) {
            iv_loading_categories.visibility = View.VISIBLE
        } else {
            iv_loading_categories.visibility = View.GONE
        }
    }

    override fun showNoInternet(show: Boolean) {
        if (show) {
            rl_no_internet_container.visibility = View.VISIBLE
        } else {
            rl_no_internet_container.visibility = View.GONE
        }
    }
}
