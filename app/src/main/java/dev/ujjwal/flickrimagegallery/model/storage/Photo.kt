package dev.ujjwal.flickrimagegallery.model.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
class Photo(
    @field:PrimaryKey
    val id: String,

    val title: String?,

    val url_s: String?,

    val width_s: String?,

    val height_s: String?
)