package com.gts.trackmypath

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

import org.koin.dsl.module
import org.koin.core.qualifier.named
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.android.ext.koin.androidApplication

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

import com.gts.trackmypath.data.network.FlickrApi
import com.gts.trackmypath.data.network.FlickrDataSource
import com.gts.trackmypath.data.network.FlickrDataSourceImpl
import com.gts.trackmypath.data.PhotoRepositoryImpl
import com.gts.trackmypath.data.database.PhotoDatabase
import com.gts.trackmypath.domain.PhotoRepository
import com.gts.trackmypath.domain.usecase.ClearPhotosFromDbUseCase
import com.gts.trackmypath.domain.usecase.RetrievePhotosFromDbUseCase
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
    // Define single instance of type FlickrDataSource
    // Resolve constructor dependencies with get(), here we need a flickrApi
    single<FlickrDataSource> { FlickrDataSourceImpl(flickrApi = get()) }
    // Define single instance of type PhotoRepository
    // Resolve constructor dependencies with get(), here we need a flickrApi and photoDao
    single<PhotoRepository> {
        PhotoRepositoryImpl(flickrDataSource = get(), photoDao = get())
    }
    // Define single instance of SearchPhotoByLocationUseCase
    // Resolve constructor dependencies with get(), here we need a photoRepository
    single {
        SearchPhotoByLocationUseCase(
            photoRepository = get()
        )
    }
    // Define single instance of ClearPhotosFromDbUseCase
    // Resolve constructor dependencies with get(), here we need a photoRepository
    single {
        ClearPhotosFromDbUseCase(
            photoRepository = get()
        )
    }
    // Define single instance of RetrievePhotosFromDbUseCase
    // Resolve constructor dependencies with get(), here we need a photoRepository
    single {
        RetrievePhotosFromDbUseCase(
            photoRepository = get()
        )
    }
    single(named("EncrSharedPrefs")) {
        provideEncryptedSharedPreferences(applicationContext = androidApplication())
    }
    // Define ViewModel and resolve constructor dependencies with get(),
    // here we need retrievePhotosFromDbUseCase
    viewModel { PhotoStreamViewModel(retrievePhotosFromDbUseCase = get()) }
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

// Step 1: Create or retrieve the Master Key for encryption/decryption
private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

// Step 2: Initialize/open an instance of EncryptedSharedPreferences
private fun provideEncryptedSharedPreferences(applicationContext: Context) = EncryptedSharedPreferences.create(
    "appPreferences",
    masterKeyAlias,
    applicationContext,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
