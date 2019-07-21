package com.gts.flickrflow.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoEntity)

    @Query("SELECT * FROM photos WHERE id = :photoId LIMIT 1")
    suspend fun selectPhotoById(photoId: String): PhotoEntity?

    @Query("DELETE FROM photos")
    suspend fun deletePhotos()
}