package com.gts.flickrflow.data.network

import com.squareup.moshi.Json

import com.gts.flickrflow.data.database.PhotoEntity
import com.gts.flickrflow.domain.model.Photo

data class PhotosResponse(
    @Json(name = "photos")
    val photos: Photos
)

data class Photos(
    @Json(name = "page")
    val page: Int,
    @Json(name = "pages")
    val pages: Int,
    @Json(name = "perpage")
    val perpage: String,
    @Json(name = "total")
    val total: String,
    @Json(name = "photo")
    val list: List<PhotoResponse>
)

data class PhotoResponse(
    @Json(name = "id")
    val id: String,
    @Json(name = "secret")
    val secret: String,
    @Json(name = "server")
    val server: String,
    @Json(name = "farm")
    val farm: String
)

// map to DB entity, PhotoEntity
fun PhotoResponse.toPhotoEntity() = PhotoEntity(
    photoId = 0,
    id = id,
    secret = secret,
    server = server,
    farm = farm
)

// map to data class, Photo
fun PhotoResponse.toPhotoModel() = Photo(
    id = id,
    secret = secret,
    server = server,
    farm = farm
)