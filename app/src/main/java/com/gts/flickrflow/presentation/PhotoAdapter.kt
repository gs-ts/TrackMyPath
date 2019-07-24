package com.gts.flickrflow.presentation

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

import com.facebook.drawee.view.SimpleDraweeView

import com.gts.flickrflow.R
import com.gts.flickrflow.domain.model.Photo

class PhotoAdapter(private val photos: MutableList<Photo>) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    fun addPhoto(photo: Photo) {
        // Add the photo at the beggining of the list
        photos.add(0, photo)
        notifyItemInserted(0)
    }

    fun populate(photosFromDb: List<Photo>) {
        photos.addAll(photosFromDb)
        notifyDataSetChanged()
    }

    fun resetPhotoList() {
        photos.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PhotoViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.photo_item, viewGroup, false)
        return PhotoViewHolder(v)
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

    override fun getItemCount(): Int = photos.size

    class PhotoViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var photoImageView: SimpleDraweeView = v.findViewById<View>(R.id.imageViewId) as SimpleDraweeView
    }
}