package com.gts.flickrflow.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

import com.gts.flickrflow.domain.model.Photo

// This class will have a mapping SQLite table in the database.
// Each entity must have at least 1 field annotated with PrimaryKey.
// Each entity must either have a no-arg constructor or a constructor whose parameters match fields (based on type and name).
@Entity(tableName = "photos")
class PhotoEntity(
    @PrimaryKey(autoGenerate = true) var photoId: Int,
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "secret") var secret: String = "",
    @ColumnInfo(name = "server") var server: String = "",
    @ColumnInfo(name = "farm") var farm: String = ""
)

// map to data class, Photo
fun PhotoEntity.toPhotoModel() = Photo(
    id = id,
    secret = secret,
    server = server,
    farm = farm
)