package moymer.com.category.recyclerview

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category_header.view.*
import moymer.com.category.SCategoryVisionContract
import moymer.com.db.Category
import moymer.com.learnedwords.SLearnedWordsVisionActivity
import moymer.com.photolearning.PLMainActivity
import moymer.com.photolearning.R
import java.util.*


class SCategoryVisionAdapter (private val context: Context?, val presenter: SCategoryVisionContract.Presenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), SCategoryVisionContract.Adapter {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    private val mPresenter = presenter

    private var mCategories: List<Category> = ArrayList(0)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mPresenter.takeAdapterView(this)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mPresenter.dropAdapterView()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)
            CategoryViewHolder(view)
        } else {
            val header = LayoutInflater.from(context).inflate(R.layout.item_category_header, parent, false)
            HeaderViewHolder(header)
        }
    }

    override fun setCategoryList(categories: ArrayList<Category>) {
        mCategories = categories
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mPresenter.getCount()
    }

    override fun getItemViewType(position: Int): Int {
        if (isPositionHeader(position)) {
            return TYPE_HEADER
        }
        return TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CategoryViewHolder) {
            val category = mCategories[position - 1]
            holder.let { holder ->

                val language = mPresenter.getLocale()

                val title = category.title?.get(language)
                if (title != null) {
                    holder.title.text = category.title?.get(language)
                } else {
                    holder.title.text = category.title?.get("en")
                }

                //it.count.text = category.labels?.size.toString()
                holder.container.setOnClickListener {
                    launchCameraActivity(category)
                }

                holder.countContainer.setOnClickListener {
                    launchLearnedVisionWordsActivity(category)
                }

                //tv_category_count.setText(formatQuantityString(user.getUserWords().size()))
            }
        } else if (holder is HeaderViewHolder) {
            //cast holder to VHHeader and set data for header.
        }


    }

    private fun launchCameraActivity(category: Category?) {
        val intent = Intent(context, SLearnedWordsVisionActivity::class.java)
        intent.putExtra("categoryId", category?.categoryId)
        context?.startActivity(intent)
    }

    private fun launchLearnedVisionWordsActivity(category: Category) {
//        val intent = Intent(context, SLearnedWordsVisionActivity::class.java)
//        intent.putExtra("category", category)
//        context?.startActivity(intent)
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: AppCompatTextView = itemView.tv_category_title
        val container: RelativeLayout = itemView.rl_word_container
        val countContainer: LinearLayout = itemView.ll_category_count
        val count: AppCompatTextView = itemView.tv_category_count
        val progressBar: ProgressBar = itemView.pb_vision_category
        val arrow: AppCompatImageView = itemView.iv_category_count_icon

    }

    class HeaderViewHolder(header: View) : RecyclerView.ViewHolder(header) {

        val title: AppCompatTextView = header.tv_select_category_message

    }
}

