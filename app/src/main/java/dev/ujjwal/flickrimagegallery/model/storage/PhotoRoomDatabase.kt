package dev.ujjwal.flickrimagegallery.model.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Photo::class], version = 1)
abstract class PhotoRoomDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {
        private var photoRoomDatabase: PhotoRoomDatabase? = null

        internal fun getDatabase(context: Context): PhotoRoomDatabase? {
            if (photoRoomDatabase == null) {
                synchronized(PhotoRoomDatabase::class.java) {
                    if (photoRoomDatabase == null) {
                        photoRoomDatabase = Room.databaseBuilder(context.applicationContext, PhotoRoomDatabase::class.java, "photo_db").build()
                    }
                }
            }
            return photoRoomDatabase
        }
    }
}