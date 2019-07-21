package com.gts.flickrflow.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.gts.flickrflow.core.Result
import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.data.network.PhotoRepository

class SearchByLocationUseCase(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke(lat: String, lon: String): Result<Photo> = withContext(Dispatchers.IO) {
        return@withContext photoRepository.searchByLocation(lat, lon)
    }
}