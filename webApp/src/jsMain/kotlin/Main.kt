import androidx.compose.web.renderComposable
import com.viterbi.shared.WebNoteRepository
import com.viterbi.shared.Note
import com.viterbi.shared.NoteViewModel
import com.viterbi.shared.ui.NoteDetailScreen
import com.viterbi.shared.ui.NoteListScreen
import org.jetbrains.compose.web.dom.Div
import kotlinx.browser.window
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * Main entry point for the Viterbi web app.
 * 
 * This function initializes our app and renders it in the browser.
 * It demonstrates how the same shared code works on the web platform.
 */
fun main() {
    // Initialize our app components with persistent storage (same as Android!)
    val repository = WebNoteRepository()
    val viewModel = NoteViewModel(repository)
    
    // Simple navigation state
    var currentScreen by mutableStateOf<Screen>(Screen.List)
    var selectedNote by mutableStateOf<Note?>(null)
    
    // Render our app in the browser
    renderComposable(rootElementId = "root") {
        Div {
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

/**
 * Simple navigation enum for our app screens
 */
sealed class Screen {
    object List : Screen()
    object Detail : Screen()
} 