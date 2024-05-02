package com.muhammedjasir.androidnotesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muhammedjasir.androidnotesapp.data.local.dao.NoteDao
import com.muhammedjasir.androidnotesapp.data.local.model.Note

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao
}