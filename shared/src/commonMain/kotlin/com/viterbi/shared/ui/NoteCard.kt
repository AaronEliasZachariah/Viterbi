package com.viterbi.shared.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.viterbi.shared.Note
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * A card component that displays a single note.
 * 
 * This composable is shared across all platforms and provides a consistent
 * UI for displaying note information.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteCard(
    note: Note,
    onNoteClick: (Note) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onNoteClick(note) },
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with title and actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Title
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                // Action buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Favorite button
                    IconButton(
                        onClick = { onFavoriteClick(note.id) }
                    ) {
                        Icon(
                            imageVector = if (note.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (note.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (note.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Delete button
                    IconButton(
                        onClick = { onDeleteClick(note.id) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete note",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Content preview
            Text(
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Footer with timestamp
            Text(
                text = formatDate(note.updatedAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Format a timestamp for display
 */
private fun formatDate(instant: kotlinx.datetime.Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.monthNumber}/${localDateTime.dayOfMonth}/${localDateTime.year}"
} 