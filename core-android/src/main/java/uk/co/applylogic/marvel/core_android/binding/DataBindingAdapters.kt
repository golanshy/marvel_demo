package uk.co.applylogic.marvel.core_android.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import uk.co.applylogic.marvel.core_android.R

@BindingAdapter("thumbnailImage")
fun loadImage(view: ImageView, imageUrl: String?) {
    Glide.with(view.context)
        .load(imageUrl?.replace("http://", "https://"))
        .placeholder(R.drawable.placeholder)
        .apply(RequestOptions().centerCrop())
        .into(view)
}