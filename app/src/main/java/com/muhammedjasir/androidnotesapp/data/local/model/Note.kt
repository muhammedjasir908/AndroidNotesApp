package com.muhammedjasir.androidnotesapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "note_title")
    var noteTitle: String?= null,
    @ColumnInfo(name = "date_time")
    var dateTime: String?= null,
    @ColumnInfo(name = "note_subtitle")
    var noteSubtitle: String?=null,
    @ColumnInfo(name = "note_text")
    var noteText: String?=null,
    @ColumnInfo(name = "image_path")
    var imagePath: String?=null,
    @ColumnInfo(name = "audio_path")
    var audioPath: String?=null,
    @ColumnInfo(name = "file_path")
    var filePath: String?=null,
    @ColumnInfo(name = "color")
    var color: String?=null,
    @ColumnInfo(name = "note_link")
    var noteLink: String?=null
): Serializable{
    override fun toString(): String {
        return "$noteTitle : $dateTime"
    }
}
