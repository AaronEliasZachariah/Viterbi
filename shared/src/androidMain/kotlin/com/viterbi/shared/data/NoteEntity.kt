package com.viterbi.shared.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.viterbi.shared.Note
import kotlinx.datetime.Instant

/**
 * Room entity for storing notes in the Android database.
 * 
 * This class maps our shared Note data model to the SQLite database.
 * Room annotations define how the data is stored and retrieved.
 */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long, // Stored as timestamp
    val updatedAt: Long, // Stored as timestamp
    val isFavorite: Boolean
) {
    /**
     * Convert to shared Note model
     */
    fun toNote(): Note = Note(
        id = id,
        title = title,
        content = content,
        createdAt = Instant.fromEpochMilliseconds(createdAt),
        updatedAt = Instant.fromEpochMilliseconds(updatedAt),
        isFavorite = isFavorite
    )
    
    companion object {
        /**
         * Convert from shared Note model
         */
        fun fromNote(note: Note): NoteEntity = NoteEntity(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAt = note.createdAt.toEpochMilliseconds(),
            updatedAt = note.updatedAt.toEpochMilliseconds(),
            isFavorite = note.isFavorite
        )
    }
} 