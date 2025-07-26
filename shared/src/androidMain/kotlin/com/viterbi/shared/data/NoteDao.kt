package com.viterbi.shared.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for note operations in the Android Room database.
 * 
 * This interface defines all the database operations we can perform on notes.
 * Room will automatically generate the implementation of these methods.
 */
@Dao
interface NoteDao {
    
    /**
     * Get all notes as a reactive stream, ordered by most recent first
     */
    @Query("SELECT * FROM notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>
    
    /**
     * Get a specific note by ID
     */
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: String): NoteEntity?
    
    /**
     * Insert or update a note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateNote(note: NoteEntity)
    
    /**
     * Delete a note by ID
     */
    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id: String)
    
    /**
     * Update the favorite status of a note
     */
    @Query("UPDATE notes SET isFavorite = :isFavorite, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean, updatedAt: Long)
    
    /**
     * Search notes by title or content
     */
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    suspend fun searchNotes(query: String): List<NoteEntity>
    
    /**
     * Get favorite notes only
     */
    @Query("SELECT * FROM notes WHERE isFavorite = 1 ORDER BY updatedAt DESC")
    fun getFavoriteNotes(): Flow<List<NoteEntity>>
    
    /**
     * Get the total count of notes
     */
    @Query("SELECT COUNT(*) FROM notes")
    suspend fun getNoteCount(): Int
} 