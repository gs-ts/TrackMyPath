package com.gts.flickrflow

import androidx.room.Room

import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.android.ext.koin.androidApplication

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory

import com.gts.flickrflow.data.database.PhotoDatabase
import com.gts.flickrflow.data.network.FlickrApi
import com.gts.flickrflow.data.network.PhotoRepository
import com.gts.flickrflow.data.network.PhotoRepositoryImpl
import com.gts.flickrflow.domain.EmptyPhotosDbUseCase
import com.gts.flickrflow.domain.RetrievePhotosFromDbUseCase
import com.gts.flickrflow.domain.SearchPhotoByLocationUseCase
import com.gts.flickrflow.presentation.PhotoStreamViewModel

val appModule = module {
    single { provideFlickrApi().create(FlickrApi::class.java) }
    single { Room.databaseBuilder(androidApplication(), PhotoDatabase::class.java, "photo-db").build() }
    single { get<PhotoDatabase>().photoDao() }
    single<PhotoRepository> {
        PhotoRepositoryImpl(flickrApi = get(), photoDao = get())
    }
    single { SearchPhotoByLocationUseCase(photoRepository = get()) }
    single { EmptyPhotosDbUseCase(photoRepository = get()) }
    single { RetrievePhotosFromDbUseCase(photoRepository = get()) }
    viewModel { PhotoStreamViewModel(retrievePhotosFromDbUseCase = get()) }
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

private val jsonMoshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun provideFlickrApi(): Retrofit {
    return Retrofit.Builder()
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(jsonMoshi))
        .baseUrl("https://api.flickr.com/")
        .client(okHttpClient)
        .build()
}