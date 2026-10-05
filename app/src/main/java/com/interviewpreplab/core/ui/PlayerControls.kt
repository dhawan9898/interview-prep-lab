package com.interviewpreplab.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.interviewpreplab.core.player.PlayerState

/**
 * Playback controls and scrubber for frame-by-frame playback.
 */
@Composable
fun PlayerControls(
    state: PlayerState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onStepBack: () -> Unit,
    onStepForward: () -> Unit,
    onReset: () -> Unit,
    onScrub: (Int) -> Unit,
    onSpeedChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Playback buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onReset) {
                Icon(Icons.Default.Refresh, "Reset", Modifier.size(32.dp))
            }
            IconButton(onClick = onStepBack) {
                Icon(Icons.Default.NavigateBefore, "Step back", Modifier.size(32.dp))
            }
            IconButton(
                onClick = if (state.isPlaying) onPause else onPlay
            ) {
                Icon(
                    if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    if (state.isPlaying) "Pause" else "Play",
                    Modifier.size(32.dp)
                )
            }
            IconButton(onClick = onStepForward) {
                Icon(Icons.Default.NavigateNext, "Step forward", Modifier.size(32.dp))
            }
        }

        // Scrubber
        Text(
            "Frame ${state.frameIdx + 1} of ${state.frames.size}",
            style = MaterialTheme.typography.labelSmall
        )
        if (state.frames.isNotEmpty()) {
            Slider(
                value = state.frameIdx.toFloat(),
                onValueChange = { onScrub(it.toInt()) },
                valueRange = 0f..maxOf(1f, (state.frames.size - 1).toFloat()),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Speed control
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Speed: ${String.format("%.2f", state.speed)}x", style = MaterialTheme.typography.labelSmall)
            Slider(
                value = state.speed,
                onValueChange = onSpeedChange,
                valueRange = 0.25f..4f,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
