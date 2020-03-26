package com.gts.trackmypath.domain

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.domain.model.Photo

// Repository modules handle data operations.
// They provide a clean API so that the rest of the app can retrieve this data easily.
interface PhotoRepository {

    suspend fun searchPhotoByLocation(lat: String, lon: String): Result<Photo>

    suspend fun loadAllPhotos(): Result<List<Photo>>

    suspend fun deletePhotos()
}
