package com.gts.flickrflow.data.network

import java.io.IOException
import java.lang.Exception

import com.gts.flickrflow.core.Result
import com.gts.flickrflow.data.database.PhotoDao
import com.gts.flickrflow.data.model.toPhotoEntity
import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.domain.model.toPhoto

import timber.log.Timber

class PhotoRepositoryImpl(private val flickrApi: FlickrApi, private val photoDao: PhotoDao) : PhotoRepository {

    override suspend fun searchByLocation(lat: String, lon: String): Result<Photo> {
        return try {
            val response = flickrApi.search(FlickrApi.API_KEY, lat, lon).await()

            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    photoDao.insert(data.photos.list[0].toPhotoEntity())
                    return Result.Success(data.photos.list[0].toPhoto())
                } else {
                    Timber.e("searchByLocation error")
                    Result.Error(IOException("searchByLocation error"))
                }
            } else {
                Timber.e("searchByLocation error")
                Result.Error(IOException("searchByLocation error"))
            }
        } catch (e: Exception) {
            Timber.e(e, "searchByLocation error")
            Result.Error(IOException("searchByLocation error", e))
        }
    }
}