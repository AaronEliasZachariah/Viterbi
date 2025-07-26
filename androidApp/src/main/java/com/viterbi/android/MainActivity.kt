package com.viterbi.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.viterbi.shared.InMemoryNoteRepository
import com.viterbi.shared.NoteViewModel
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
        
        // Initialize our app components
        val repository = InMemoryNoteRepository()
        val viewModel = NoteViewModel(repository)
        
        setContent {
            ViterbiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Our main app screen - shared across platforms!
                    NoteListScreen(
                        viewModel = viewModel,
                        onNoteClick = { note ->
                            // TODO: Navigate to note detail screen
                            // For now, we'll just show a simple toast
                            android.widget.Toast.makeText(
                                this@MainActivity,
                                "Selected: ${note.title}",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }
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