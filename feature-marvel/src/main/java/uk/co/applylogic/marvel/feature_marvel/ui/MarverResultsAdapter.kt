package uk.co.applylogic.marvel.feature_marvel.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import uk.co.applylogic.marvel.data.model.MarvelResult
import uk.co.applylogic.marvel.feature_marvel.databinding.ItemViewMarvelResultBinding
import javax.inject.Inject

class MarvelResultsAdapter @Inject constructor() :
    PagingDataAdapter<MarvelResult, MarvelResultsAdapter.MarvelResultViewHolder>(CharacterComparator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MarvelResultViewHolder(
            ItemViewMarvelResultBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: MarvelResultViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class MarvelResultViewHolder(private val binding: ItemViewMarvelResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: MarvelResult?) = with(binding) {
            result?.let {
                item = it
                imageUrl = it.thumbnail?.path.toString() + "." + it.thumbnail?.extension
            }
        }
    }

    object CharacterComparator : DiffUtil.ItemCallback<MarvelResult>() {
        override fun areItemsTheSame(oldItem: MarvelResult, newItem: MarvelResult) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MarvelResult, newItem: MarvelResult) =
            oldItem.title == newItem.title
                    && oldItem.title == newItem.title
                    && oldItem.description == newItem.description
                    && oldItem.thumbnail?.path == newItem.thumbnail?.path
                    && oldItem.thumbnail?.extension == newItem.thumbnail?.extension
    }
}
