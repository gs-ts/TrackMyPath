package com.gts.flickrflow.presentation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

import org.koin.androidx.viewmodel.ext.android.viewModel

import com.gts.flickrflow.R
import kotlinx.android.synthetic.main.fragment_photo_stream.*

class PhotoStreamFragment : Fragment() {

    private val viewModel: PhotoStreamViewModel by viewModel()
    private lateinit var recyclerView: RecyclerView
    private lateinit var photoAdapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.startPhotoStreamBasedOnLocation()

        recyclerView = photo_recycler_view.apply {
            adapter = photoAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_stream, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.photo.observe(viewLifecycleOwner, Observer {
//            my_image_view.setImageURI("https://farm${it.farm}.staticflickr.com/${it.server}/${it.id}_${it.secret}.jpg")
            Toast.makeText(context, it.id, Toast.LENGTH_SHORT).show()
        })
    }

    companion object {
        fun newInstance() = PhotoStreamFragment()
    }
}
