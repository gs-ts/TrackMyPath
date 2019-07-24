package com.gts.flickrflow.core

fun buildUri(farm: String, server: String, id: String, secret: String): String {
    return ("https://farm$farm.staticflickr.com/$server/${id}_$secret.jpg")
}