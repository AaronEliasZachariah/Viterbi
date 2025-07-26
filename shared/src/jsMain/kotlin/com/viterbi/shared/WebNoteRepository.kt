package com.viterbi.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Web-specific implementation of NoteRepository using browser LocalStorage.
 * 
 * This class implements the shared NoteRepository interface using the browser's
 * LocalStorage API for persistent storage. It demonstrates how platform-specific
 * code can implement shared interfaces.
 */
class WebNoteRepository : NoteRepository {
    
    private val storageKey = "viterbi_notes"
    private val notesFlow = MutableStateFlow<List<Note>>(emptyList())
    private val notes = mutableListOf<Note>()
    
    init {
        loadNotesFromStorage()
    }
    
    override fun getAllNotes(): Flow<List<Note>> = notesFlow.asStateFlow()
    
    override suspend fun getNoteById(id: String): Note? {
        return notes.find { it.id == id }
    }
    
    override suspend fun saveNote(note: Note) {
        val existingIndex = notes.indexOfFirst { it.id == note.id }
        
        if (existingIndex >= 0) {
            // Update existing note
            notes[existingIndex] = note
        } else {
            // Add new note
            notes.add(note)
        }
        
        updateStorage()
        notesFlow.value = notes.toList()
    }
    
    override suspend fun deleteNote(id: String) {
        notes.removeAll { it.id == id }
        updateStorage()
        notesFlow.value = notes.toList()
    }
    
    override suspend fun toggleFavorite(id: String) {
        val noteIndex = notes.indexOfFirst { it.id == id }
        if (noteIndex >= 0) {
            val note = notes[noteIndex]
            val updatedNote = note.copy(
                isFavorite = !note.isFavorite,
                updatedAt = Clock.System.now()
            )
            notes[noteIndex] = updatedNote
            updateStorage()
            notesFlow.value = notes.toList()
        }
    }
    
    override suspend fun searchNotes(query: String): List<Note> {
        if (query.isBlank()) return notes.toList()
        
        return notes.filter { note ->
            note.title.contains(query, ignoreCase = true) ||
            note.content.contains(query, ignoreCase = true)
        }
    }
    
    /**
     * Load notes from browser LocalStorage
     */
    private fun loadNotesFromStorage() {
        try {
            val storedData = getLocalStorage().getItem(storageKey)
            if (storedData != null) {
                val storedNotes = Json.decodeFromString<List<Note>>(storedData)
                notes.clear()
                notes.addAll(storedNotes)
                notesFlow.value = notes.toList()
            } else {
                // Add sample notes for first-time users
                addSampleNotes()
            }
        } catch (e: Exception) {
            console.error("Error loading notes from storage:", e)
            addSampleNotes()
        }
    }
    
    /**
     * Save notes to browser LocalStorage
     */
    private fun updateStorage() {
        try {
            val jsonData = Json.encodeToString(notes)
            getLocalStorage().setItem(storageKey, jsonData)
        } catch (e: Exception) {
            console.error("Error saving notes to storage:", e)
        }
    }
    
    /**
     * Add sample notes for new users
     */
    private fun addSampleNotes() {
        val sampleNotes = listOf(
            Note(
                id = "1",
                title = "Welcome to Viterbi",
                content = "This is your first note! Start writing your thoughts, ideas, and important information here.\n\nYou can:\n• Create new notes\n• Edit existing notes\n• Mark notes as favorites\n• Search through your notes\n\nHappy note-taking! 📝",
                isFavorite = true
            ),
            Note(
                id = "2",
                title = "Shopping List",
                content = "• Milk\n• Bread\n• Eggs\n• Bananas\n• Coffee",
                isFavorite = false
            ),
            Note(
                id = "3",
                title = "Meeting Notes",
                content = "Team meeting - 2:00 PM\n\nAgenda:\n1. Project updates\n2. Q1 goals review\n3. New feature planning\n\nAction items:\n- Schedule follow-up meeting\n- Prepare presentation slides",
                isFavorite = true
            )
        )
        
        notes.addAll(sampleNotes)
        updateStorage()
        notesFlow.value = notes.toList()
    }
    
    /**
     * Get browser LocalStorage
     */
    private fun getLocalStorage(): dynamic {
        return js("window.localStorage")
    }
} 