package com.viterbi.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing note-related UI state and business logic.
 * 
 * This class follows the MVVM (Model-View-ViewModel) pattern and will be
 * shared across all platforms. It manages the state of our note-taking app.
 */
class NoteViewModel(
    private val repository: NoteRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    
    // UI State
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote.asStateFlow()
    
    init {
        loadNotes()
    }
    
    /**
     * Load all notes from the repository
     */
    private fun loadNotes() {
        coroutineScope.launch {
            _isLoading.value = true
            try {
                repository.getAllNotes().collect { notesList ->
                    _notes.value = notesList
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Create a new note
     */
    fun createNote(title: String, content: String) {
        if (title.isBlank() && content.isBlank()) return
        
        val note = Note(
            title = title.ifBlank { "Untitled" },
            content = content
        )
        
        coroutineScope.launch {
            repository.saveNote(note)
        }
    }
    
    /**
     * Update an existing note
     */
    fun updateNote(note: Note) {
        coroutineScope.launch {
            val updatedNote = note.copy(updatedAt = kotlinx.datetime.Clock.System.now())
            repository.saveNote(updatedNote)
        }
    }
    
    /**
     * Delete a note
     */
    fun deleteNote(id: String) {
        coroutineScope.launch {
            repository.deleteNote(id)
        }
    }
    
    /**
     * Toggle favorite status of a note
     */
    fun toggleFavorite(id: String) {
        coroutineScope.launch {
            repository.toggleFavorite(id)
        }
    }
    
    /**
     * Select a note for editing
     */
    fun selectNote(note: Note?) {
        _selectedNote.value = note
    }
    
    /**
     * Search notes
     */
    fun searchNotes(query: String) {
        coroutineScope.launch {
            if (query.isBlank()) {
                loadNotes()
            } else {
                val searchResults = repository.searchNotes(query)
                _notes.value = searchResults
            }
        }
    }
} 