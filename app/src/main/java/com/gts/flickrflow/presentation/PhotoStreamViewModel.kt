package com.gts.flickrflow.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData

import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.domain.RetrievePhotosFromDbUseCase

class PhotoStreamViewModel(
    private val retrievePhotosFromDbUseCase: RetrievePhotosFromDbUseCase
) : ViewModel() {

    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo>
        get() = _photo

    fun retrievePhotosFromDb() {
    }
}