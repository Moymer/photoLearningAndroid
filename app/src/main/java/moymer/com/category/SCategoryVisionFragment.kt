package moymer.com.category

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import moymer.com.category.recyclerview.SCategoryVisionAdapter
import kotlinx.android.synthetic.main.frag_category_vision.*
import moymer.com.photolearning.R

/**
 * Created by gabriellins @ moymer
 * on 31/07/18.
 */
class SCategoryVisionFragment: Fragment(), SCategoryVisionContract.View {

    var layoutManager: LinearLayoutManager? = null

    private var mPresenter: SCategoryVisionContract.Presenter = SCategoryVisionPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frag_category_vision, container, false)
        mPresenter.takeView(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerViewLayout()

        tv_upload.setOnClickListener {
            it.context?.let {ctx ->
                mPresenter.uploadToCloud(ctx)
            }
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

    override fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
