package com.viterbi.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory implementation of NoteRepository for testing and development.
 * 
 * This is a simple implementation that stores notes in memory.
 * In a real app, you'd have different implementations for each platform:
 * - Android: Room database
 * - Web: LocalStorage or IndexedDB
 * - iOS: Core Data
 */
class InMemoryNoteRepository : NoteRepository {
    
    private val notesFlow = MutableStateFlow<List<Note>>(emptyList())
    private val notes = mutableListOf<Note>()
    
    init {
        // Add some sample notes for testing
        addSampleNotes()
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
        
        // Update the flow
        notesFlow.value = notes.toList()
    }
    
    override suspend fun deleteNote(id: String) {
        notes.removeAll { it.id == id }
        notesFlow.value = notes.toList()
    }
    
    override suspend fun toggleFavorite(id: String) {
        val noteIndex = notes.indexOfFirst { it.id == id }
        if (noteIndex >= 0) {
            val note = notes[noteIndex]
            val updatedNote = note.copy(
                isFavorite = !note.isFavorite,
                updatedAt = kotlinx.datetime.Clock.System.now()
            )
            notes[noteIndex] = updatedNote
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
        notesFlow.value = notes.toList()
    }
} 