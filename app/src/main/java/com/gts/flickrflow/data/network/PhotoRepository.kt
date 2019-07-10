package com.gts.flickrflow.data.network

import com.gts.flickrflow.core.Result
import com.gts.flickrflow.domain.model.Photo

interface PhotoRepository {

    suspend fun searchByLocation(lat: String, lon: String): Result<Photo>
}