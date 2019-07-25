package com.gts.flickrflow.domain.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

import com.gts.flickrflow.data.database.PhotoEntity
import com.gts.flickrflow.data.network.PhotoResponse

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