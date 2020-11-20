package com.gts.trackmypath

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

import com.gts.trackmypath.data.network.FlickrApi
import com.gts.trackmypath.data.network.FlickrClient
import com.gts.trackmypath.data.network.FlickrClientImpl
import com.gts.trackmypath.data.PhotoRepositoryImpl
import com.gts.trackmypath.data.database.PhotoDatabase
import com.gts.trackmypath.domain.LocationServiceInteractor
import com.gts.trackmypath.domain.PhotoRepository
import com.gts.trackmypath.domain.usecase.ClearPhotosUseCase
import com.gts.trackmypath.domain.usecase.RetrievePhotosUseCase
import com.gts.trackmypath.domain.usecase.SearchPhotoByLocationUseCase
import com.gts.trackmypath.presentation.PhotoStreamViewModel

// declare a module
val appModule = module {
    // Define single instance of Retrofit
    single { provideFlickrApi().create(FlickrApi::class.java) }
    // Define single instance of RoomDatabase.Builder
    // RoomDatabase.Builder for a persistent database
    // Once a database is built, you should keep a reference to it and re-use it
    single { Room.databaseBuilder(androidApplication(), PhotoDatabase::class.java, "photo-db").build() }
    // Define single instance of PhotoDatabase
    single { get<PhotoDatabase>().photoDao() }
    // Define single instance of type FlickrClient
    // Resolve constructor dependencies with get(), here we need a flickrApi
    single<FlickrClient> { FlickrClientImpl(flickrApi = get()) }
    // Define single instance of type PhotoRepository
    // Resolve constructor dependencies with get(), here we need a flickrApi and photoDao
    single<PhotoRepository> {
        PhotoRepositoryImpl(flickrClient = get(), photoDao = get())
    }
    // Define single instance of SearchPhotoByLocationUseCase
    // Resolve constructor dependencies with get(), here we need a photoRepository
    single {
        SearchPhotoByLocationUseCase(
            photoRepository = get()
        )
    }
    // Define single instance of ClearPhotosUseCase
    // Resolve constructor dependencies with get(), here we need a photoRepository
    single {
        ClearPhotosUseCase(
            photoRepository = get()
        )
    }
    // Define single instance of RetrievePhotosUseCase
    // Resolve constructor dependencies with get(), here we need a photoRepository
    single {
        RetrievePhotosUseCase(
            photoRepository = get()
        )
    }
    // Define single instance of LocationServiceInteractor
    // Resolve constructor dependencies with get(), here we need a ClearPhotosUseCase,
    // and a SearchPhotoByLocationUseCase
    single {
        LocationServiceInteractor(
            clearPhotosUseCase = get(),
            searchPhotoByLocationUseCase = get()
        )
    }
    // Define ViewModel and resolve constructor dependencies with get(),
    // here we need retrievePhotosUseCase
    viewModel { PhotoStreamViewModel(retrievePhotosUseCase = get()) }
}

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    })
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
