package com.viterbi.shared.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for storing notes on Android.
 * 
 * This is the main database class that Room uses to manage the SQLite database.
 * It defines the database schema and provides access to DAOs.
 */
@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    
    /**
     * Get the DAO for note operations
     */
    abstract fun noteDao(): NoteDao
    
    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null
        
        /**
         * Get the database instance (singleton pattern)
         */
        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "viterbi_notes.db"
                )
                .fallbackToDestructiveMigration() // For simplicity, recreate on schema changes
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 