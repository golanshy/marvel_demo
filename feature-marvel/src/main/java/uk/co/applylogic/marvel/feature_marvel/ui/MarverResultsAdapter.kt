package uk.co.applylogic.marvel.feature_marvel.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import uk.co.applylogic.marvel.data.model.MarvelResult
import uk.co.applylogic.marvel.feature_marvel.R
import uk.co.applylogic.marvel.core_android.listeners.OnBottomReachedListener
import uk.co.applylogic.marvel.feature_marvel.databinding.ItemViewMarvelResultBinding


class MarvelResultsAdapter(
    val viewModel: MarvelMainViewModel,
    fragment: MarvelMainFragment,
    private var dataSet: MutableList<MarvelResult>
) :
    PagedListAdapter<MarvelResult, MarvelResultsAdapter.MarvelResultViewHolder>(DiffUtilCallBack()) {

    private lateinit var onBottomReachedListener: OnBottomReachedListener

    init {
        viewModel.searchResults.observe(fragment.viewLifecycleOwner, Observer { newData ->
            newData?.let {
                dataSet.addAll(newData)
                notifyDataSetChanged()
            }
        })
    }

    fun resetData() {
        dataSet.clear()
        notifyDataSetChanged()
    }

    fun setOnBottomReachedListener(onBottomReachedListener: OnBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener
    }

    class MarvelResultViewHolder(
        private val binding: ItemViewMarvelResultBinding,
        private val adapter: MarvelResultsAdapter
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MarvelResult?) {
            item?.let {
                binding.viewModel = this.adapter.viewModel
                binding.item = item
                binding.imageUrl = "${it.thumbnail?.path}.${it.thumbnail?.extension}"
                binding.executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
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
        if (position == dataSet.size - 1) {
            onBottomReachedListener.onBottomReached(position)
        }
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
