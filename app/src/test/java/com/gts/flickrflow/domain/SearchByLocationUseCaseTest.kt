package com.gts.flickrflow.domain

import com.gts.flickrflow.core.Result
import com.gts.flickrflow.data.network.PhotoRepository
import com.gts.flickrflow.domain.model.Photo
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import java.io.IOException

class SearchByLocationUseCaseTest {

    private lateinit var searchByLocationUseCase: SearchByLocationUseCase
    private val mockPhotoRepository: PhotoRepository = mock()
    private val lat = 1.0
    private val lon = 1.0
    private val photo = Photo("id", "secret", "server", "farm")

    @Before
    fun setUp() {
        searchByLocationUseCase = SearchByLocationUseCase(mockPhotoRepository)
    }

    @Test
    fun `searchByLocation get success`() {
        runBlocking {
            // given
            val expected = Result.Success(photo)
            whenever(mockPhotoRepository.searchByLocation(lat.toString(), lon.toString())).thenReturn(expected)
            // when
            val result = searchByLocationUseCase.invoke(lat, lon)
            // then
            verify(mockPhotoRepository).searchByLocation(lat.toString(), lon.toString())
            assertEquals(expected, result)
        }
    }

    @Test
    fun `searchByLocation get error`() {
        runBlocking {
            // given
            val expected = Result.Error(IOException("searchByLocation response error"))
            whenever(mockPhotoRepository.searchByLocation(lat.toString(), lon.toString())).thenReturn(expected)
            // when
            val result = searchByLocationUseCase.invoke(lat, lon)
            // then
            verify(mockPhotoRepository).searchByLocation(lat.toString(), lon.toString())
            assertEquals(expected, result)
        }
    }
}