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
import com.gts.trackmypath.domain.usecase.RetrievePhotosFromDbUseCase

class RetrievePhotosResponseEntityFromDbUseCaseTest {

    private lateinit var retrievePhotosFromDbUseCase: RetrievePhotosFromDbUseCase
    private val mockPhotoRepository: PhotoRepository = mock()
    private val photo = Photo("id", "secret", "server", "farm")

    @Before
    fun setUp() {
        retrievePhotosFromDbUseCase =
            RetrievePhotosFromDbUseCase(
                mockPhotoRepository
            )
    }

    @Test
    fun `retrievePhotosFromDb get success`() {
        runBlocking {
            // given
            val expected = Result.Success(listOf(photo))
            whenever(mockPhotoRepository.loadAllPhotos()).thenReturn(expected)
            // when
            val result = retrievePhotosFromDbUseCase.invoke()
            // then
            verify(mockPhotoRepository).loadAllPhotos()
            assertEquals(expected, result)
        }
    }

    @Test
    fun `retrievePhotosFromDb get error`() {
        runBlocking {
            // given
            val expected = Result.Error(IOException("Failed to retrieve photos from database"))
            whenever(mockPhotoRepository.loadAllPhotos()).thenReturn(expected)
            // when
            val result = retrievePhotosFromDbUseCase.invoke()
            // then
            verify(mockPhotoRepository).loadAllPhotos()
            assertEquals(expected, result)
        }
    }
}
