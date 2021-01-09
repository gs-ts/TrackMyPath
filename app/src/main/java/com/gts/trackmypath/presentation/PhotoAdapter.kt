package com.gts.trackmypath.presentation

import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

import com.facebook.drawee.view.SimpleDraweeView

import com.gts.trackmypath.databinding.PhotoItemBinding
import com.gts.trackmypath.presentation.model.PhotoViewItem

class PhotoAdapter(private val photos: MutableList<PhotoViewItem>) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private var _binding: PhotoItemBinding? = null
    private val binding get() = _binding

    fun addPhoto(photo: PhotoViewItem) {
        // Add the photo at the beginning of the list
        photos.add(0, photo)
        notifyItemInserted(0)
    }

    fun populate(photoItems: List<PhotoViewItem>) {
        photos.addAll(photoItems)
        notifyDataSetChanged()
    }

    fun resetPhotoList() {
        photos.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PhotoViewHolder {
        _binding =
            PhotoItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photos[position]
        holder.photoImageView.setImageURI(
            buildUri(
                photo.farm,
                photo.server,
                photo.id,
                photo.secret
            )
        )
    }

//    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
//        super.onDetachedFromRecyclerView(recyclerView)
//        _binding = null // we actually need this part
//    }

    override fun getItemCount(): Int = photos.size

    class PhotoViewHolder(binding: PhotoItemBinding?) : RecyclerView.ViewHolder(binding!!.root) {
        var photoImageView: SimpleDraweeView = binding?.imageViewId as SimpleDraweeView
    }
}
