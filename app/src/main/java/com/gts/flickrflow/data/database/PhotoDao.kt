package com.gts.flickrflow.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy

// Data Access Objects (DAO) are the main classes where you define your database interactions.
// They can include a variety of query methods.
@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(photo: PhotoEntity)

    @Query("SELECT * FROM photos ORDER BY photoId DESC")
    suspend fun selectAllPhotos(): Array<PhotoEntity>

    @Query("DELETE FROM photos")
    suspend fun deletePhotos()
}