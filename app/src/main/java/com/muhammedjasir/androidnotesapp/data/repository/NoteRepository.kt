package com.muhammedjasir.androidnotesapp.data.repository

import com.muhammedjasir.androidnotesapp.data.local.dao.NoteDao
import com.muhammedjasir.androidnotesapp.data.local.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesDao: NoteDao) {

    val notes = notesDao.getAllNotes()

    suspend fun getNote(id: Int) = withContext(Dispatchers.IO){
        notesDao.getSpecificNote(id)
    }

    suspend fun insertNote(note: Note) = withContext(Dispatchers.IO) {
        notesDao.insertNote(note)
    }

    suspend fun deleteNode(note: Note) = withContext(Dispatchers.IO) {
        notesDao.deleteNote(note)
    }

    suspend fun deleteNoteById(id: Int) = withContext(Dispatchers.IO) {
        notesDao.deleteSpecificNote(id)
    }

    suspend fun updateNotes(note: Note) = withContext(Dispatchers.IO) {
        notesDao.updateNote(note)
    }

}