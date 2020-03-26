package com.gts.trackmypath.data.network

import com.squareup.moshi.Json

import com.gts.trackmypath.data.database.PhotoEntity
import com.gts.trackmypath.domain.model.Photo

data class ResponseEntity(
    @Json(name = "photos")
    val photosResponseEntity: PhotosResponseEntity
)

data class PhotosResponseEntity(
    @Json(name = "page")
    val page: Int,
    @Json(name = "pages")
    val pages: Int,
    @Json(name = "perpage")
    val perpage: String,
    @Json(name = "total")
    val total: String,
    @Json(name = "photo")
    val list: List<PhotoResponseEntity>
)

data class PhotoResponseEntity(
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
fun PhotoResponseEntity.toPhotoEntity() = PhotoEntity(
    photoId = 0,
    id = id,
    secret = secret,
    server = server,
    farm = farm
)

fun PhotoResponseEntity.toDomainModel() = Photo(
    id = id,
    secret = secret,
    server = server,
    farm = farm
)
