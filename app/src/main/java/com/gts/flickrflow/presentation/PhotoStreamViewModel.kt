package com.gts.flickrflow.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

import com.gts.flickrflow.core.Result
import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.domain.DeletePhotosUseCase
import com.gts.flickrflow.domain.SearchByLocationUseCase

class PhotoStreamViewModel(
    private val searchByLocationUseCase: SearchByLocationUseCase,
    private val deletePhotosUseCase: DeletePhotosUseCase
) : ViewModel() {

    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo>
        get() = _photo

    fun getPhotoBasedOnLocation(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = searchByLocationUseCase.invoke(lat, lon)
            when (result) {
                is Result.Success -> _photo.postValue(result.data)
                is Result.Error -> throw result.exception
            }
        }
    }

    fun stopPhotoStream() {
        viewModelScope.launch {
            deletePhotosUseCase.invoke()
        }
    }
}