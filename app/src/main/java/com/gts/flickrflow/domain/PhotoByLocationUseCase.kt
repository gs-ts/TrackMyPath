package com.gts.flickrflow.domain

import com.gts.flickrflow.core.Result
import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.data.network.PhotoRepository

class PhotoByLocationUseCase(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke(lat: String, lon: String): Result<Photo> {
        return photoRepository.searchByLocation(lat, lon)
    }
}