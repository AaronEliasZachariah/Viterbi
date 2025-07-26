package com.viterbi.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.viterbi.shared.AndroidNoteRepository
import com.viterbi.shared.Note
import com.viterbi.shared.NoteViewModel
import com.viterbi.shared.ui.NoteDetailScreen
import com.viterbi.shared.ui.NoteListScreen

/**
 * Main Android activity for the Viterbi note-taking app.
 * 
 * This activity sets up the Compose UI and initializes our app components.
 * It demonstrates how the shared code is used in the Android platform.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize our app components with persistent storage
        val repository = AndroidNoteRepository(this)
        val viewModel = NoteViewModel(repository)
        
        setContent {
            ViterbiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Simple navigation state
                    var currentScreen by remember { mutableStateOf<Screen>(Screen.List) }
                    var selectedNote by remember { mutableStateOf<Note?>(null) }
                    
                    when (currentScreen) {
                        Screen.List -> {
                            NoteListScreen(
                                viewModel = viewModel,
                                onNoteClick = { note ->
                                    selectedNote = note
                                    currentScreen = Screen.Detail
                                },
                                onCreateNewNote = {
                                    selectedNote = null
                                    currentScreen = Screen.Detail
                                }
                            )
                        }
                        Screen.Detail -> {
                            NoteDetailScreen(
                                note = selectedNote,
                                viewModel = viewModel,
                                onBackClick = {
                                    currentScreen = Screen.List
                                    selectedNote = null
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Simple navigation enum for our app screens
 */
sealed class Screen {
    object List : Screen()
    object Detail : Screen()
}

/**
 * Theme for the Viterbi app.
 * 
 * This theme will be used across the Android app and can be customized
 * for different platforms while maintaining consistency.
 */
@Composable
fun ViterbiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        content = content
    )
} 