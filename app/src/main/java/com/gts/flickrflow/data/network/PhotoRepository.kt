package com.gts.flickrflow.data.network

import com.gts.flickrflow.common.Result
import com.gts.flickrflow.domain.model.Photo

// Repository modules handle data operations.
// They provide a clean API so that the rest of the app can retrieve this data easily.
interface PhotoRepository {

    suspend fun searchPhotoByLocation(lat: String, lon: String): Result<Photo>

    suspend fun loadAllPhotos(): Result<List<Photo>>

    suspend fun deletePhotos()
}