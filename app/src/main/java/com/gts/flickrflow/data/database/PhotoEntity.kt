package com.gts.flickrflow.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

import com.gts.flickrflow.domain.model.Photo

@Entity(tableName = "photos")
class PhotoEntity(
    @PrimaryKey(autoGenerate = true) var photoId: Int,
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "secret") var secret: String = "",
    @ColumnInfo(name = "server") var server: String = "",
    @ColumnInfo(name = "farm") var farm: String = ""
)

fun PhotoEntity.toDomainModel() = Photo(
    id = id,
    secret = secret,
    server = server,
    farm = farm
)