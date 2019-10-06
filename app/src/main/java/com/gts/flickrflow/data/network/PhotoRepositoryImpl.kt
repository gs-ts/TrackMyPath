package com.gts.flickrflow.data.network

import java.io.IOException
import java.lang.Exception

import com.gts.flickrflow.common.Result
import com.gts.flickrflow.data.database.PhotoDao
import com.gts.flickrflow.data.database.toPhotoModel
import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.domain.PhotoRepository

import timber.log.Timber

// In this app only one data source is used to fetch data (photos), the flickr service.
// Then the fetched data are stored in the database.
// The fetched photos can be then used by the view to create a flow of images.
class PhotoRepositoryImpl(private val flickrApi: FlickrApi, private val photoDao: PhotoDao) :
    PhotoRepository {

    // request a photo from flickr service based on current location
    override suspend fun searchPhotoByLocation(lat: String, lon: String): Result<Photo> {
        return try {
            val response = flickrApi.search(FlickrApi.API_KEY, lat, lon).await()
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    // take the first result from response
                    // and save it in the DB
                    photoDao.insert(data.photos.list[0].toPhotoEntity())
                    return Result.Success(data.photos.list[0].toPhotoModel())
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

    // Retrieve all photos from the DB
    override suspend fun loadAllPhotos(): Result<List<Photo>> {
        val photos = photoDao.loadAllPhotos()
        return if (photos.isNotEmpty()) {
            val result = photos.map {  photoEntity ->  photoEntity.toPhotoModel() }
            Result.Success(result)
        } else {
            Result.Error(IOException("Failed to retrieve photos from database"))
        }

    }

    // Delete all photos in DB
    override suspend fun deletePhotos() {
        try {
            photoDao.deletePhotos()
        } catch (e: Exception) {
            Timber.e(e, "deletePhotos exception")
        }
    }
}