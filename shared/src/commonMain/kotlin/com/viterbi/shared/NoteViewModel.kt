package com.viterbi.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        loadNotes()
    }
    
    /**
     * Load all notes from the repository
     */
    private fun loadNotes() {
        coroutineScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.getAllNotes().collect { notesList ->
                    _notes.value = notesList
                }
            } catch (e: Exception) {
                _error.value = "Failed to load notes: ${e.message}"
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
            try {
                withContext(Dispatchers.IO) {
                    repository.saveNote(note)
                }
            } catch (e: Exception) {
                _error.value = "Failed to create note: ${e.message}"
            }
        }
    }
    
    /**
     * Update an existing note
     */
    fun updateNote(note: Note) {
        coroutineScope.launch {
            try {
                val updatedNote = note.copy(updatedAt = kotlinx.datetime.Clock.System.now())
                withContext(Dispatchers.IO) {
                    repository.saveNote(updatedNote)
                }
            } catch (e: Exception) {
                _error.value = "Failed to update note: ${e.message}"
            }
        }
    }
    
    /**
     * Delete a note
     */
    fun deleteNote(id: String) {
        coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.deleteNote(id)
                }
            } catch (e: Exception) {
                _error.value = "Failed to delete note: ${e.message}"
            }
        }
    }
    
    /**
     * Toggle favorite status of a note
     */
    fun toggleFavorite(id: String) {
        coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repository.toggleFavorite(id)
                }
            } catch (e: Exception) {
                _error.value = "Failed to toggle favorite: ${e.message}"
            }
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
            try {
                if (query.isBlank()) {
                    loadNotes()
                } else {
                    val searchResults = withContext(Dispatchers.IO) {
                        repository.searchNotes(query)
                    }
                    _notes.value = searchResults
                }
            } catch (e: Exception) {
                _error.value = "Failed to search notes: ${e.message}"
            }
        }
    }
    
    /**
     * Clear error state
     */
    fun clearError() {
        _error.value = null
    }
} 