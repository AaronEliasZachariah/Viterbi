import androidx.compose.web.renderComposable
import com.viterbi.shared.InMemoryNoteRepository
import com.viterbi.shared.NoteViewModel
import com.viterbi.shared.ui.NoteListScreen
import org.jetbrains.compose.web.dom.Div

/**
 * Main entry point for the Viterbi web app.
 * 
 * This function initializes our app and renders it in the browser.
 * It demonstrates how the same shared code works on the web platform.
 */
fun main() {
    // Initialize our app components (same as Android!)
    val repository = InMemoryNoteRepository()
    val viewModel = NoteViewModel(repository)
    
    // Render our app in the browser
    renderComposable(rootElementId = "root") {
        Div {
            // Our main app screen - shared across platforms!
            NoteListScreen(
                viewModel = viewModel,
                onNoteClick = { note ->
                    // TODO: Navigate to note detail screen
                    // For now, we'll just show a simple alert
                    window.alert("Selected: ${note.title}")
                }
            )
        }
    }
} 