package com.gts.trackmypath.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import androidx.hilt.lifecycle.ViewModelInject

import kotlinx.coroutines.launch

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.domain.usecase.RetrievePhotosUseCase
import com.gts.trackmypath.presentation.model.PhotoViewItem
import com.gts.trackmypath.presentation.model.toPresentationModel

import timber.log.Timber

class PhotoStreamViewModel @ViewModelInject constructor(
    private val retrievePhotosUseCase: RetrievePhotosUseCase
) : ViewModel() {

    private val _photosFromDb = MutableLiveData<List<PhotoViewItem>>()
    val photosFromDb: LiveData<List<PhotoViewItem>>
        get() = _photosFromDb

    fun retrievePhotosFromDb() {
        viewModelScope.launch {
            when (val result = retrievePhotosUseCase.invoke()) {
                is Result.Success -> {
                    _photosFromDb.postValue(result.data.map { it.toPresentationModel() })
                }
                is Result.Error -> Timber.d("no photos from DB")
            }
        }
    }
}
