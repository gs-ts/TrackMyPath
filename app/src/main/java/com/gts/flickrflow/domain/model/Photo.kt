package com.gts.flickrflow.domain.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val secret: String,
    val server: String,
    val farm: String
): Parcelable