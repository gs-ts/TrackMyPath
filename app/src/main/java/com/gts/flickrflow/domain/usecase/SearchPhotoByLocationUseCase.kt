package com.gts.flickrflow.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.gts.flickrflow.common.Result
import com.gts.flickrflow.domain.PhotoRepository
import com.gts.flickrflow.domain.model.Photo

class SearchPhotoByLocationUseCase(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke(lat: Double, lon: Double): Result<Photo> = withContext(Dispatchers.IO) {
        return@withContext photoRepository.searchPhotoByLocation(lat.toString(), lon.toString())
    }
}