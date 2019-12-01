package com.gts.flickrflow.data.network

import com.gts.flickrflow.common.Result

interface FlickrDataSource {

    suspend fun searchPhoto(lat: String, lon: String, radius: String): Result<PhotoResponseEntity>
}