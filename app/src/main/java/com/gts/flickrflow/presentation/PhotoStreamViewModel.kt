package com.gts.flickrflow.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gts.flickrflow.data.network.PhotoRepository
import kotlinx.coroutines.launch
import com.gts.flickrflow.core.Result
import com.gts.flickrflow.domain.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoStreamViewModel(private val photoRepository: PhotoRepository) : ViewModel() {

    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo>
        get() = _photo

    fun startPhotoStreamBasedOnLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = photoRepository.searchByLocation("38.319859", "23.322901")
            withContext(Dispatchers.Main) {
                when (result) {
                    is Result.Success -> _photo.postValue(result.data)
                    is Result.Error -> throw result.exception
                }
            }
        }
    }
}