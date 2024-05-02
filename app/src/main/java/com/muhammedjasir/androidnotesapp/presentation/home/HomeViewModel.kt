package com.muhammedjasir.androidnotesapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammedjasir.androidnotesapp.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel() {

    private val searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val notes = searchQuery.flatMapLatest { query ->
        noteRepository.notes.map { it -> it.filter { it.noteTitle?.contains(query, ignoreCase = true) == true } }
    }.stateIn(viewModelScope,SharingStarted.Eagerly, emptyList())

    fun onSearchQueryChanged(query: String) = viewModelScope.launch {
        searchQuery.emit(query)
    }
}