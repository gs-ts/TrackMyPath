package com.gts.trackmypath.data.repository

import org.junit.Test
import org.junit.Before
import org.junit.Assert

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import kotlinx.coroutines.runBlocking

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.data.PhotoRepositoryImpl
import com.gts.trackmypath.data.database.PhotoDao
import com.gts.trackmypath.data.database.PhotoEntity
import com.gts.trackmypath.data.network.toDomainModel
import com.gts.trackmypath.data.network.toPhotoEntity
import com.gts.trackmypath.data.network.FlickrDataSource
import com.gts.trackmypath.data.network.PhotoResponseEntity

class PhotoRepositoryImplTest {

    private lateinit var repository: PhotoRepositoryImpl

    private val mockFlickrDataSource: FlickrDataSource = mock()
    private val mockPhotoDatabase: PhotoDao = mock()

    private val lat = "lat"
    private val lon = "lon"
    private val radius = "0.1"
    private val id = "id"
    private val secret = "secret"
    private val server = "server"
    private val farm = "farm"
    private val photoResponseEntity = PhotoResponseEntity(id, secret, server, farm)
    private val photoEntity = photoResponseEntity.toPhotoEntity()

    @Before
    fun setUp() {
        repository = PhotoRepositoryImpl(mockFlickrDataSource, mockPhotoDatabase)
    }

    @Test
    fun `given empty database, when searchPhotoByLocation, then success result with the first photo`() {
        runBlocking {
            val photosFromDb = arrayOf<PhotoEntity>()
            whenever(mockPhotoDatabase.selectAllPhotos()).thenReturn(photosFromDb)
            val result = Result.Success(listOf(photoResponseEntity))
            whenever(mockFlickrDataSource.searchPhoto(lat, lon, radius)).thenReturn(result)
            whenever(mockPhotoDatabase.insert(photoEntity)).thenReturn(Unit)

            val test = repository.searchPhotoByLocation(lat, lon)

            verify(mockFlickrDataSource).searchPhoto(lat, lon, radius)
            Assert.assertEquals(test, Result.Success(result.data[0].toDomainModel()))
        }
    }

}