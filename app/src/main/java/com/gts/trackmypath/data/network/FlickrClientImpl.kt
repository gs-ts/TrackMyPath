package com.gts.trackmypath.data.network

import java.io.IOException
import java.lang.Exception

import com.gts.trackmypath.common.Result

import timber.log.Timber

class FlickrClientImpl(private val flickrApi: FlickrApi) : FlickrClient {

    // request a photo from flickr service based on current location
    override suspend fun searchPhoto(
        lat: String,
        lon: String,
        radius: String
    ): Result<List<PhotoResponseEntity>> {
        return try {
            val response = flickrApi.search(FlickrApi.API_KEY, lat, lon, radius)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    // return the first result from response
                    return Result.Success(data.photosResponseEntity.list)
                } else {
                    Timber.e("searchPhotoByLocation data error")
                    Result.Error(IOException("searchPhotoByLocation data error"))
                }
            } else {
                Timber.e("searchPhotoByLocation response error")
                Result.Error(IOException("searchPhotoByLocation response error"))
            }
        } catch (e: Exception) {
            Timber.e(e, "searchPhotoByLocation exception")
            Result.Error(IOException("searchPhotoByLocation exception", e))
        }
    }
}
