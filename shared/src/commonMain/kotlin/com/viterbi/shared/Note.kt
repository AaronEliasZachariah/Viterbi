package com.viterbi.shared

import kotlinx.serialization.Serializable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Represents a note in our application.
 * 
 * This is a data class that will be shared across all platforms.
 * We use @Serializable to enable JSON serialization for data persistence.
 */
@Serializable
data class Note(
    val id: String = generateId(),
    val title: String,
    val content: String,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val isFavorite: Boolean = false
) {
    companion object {
        /**
         * Generates a unique ID for new notes.
         * In a production app, you might want to use a more sophisticated ID generation.
         */
        private fun generateId(): String = 
            System.currentTimeMillis().toString() + (0..999).random().toString().padStart(3, '0')
    }
} 