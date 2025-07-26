package com.viterbi.shared

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing notes.
 * 
 * This interface defines the contract for note operations that will be
 * implemented differently on each platform (Android, Web, iOS).
 * 
 * We use Flow for reactive data streams, which is perfect for UI updates.
 */
interface NoteRepository {
    /**
     * Get all notes as a reactive stream
     */
    fun getAllNotes(): Flow<List<Note>>
    
    /**
     * Get a specific note by ID
     */
    suspend fun getNoteById(id: String): Note?
    
    /**
     * Save a note (create or update)
     */
    suspend fun saveNote(note: Note)
    
    /**
     * Delete a note by ID
     */
    suspend fun deleteNote(id: String)
    
    /**
     * Toggle the favorite status of a note
     */
    suspend fun toggleFavorite(id: String)
    
    /**
     * Search notes by title or content
     */
    suspend fun searchNotes(query: String): List<Note>
} 