package com.gts.flickrflow.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.gts.flickrflow.data.model.PhotoResponse

@Entity(tableName = "photos")
class PhotoEntity(
    @PrimaryKey var id: String = "",
    @ColumnInfo(name = "secret") var secret: String = "",
    @ColumnInfo(name = "server") var server: String = "",
    @ColumnInfo(name = "farm") var farm: String = ""
)