package moymer.com.learnedwords.recyclerview

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moymer.com.learnedwords.SLearnedWordsVisionContract
import java.util.*
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.item_learned_word.view.*
import moymer.com.photolearning.PLMainActivity
import moymer.com.photolearning.R
import java.text.MessageFormat
import java.util.concurrent.TimeUnit


class SLearnedWordsVisionAdapter (private val context: Context?, val presenter: SLearnedWordsVisionContract.Presenter) : RecyclerView.Adapter<SLearnedWordsVisionAdapter.LearnedWordViewHolder>(), SLearnedWordsVisionContract.Adapter {

    private val mPresenter = presenter

    private var mLearnedWords: List<String> = ArrayList(0)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        mPresenter.takeAdapterView(this)
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mPresenter.dropAdapterView()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LearnedWordViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_learned_word, parent, false)

        return LearnedWordViewHolder(view)

    }

    override fun setLearnedWordsList(learnedWords: ArrayList<String>) {
        mLearnedWords = learnedWords
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mPresenter.getCount()
    }

    override fun getLearnedWordsCount(): Int? {
        return mPresenter.getLearnedWordsCount()
    }

    override fun onBindViewHolder(holder: LearnedWordViewHolder, position: Int) {

        val learnedLabel = mLearnedWords[position]
        holder.position.text = (position + 1).toString()
        holder.title.text = learnedLabel

        holder.container.setOnClickListener {
            launchDefinitionActivity(learnedLabel)
        }
    }

    private fun launchDefinitionActivity(value: String) {
        val intent = Intent(context, PLMainActivity::class.java)
        val path = mPresenter.getCategoryId() + "/" + value
        intent.putExtra("path", path)
        context?.startActivity(intent)
    }

    class LearnedWordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: AppCompatTextView = itemView.tv_word
        val container: RelativeLayout = itemView.rl_word_container
        val position: AppCompatTextView = itemView.tv_position
        val date: AppCompatTextView = itemView.tv_learned_date

    }

}

