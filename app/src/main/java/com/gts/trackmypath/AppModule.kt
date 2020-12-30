package com.gts.trackmypath

import android.content.Context
import androidx.room.Room

import dagger.Module
import dagger.Binds
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

import com.gts.trackmypath.data.PhotoRepositoryImpl
import com.gts.trackmypath.data.database.PhotoDao
import com.gts.trackmypath.data.database.PhotoDatabase
import com.gts.trackmypath.data.network.FlickrApi
import com.gts.trackmypath.data.network.FlickrClient
import com.gts.trackmypath.domain.PhotoRepository

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providerHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(run {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.apply {
                    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                }
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideJsonMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.flickr.com/")
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providePhotoDatabase(@ApplicationContext context: Context): PhotoDatabase {
        return Room.databaseBuilder(context, PhotoDatabase::class.java, "photos.db").build()
    }

    @Provides
    @Singleton
    fun provideFlickrClient(retrofit: Retrofit): FlickrClient {
        return FlickrClient(retrofit.create(FlickrApi::class.java) )
    }

    @Provides
    @Singleton
    fun providePhotoDAO(photoDatabase: PhotoDatabase): PhotoDao {
        return photoDatabase.photoDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PhotoRepositoryModule {

    // https://developer.android.com/training/dependency-injection/hilt-android#component-scopes
    @Singleton
    @Binds
    abstract fun bindPhotoRepository(
        photoRepositoryImpl: PhotoRepositoryImpl
    ): PhotoRepository
}
