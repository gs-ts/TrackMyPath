package com.gts.trackmypath.domain.usecase

import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.gts.trackmypath.domain.PhotoRepository

class ClearPhotosFromDbUseCase @Inject constructor(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        photoRepository.deletePhotos()
    }
}
