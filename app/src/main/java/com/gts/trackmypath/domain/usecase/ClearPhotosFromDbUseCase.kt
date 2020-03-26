package com.gts.trackmypath.domain.usecase

import com.gts.trackmypath.domain.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ClearPhotosFromDbUseCase(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        photoRepository.deletePhotos()
    }
}
