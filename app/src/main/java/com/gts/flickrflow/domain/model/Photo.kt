package com.gts.flickrflow.domain.model

import android.os.Parcelable
import com.gts.flickrflow.data.database.PhotoEntity
import com.gts.flickrflow.data.model.PhotoResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val secret: String,
    val server: String,
    val farm: String
): Parcelable

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