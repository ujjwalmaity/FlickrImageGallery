package dev.ujjwal.flickrimagegallery.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.ujjwal.flickrimagegallery.R
import dev.ujjwal.flickrimagegallery.model.network.FlickrPhoto
import dev.ujjwal.flickrimagegallery.util.getProgressDrawable
import dev.ujjwal.flickrimagegallery.util.loadImage
import kotlinx.android.synthetic.main.layout_grid_item.view.*

class HomeRecyclerViewAdapter(var listFlickrPhoto: ArrayList<FlickrPhoto>) :
    RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    fun updatePhoto(newPhoto: List<FlickrPhoto>) {
        listFlickrPhoto.clear()
        listFlickrPhoto.addAll(newPhoto)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_grid_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFlickrPhoto[position])
    }

    override fun getItemCount() = listFlickrPhoto.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val imageView = view.image

        private val progressDrawable = getProgressDrawable(view.context)

        fun bind(flickrPhoto: FlickrPhoto) {
            imageView.loadImage(flickrPhoto.url, progressDrawable)
        }
    }
}