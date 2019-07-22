package com.gts.flickrflow.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch

import com.gts.flickrflow.core.Result
import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.domain.RetrievePhotosFromDbUseCase

import timber.log.Timber

class PhotoStreamViewModel(
    private val retrievePhotosFromDbUseCase: RetrievePhotosFromDbUseCase
) : ViewModel() {

    private val _photos = MutableLiveData<List<Photo>>()
    val photos: LiveData<List<Photo>>
        get() = _photos

    fun retrievePhotosFromDb() {
        viewModelScope.launch {
            val result = retrievePhotosFromDbUseCase.invoke()
            when (result) {
                is Result.Success -> {
                    _photos.postValue(result.data)
                }
                is Result.Error -> Timber.d("no photos")
            }
        }
    }
}