package com.gts.trackmypath.domain

import javax.inject.Inject
import dagger.hilt.android.scopes.ServiceScoped

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.domain.model.Photo
import com.gts.trackmypath.domain.usecase.ClearPhotosFromDbUseCase
import com.gts.trackmypath.domain.usecase.SearchPhotoByLocationUseCase

@ServiceScoped
class LocationServiceInteractor @Inject constructor(
    private val clearPhotosFromDbUseCase: ClearPhotosFromDbUseCase,
    private val searchPhotoByLocationUseCase: SearchPhotoByLocationUseCase
) {

    suspend fun clearPhotosFromList() {
        clearPhotosFromDbUseCase.invoke()
    }

    suspend fun getPhotoBasedOnLocation(latitude: Double, longitude: Double): Result<Photo> {
        return searchPhotoByLocationUseCase.invoke(latitude, longitude)
    }
}
