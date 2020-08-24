package com.gts.trackmypath

import android.content.Context
import androidx.room.Room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.components.ApplicationComponent

import javax.inject.Qualifier
import javax.inject.Singleton

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
import com.gts.trackmypath.data.database.PhotoDao
import com.gts.trackmypath.data.database.PhotoDatabase
import com.gts.trackmypath.domain.PhotoRepository

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providePhotoRepository(
        @AppModule.RemoteDataSource remoteDataSource: FlickrClient,
        @AppModule.LocalDataSource localDataSource: PhotoDao
    ): PhotoRepository {
        return PhotoRepositoryImpl(remoteDataSource, localDataSource)
    }
}

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class RemoteDataSource

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class LocalDataSource

    @Singleton
    @RemoteDataSource
    @Provides
    fun provideFlickrClient(
        retrofit: Retrofit
    ): FlickrClient {
        return FlickrClientImpl(retrofit.create(FlickrApi::class.java) )
    }

    @Singleton
    @LocalDataSource
    @Provides
    fun providePhotoDAO(photoDatabase: PhotoDatabase): PhotoDao {
        return photoDatabase.photoDao()
    }

    @Singleton
    @Provides
    fun providePhotoDatabase(@ApplicationContext context: Context): PhotoDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PhotoDatabase::class.java,
            "photos.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideFlickrApi(
        @HttpClient okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.flickr.com/")
            .client(okHttpClient)
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class HttpClient

    @HttpClient
    @Provides
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

    @Singleton
    @Provides
    fun provideJsonMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}
