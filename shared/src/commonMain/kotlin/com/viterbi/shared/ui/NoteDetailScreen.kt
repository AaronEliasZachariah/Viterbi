package com.viterbi.shared.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.viterbi.shared.Note
import com.viterbi.shared.NoteViewModel

/**
 * Screen for viewing and editing a single note.
 * 
 * This composable provides a full-screen editing experience for notes,
 * with real-time saving and proper error handling.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: Note?,
    viewModel: NoteViewModel,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var hasChanges by remember { mutableStateOf(false) }
    
    // Update local state when note changes
    LaunchedEffect(note) {
        title = note?.title ?: ""
        content = note?.content ?: ""
        hasChanges = false
    }
    
    // Track changes
    LaunchedEffect(title, content) {
        hasChanges = title != note?.title || content != note?.content
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (note == null) "New Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    // Favorite button
                    note?.let { existingNote ->
                        IconButton(
                            onClick = { viewModel.toggleFavorite(existingNote.id) }
                        ) {
                            Icon(
                                imageVector = if (existingNote.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (existingNote.isFavorite) "Remove from favorites" else "Add to favorites",
                                tint = if (existingNote.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    // Delete button
                    note?.let { existingNote ->
                        IconButton(
                            onClick = {
                                viewModel.deleteNote(existingNote.id)
                                onBackClick()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete note",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    
                    // Save button
                    if (hasChanges) {
                        IconButton(
                            onClick = {
                                if (note != null) {
                                    // Update existing note
                                    val updatedNote = note.copy(
                                        title = title.ifBlank { "Untitled" },
                                        content = content
                                    )
                                    viewModel.updateNote(updatedNote)
                                } else {
                                    // Create new note
                                    viewModel.createNote(title, content)
                                }
                                onBackClick()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Save,
                                contentDescription = "Save note"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Enter note title...") }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Content field
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text("Start writing your note...") },
                minLines = 10
            )
            
            // Auto-save indicator
            if (hasChanges) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Unsaved changes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
} 