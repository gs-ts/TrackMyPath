package com.gts.flickrflow.domain.model

import com.gts.flickrflow.data.database.PhotoEntity
import com.gts.flickrflow.data.model.PhotoResponse

data class Photo(
    val id: String,
    val secret: String,
    val server: String,
    val farm: String
)

fun PhotoResponse.toPhoto() = Photo(
    id = id,
    secret = secret,
    server = server,
    farm = farm
)

fun PhotoEntity.toPhoto() = Photo(
    id = id,
    secret = secret,
    server = server,
    farm = farm
)