package com.gts.flickrflow.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch

import com.gts.flickrflow.common.Result
import com.gts.flickrflow.domain.usecase.RetrievePhotosFromDbUseCase
import com.gts.flickrflow.presentation.model.PhotoViewItem
import com.gts.flickrflow.presentation.model.toPresentationModel

import timber.log.Timber

class PhotoStreamViewModel(
    private val retrievePhotosFromDbUseCase: RetrievePhotosFromDbUseCase
) : ViewModel() {

    private val _photosFromDb = MutableLiveData<List<PhotoViewItem>>()
    val photosFromDb: LiveData<List<PhotoViewItem>>
        get() = _photosFromDb

    fun retrievePhotosFromDb() {
        viewModelScope.launch {
            when (val result = retrievePhotosFromDbUseCase.invoke()) {
                is Result.Success -> {
                    _photosFromDb.postValue(result.data.map { it.toPresentationModel() })
                }
                is Result.Error -> Timber.d("no photos from DB")
            }
        }
    }
}