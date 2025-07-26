package com.viterbi.shared

import android.content.Context
import com.viterbi.shared.data.NoteDatabase
import com.viterbi.shared.data.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

/**
 * Android-specific implementation of NoteRepository using Room database.
 * 
 * This class implements the shared NoteRepository interface using Android's
 * Room database for persistent storage. It demonstrates how platform-specific
 * code can implement shared interfaces.
 */
class AndroidNoteRepository(
    private val context: Context
) : NoteRepository {
    
    private val database = NoteDatabase.getDatabase(context)
    private val noteDao = database.noteDao()
    
    override fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes().map { entities ->
            entities.map { it.toNote() }
        }
    }
    
    override suspend fun getNoteById(id: String): Note? {
        return withContext(Dispatchers.IO) {
            noteDao.getNoteById(id)?.toNote()
        }
    }
    
    override suspend fun saveNote(note: Note) {
        withContext(Dispatchers.IO) {
            val entity = NoteEntity.fromNote(note)
            noteDao.insertOrUpdateNote(entity)
        }
    }
    
    override suspend fun deleteNote(id: String) {
        withContext(Dispatchers.IO) {
            noteDao.deleteNote(id)
        }
    }
    
    override suspend fun toggleFavorite(id: String) {
        withContext(Dispatchers.IO) {
            val note = noteDao.getNoteById(id)
            note?.let {
                val newFavoriteStatus = !it.isFavorite
                val updatedAt = Clock.System.now().toEpochMilliseconds()
                noteDao.updateFavoriteStatus(id, newFavoriteStatus, updatedAt)
            }
        }
    }
    
    override suspend fun searchNotes(query: String): List<Note> {
        return withContext(Dispatchers.IO) {
            noteDao.searchNotes(query).map { it.toNote() }
        }
    }
} 