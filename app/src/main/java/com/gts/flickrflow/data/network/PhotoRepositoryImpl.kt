package com.gts.flickrflow.data.network

import java.io.IOException
import java.lang.Exception

import com.gts.flickrflow.core.Result
import com.gts.flickrflow.data.database.PhotoDao
import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.domain.model.toPhoto

import timber.log.Timber

class PhotoRepositoryImpl(private val flickrApi: FlickrApi, private val photoDao: PhotoDao) : PhotoRepository {

    override suspend fun searchPhotoByLocation(lat: String, lon: String): Result<Photo> {
        return try {
            val response = flickrApi.search(FlickrApi.API_KEY, lat, lon).await()

            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    photoDao.insert(data.photos.list[0].toPhotoEntity()) // take the first result from response
                    return Result.Success(data.photos.list[0].toPhoto())
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

    override suspend fun loadAllPhotos(): Result<List<Photo>> {
        val photos = photoDao.loadAllPhotos()
        return if (photos.isNotEmpty()) {
            val result = photos.map {  photoEntity ->  photoEntity.toPhoto() }
            Result.Success(result)
        } else {
            Result.Error(IOException("Failed to retrieve photos from database"))
        }

    }

    override suspend fun deletePhotos() {
        try {
            photoDao.deletePhotos()
        } catch (e: Exception) {
            Timber.e(e, "deletePhotos exception")
        }
    }
}