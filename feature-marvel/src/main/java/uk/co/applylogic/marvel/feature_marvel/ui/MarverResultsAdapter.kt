package uk.co.applylogic.marvel.feature_marvel.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.collections.ArrayList
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import uk.co.applylogic.marvel.data.model.MarvelResult
import uk.co.applylogic.marvel.feature_marvel.R
import uk.co.applylogic.marvel.feature_marvel.databinding.ItemViewMarvelResultBinding

class MarvelResultsAdapter(
    private val fragment: MarvelMainFragment,
    private var dataSet: MutableList<MarvelResult>
) :
    PagedListAdapter<MarvelResult, MarvelResultsAdapter.MarvelResultViewHolder>(DiffUtilCallBack()) {

    class MarvelResultViewHolder(
        private val binding: ItemViewMarvelResultBinding,
        private val adapter: MarvelResultsAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MarvelResult?) {
            item?.let {
                binding.adapter = adapter
                binding.item = item
                binding.imageUrl = "${it.thumbnail?.path}.${it.thumbnail?.extension}"
                binding.executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun onItemUpdated(index: Int) {
        if (index < itemCount)
            notifyItemChanged(index)
    }

    fun notifyDataSetChanged(newData: ArrayList<MarvelResult>) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarvelResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemViewMarvelResultBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.item_view_marvel_result, parent, false)
        return MarvelResultViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: MarvelResultViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<MarvelResult>() {
    override fun areItemsTheSame(oldItem: MarvelResult, newItem: MarvelResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MarvelResult, newItem: MarvelResult): Boolean {
        return oldItem.title == newItem.title
                && oldItem.title == newItem.title
                && oldItem.description == newItem.description
                && oldItem.thumbnail?.path == newItem.thumbnail?.path
                && oldItem.thumbnail?.extension == newItem.thumbnail?.extension
    }
}
