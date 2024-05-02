package com.muhammedjasir.androidnotesapp.di

import android.content.Context
import androidx.room.Room
import com.muhammedjasir.androidnotesapp.data.local.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(context,NoteDatabase::class.java,"notes.db").build()

    @Provides
    fun providesNotesDao(database: NoteDatabase) = database.noteDao()
}