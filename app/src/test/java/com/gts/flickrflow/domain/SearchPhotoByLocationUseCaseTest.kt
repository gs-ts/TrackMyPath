package com.gts.flickrflow.domain

import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

import java.io.IOException

import kotlinx.coroutines.runBlocking

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import com.gts.flickrflow.common.Result
import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.domain.usecase.SearchPhotoByLocationUseCase

class SearchPhotoByLocationUseCaseTest {

    private lateinit var searchPhotoByLocationUseCase: SearchPhotoByLocationUseCase
    private val mockPhotoRepository: PhotoRepository = mock()
    private val lat = 1.0
    private val lon = 1.0
    private val photo = Photo("id", "secret", "server", "farm")

    @Before
    fun setUp() {
        searchPhotoByLocationUseCase =
            SearchPhotoByLocationUseCase(
                mockPhotoRepository
            )
    }

    @Test
    fun `searchPhotoByLocation get success`() {
        runBlocking {
            // given
            val expected = Result.Success(photo)
            whenever(mockPhotoRepository.searchPhotoByLocation(lat.toString(), lon.toString())).thenReturn(expected)
            // when
            val result = searchPhotoByLocationUseCase.invoke(lat, lon)
            // then
            verify(mockPhotoRepository).searchPhotoByLocation(lat.toString(), lon.toString())
            assertEquals(expected, result)
        }
    }

    @Test
    fun `searchPhotoByLocation get error`() {
        runBlocking {
            // given
            val expected = Result.Error(IOException("searchPhotoByLocation response error"))
            whenever(mockPhotoRepository.searchPhotoByLocation(lat.toString(), lon.toString())).thenReturn(expected)
            // when
            val result = searchPhotoByLocationUseCase.invoke(lat, lon)
            // then
            verify(mockPhotoRepository).searchPhotoByLocation(lat.toString(), lon.toString())
            assertEquals(expected, result)
        }
    }
}