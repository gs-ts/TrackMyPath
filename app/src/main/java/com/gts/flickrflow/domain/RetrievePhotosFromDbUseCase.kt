package com.gts.flickrflow.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.gts.flickrflow.common.Result
import com.gts.flickrflow.domain.model.Photo

class RetrievePhotosFromDbUseCase(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke(): Result<List<Photo>> = withContext(Dispatchers.IO) {
        return@withContext photoRepository.loadAllPhotos()
    }
}