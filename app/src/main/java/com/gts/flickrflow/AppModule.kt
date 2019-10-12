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

import com.gts.flickrflow.data.network.FlickrService
import com.gts.flickrflow.data.database.PhotoDatabase
import com.gts.flickrflow.data.network.PhotoRepositoryImpl
import com.gts.flickrflow.domain.PhotoRepository
import com.gts.flickrflow.domain.EmptyPhotosDbUseCase
import com.gts.flickrflow.domain.RetrievePhotosFromDbUseCase
import com.gts.flickrflow.domain.SearchPhotoByLocationUseCase
import com.gts.flickrflow.presentation.PhotoStreamViewModel

// declare a module
val appModule = module {
    // Define single instance of Retrofit
    single { provideFlickrApi().create(FlickrService::class.java) }
    // Define single instance of RoomDatabase.Builder
    // RoomDatabase.Builder for a persistent database
    // Once a database is built, you should keep a reference to it and re-use it
    single { Room.databaseBuilder(androidApplication(), PhotoDatabase::class.java, "photo-db").build() }
    // Define single instance of PhotoDatabase
    single { get<PhotoDatabase>().photoDao() }
    // Define single instance of type PhotoRepository
    // Resolve constructor dependencies with get(), here we need a flickrApi and photoDao
    single<PhotoRepository> {
        PhotoRepositoryImpl(flickrService = get(), photoDao = get())
    }
    // Define single instance of SearchPhotoByLocationUseCase
    // Resolve constructor dependencies with get(), here we need a photoRepository
    single { SearchPhotoByLocationUseCase(photoRepository = get()) }
    // Define single instance of EmptyPhotosDbUseCase
    // Resolve constructor dependencies with get(), here we need a photoRepository
    single { EmptyPhotosDbUseCase(photoRepository = get()) }
    // Define single instance of RetrievePhotosFromDbUseCase
    // Resolve constructor dependencies with get(), here we need a photoRepository
    single { RetrievePhotosFromDbUseCase(photoRepository = get()) }
    // Define ViewModel and resolve constructor dependencies with get(),
    // here we need retrievePhotosFromDbUseCase
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
        .addConverterFactory(MoshiConverterFactory.create(jsonMoshi))
        .baseUrl("https://api.flickr.com/")
        .client(okHttpClient)
        .build()
}