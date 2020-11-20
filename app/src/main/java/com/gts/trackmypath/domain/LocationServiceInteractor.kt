package com.gts.trackmypath.domain

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.domain.model.Photo
import com.gts.trackmypath.domain.usecase.ClearPhotosUseCase
import com.gts.trackmypath.domain.usecase.SearchPhotoByLocationUseCase

class LocationServiceInteractor(
    private val clearPhotosUseCase: ClearPhotosUseCase,
    private val searchPhotoByLocationUseCase: SearchPhotoByLocationUseCase
) {

    suspend fun clearPhotosFromList() {
        clearPhotosUseCase.invoke()
    }

    suspend fun getPhotoBasedOnLocation(latitude: Double, longitude: Double): Result<Photo> {
        return searchPhotoByLocationUseCase.invoke(latitude, longitude)
    }
}
