package com.gts.trackmypath.domain.usecase

import javax.inject.Inject

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.domain.PhotoRepository
import com.gts.trackmypath.domain.model.Photo

class SearchPhotoByLocationUseCase @Inject constructor(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke(lat: Double, lon: Double): Result<Photo> = withContext(Dispatchers.IO) {
        return@withContext photoRepository.searchPhotoByLocation(lat.toString(), lon.toString())
    }
}
