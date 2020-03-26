package com.gts.trackmypath.presentation.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

import com.gts.trackmypath.domain.model.Photo

@Parcelize
data class PhotoViewItem(
    val id: String,
    val secret: String,
    val server: String,
    val farm: String
): Parcelable

fun Photo.toPresentationModel() = PhotoViewItem(
    id = id,
    secret = secret,
    server = server,
    farm = farm
)
