package com.gts.flickrflow.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.gts.flickrflow.data.network.PhotoRepository

class DeletePhotosUseCase(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        photoRepository.deletePhotos()
    }
}