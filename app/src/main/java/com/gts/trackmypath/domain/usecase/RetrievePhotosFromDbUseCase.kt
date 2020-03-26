package com.gts.trackmypath.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.domain.PhotoRepository
import com.gts.trackmypath.domain.model.Photo

class RetrievePhotosFromDbUseCase(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke(): Result<List<Photo>> = withContext(Dispatchers.IO) {
        return@withContext photoRepository.loadAllPhotos()
    }
}
