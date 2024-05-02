package com.muhammedjasir.androidnotesapp.presentation.create_notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedjasir.androidnotesapp.data.local.model.Note
import com.muhammedjasir.androidnotesapp.data.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreateNotesViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {

    val noteId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val note = noteId.flatMapLatest {
        val note = it?.let { noteRepository.getNote(it) }
        flowOf(note)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setNoteId(id: Int) = viewModelScope.launch {
        noteId.emit(id)
    }

    suspend fun updateNote(note: Note) = noteRepository.updateNotes(note)

    suspend fun saveNote(note: Note) = noteRepository.insertNote(note)

    suspend fun deleteNote() = noteId.value?.let { noteRepository.deleteNoteById(it) }
}