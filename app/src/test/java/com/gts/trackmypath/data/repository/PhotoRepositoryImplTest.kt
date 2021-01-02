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
import com.gts.trackmypath.data.network.FlickrClient
import com.gts.trackmypath.data.network.toDomainModel
import com.gts.trackmypath.data.network.toPhotoEntity
import com.gts.trackmypath.data.network.PhotoResponseEntity

class PhotoRepositoryImplTest {

    private lateinit var repository: PhotoRepositoryImpl

    private val mockFlickrClient: FlickrClient = mock()
    private val mockPhotoDatabase: PhotoDao = mock()

    private val lat = "lat"
    private val lon = "lon"
    private val radius = "0.1"
    private val id1 = "id1"
    private val id2 = "id2"
    private val secret = "secret"
    private val server = "server"
    private val farm = "farm"
    private val photoResponseEntity1 = PhotoResponseEntity(id1, secret, server, farm)
    private val photoEntity1 = photoResponseEntity1.toPhotoEntity()
    private val photoResponseEntity2 = PhotoResponseEntity(id2, secret, server, farm)
    private val photoEntity2 = photoResponseEntity2.toPhotoEntity()

    @Before
    fun setUp() {
        repository = PhotoRepositoryImpl(mockFlickrClient, mockPhotoDatabase)
    }

    @Test
    fun `given empty database, when searchPhotoByLocation, then return the first photo`() {
        runBlocking {
            val photosFromDb = arrayOf<PhotoEntity>()
            whenever(mockPhotoDatabase.selectAllPhotos()).thenReturn(photosFromDb)

            val photosFromFlickr = listOf(photoResponseEntity1)
            val result = Result.Success(photosFromFlickr)
            whenever(mockFlickrClient.searchPhoto(lat, lon, radius)).thenReturn(result)
            whenever(mockPhotoDatabase.insert(photoEntity1)).thenReturn(Unit)

            val test = repository.searchPhotoByLocation(lat, lon)

            verify(mockFlickrClient).searchPhoto(lat, lon, radius)
            Assert.assertEquals(test, Result.Success(result.data[0].toDomainModel()))
        }
    }

    @Test
    fun `given filled database, when searchPhotoByLocation returns existing photo, then return next photo`() {
        runBlocking {
            val photosFromDb = arrayOf<PhotoEntity>(photoEntity1)
            whenever(mockPhotoDatabase.selectAllPhotos()).thenReturn(photosFromDb)

            val photosFromFlickr = listOf(photoResponseEntity1, photoResponseEntity2)
            val result = Result.Success(photosFromFlickr)
            whenever(mockFlickrClient.searchPhoto(lat, lon, radius)).thenReturn(result)
            whenever(mockPhotoDatabase.insert(photoEntity2)).thenReturn(Unit)

            val test = repository.searchPhotoByLocation(lat, lon)

            verify(mockFlickrClient).searchPhoto(lat, lon, radius)
            Assert.assertEquals(test, Result.Success(result.data[1].toDomainModel()))
        }
    }

}
