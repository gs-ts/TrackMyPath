package com.gts.trackmypath.domain

import org.junit.Test
import org.junit.Before
import org.junit.Assert.assertEquals

import java.io.IOException

import kotlinx.coroutines.runBlocking

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.domain.model.Photo
import com.gts.trackmypath.domain.usecase.RetrievePhotosUseCase

class RetrievePhotosUseCaseTest {

    private lateinit var retrievePhotosUseCase: RetrievePhotosUseCase
    private val mockPhotoRepository: PhotoRepository = mock()
    private val photo = Photo("id", "secret", "server", "farm")

    @Before
    fun setUp() {
        retrievePhotosUseCase =
            RetrievePhotosUseCase(
                mockPhotoRepository
            )
    }

    @Test
    fun `when repository succeeds to retrieve photos then retrieve photos usecase returns success with photos`() {
        runBlocking {
            // given
            val expected = Result.Success(listOf(photo))
            whenever(mockPhotoRepository.loadAllPhotos()).thenReturn(expected)
            // when
            val result = retrievePhotosUseCase.invoke()
            // then
            verify(mockPhotoRepository).loadAllPhotos()
            assertEquals(expected, result)
        }
    }

    @Test
    fun `when repository fails to retrieve photos then retrieve photos usecase returns error`() {
        runBlocking {
            // given
            val expected = Result.Error(IOException("Failed to retrieve photos from database"))
            whenever(mockPhotoRepository.loadAllPhotos()).thenReturn(expected)
            // when
            val result = retrievePhotosUseCase.invoke()
            // then
            verify(mockPhotoRepository).loadAllPhotos()
            assertEquals(expected, result)
        }
    }
}
