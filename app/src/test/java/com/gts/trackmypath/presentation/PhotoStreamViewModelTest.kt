package com.gts.trackmypath.presentation

import org.junit.Test
import org.junit.Rule
import org.junit.Before
import org.junit.Assert

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever

import com.gts.trackmypath.common.Result
import com.gts.trackmypath.domain.model.Photo
import com.gts.trackmypath.domain.usecase.RetrievePhotosUseCase
import com.gts.trackmypath.presentation.model.PhotoViewItem
import com.gts.trackmypath.presentation.model.toPresentationModel

class PhotoStreamViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PhotoStreamViewModel
    private val mockRetrievePhotosUseCase: RetrievePhotosUseCase = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = PhotoStreamViewModel(mockRetrievePhotosUseCase)
    }

    @Test
    fun retrievePhotos_sendsListOfPhotos() {
        val photo = Photo("id", "secret", "server", "farm")
        val expected = Result.Success(listOf(photo))
        runBlocking { whenever(mockRetrievePhotosUseCase.invoke()).thenReturn(expected) }
        // when
        viewModel.retrievePhotos()
        // then
        val photoList: List<PhotoViewItem>? = LiveDataTestUtil.getValue(viewModel.photosByLocation)
        Assert.assertNotNull(photoList)
        Assert.assertEquals(expected.data.map { it.toPresentationModel() }, photoList)
    }
}
