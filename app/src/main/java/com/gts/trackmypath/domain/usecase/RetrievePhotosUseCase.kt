package com.gts.trackmypath.domain.usecase

import javax.inject.Inject
import dagger.hilt.android.scopes.ActivityRetainedScoped

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.domain.PhotoRepository
import com.gts.trackmypath.domain.model.Photo

@ActivityRetainedScoped
class RetrievePhotosUseCase @Inject constructor(private val photoRepository: PhotoRepository) {

    suspend operator fun invoke(): Result<List<Photo>> = withContext(Dispatchers.IO) {
        return@withContext photoRepository.loadAllPhotos()
    }
}
