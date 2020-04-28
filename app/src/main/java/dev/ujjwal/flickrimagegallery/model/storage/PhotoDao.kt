package dev.ujjwal.flickrimagegallery.model.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PhotoDao {

    @Insert
    fun insert(photo: Photo)

    @get:Query("SELECT * FROM photos")
    val allPhotos: LiveData<List<Photo>>

    @Query("DELETE FROM photos")
    fun deleteAll()
}