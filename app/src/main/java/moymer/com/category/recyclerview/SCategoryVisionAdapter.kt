package moymer.com.category.recyclerview

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.REVERSE
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.moymer.spoken.R
import com.moymer.spoken.db.entities.VisionCategory
import com.moymer.spoken.usercases.main.vision.category.SCategoryVisionContract
import com.moymer.spoken.usercases.vision.learnedwords.SLearnedWordsVisionActivity
import kotlinx.android.synthetic.main.item_category.view.*
import kotlinx.android.synthetic.main.item_category_header.view.*
import java.util.*


class SCategoryVisionAdapter (private val context: Context?, val presenter: SCategoryVisionContract.Presenter) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), SCategoryVisionContract.Adapter {

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    private val mPresenter = presenter

    private var mCategories: List<VisionCategory> = ArrayList(0)

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

    override fun setCategoryList(categories: ArrayList<VisionCategory>) {
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
                    mPresenter.setCategoryIdInPreferences(category.categoryId)
                    launchCameraActivity(category)
                }

                holder.countContainer.setOnClickListener {
                    holder.arrow.clearAnimation()
                    category.opened = true
                    mPresenter.setCategoryOpened(category)
                    launchLearnedVisionWordsActivity(category)
                }

            category.labels?.let { labels ->
                    category.learnedLabels?.let { learnedLabels ->

                        val learnedCount: Int = if (category.learnedLabels != null) {
                            learnedLabels.size
                        } else {
                            0
                        }

                        holder.count.text = getLearnedCount(learnedCount, category)

                        if (learnedCount == 0) {
                            holder.arrow.visibility = View.INVISIBLE
                            holder.progressBar.progress = 10                                    // Setando um valor qualquer antes do valor real, pois progress=0 não atualiza a view (parece ser um bug no progressbar de android)
                            holder.progressBar.progress = 0

                        } else {
                            val progress = learnedCount.toDouble()/labels.size.toDouble() * 100
                            holder.arrow.visibility = View.VISIBLE
                            holder.progressBar.progress = 10
                            holder.progressBar.progress = Math.round(progress).toInt()

                            if (!category.opened) {
                                scaleView(holder.arrow, 1f, 1.5f)
                            } else {
                                holder.arrow.clearAnimation()
                            }
                        }
                    }
                }

                //tv_category_count.setText(formatQuantityString(user.getUserWords().size()))
            }
        } else if (holder is HeaderViewHolder) {
            //cast holder to VHHeader and set data for header.
        }


    }

    private fun getLearnedCount(learnedCount: Int, category: VisionCategory): SpannableString {

        val totalCount = Integer.toString(Objects.requireNonNull<List<String>>(category.labels).size)
        val count = learnedCount.toString() + "/" + totalCount
        val ss1 = SpannableString(count)
        ss1.setSpan(RelativeSizeSpan(1.3f), 0, Integer.toString(learnedCount).length, 0)
        ss1.setSpan(ForegroundColorSpan(Color.BLACK), 0, Integer.toString(learnedCount).length, 0)
        return ss1
    }

    private fun scaleView(v: View, startScale: Float, endScale: Float) {
        val anim = ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 500
        anim.repeatCount = Animation.INFINITE
        anim.repeatMode = REVERSE

        v.startAnimation(anim)
    }

    private fun launchCameraActivity(category: VisionCategory?) {
        val i = Intent()
        i.putExtra("category", category)
        (context as Activity).setResult(RESULT_OK, i)
        context.finish()
    }

    private fun launchLearnedVisionWordsActivity(category: VisionCategory) {
        val intent = Intent(context, SLearnedWordsVisionActivity::class.java)
        intent.putExtra("category", category)
        context?.startActivity(intent)
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

