package com.gts.flickrflow.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: PhotoEntity)

    @Query("SELECT * FROM photos ORDER BY photoId DESC")
    fun loadAllPhotos(): Array<PhotoEntity>

    @Query("DELETE FROM photos")
    suspend fun deletePhotos()
}