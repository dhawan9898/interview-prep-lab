package com.interviewpreplab.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.ui.theme.MonospaceFamily

private fun label(phase: String) = phase.replace('_', ' ').uppercase()

/** Draws the lesson's step path and the live stats for frames that carry no drawable scene. */
@Composable
fun ConceptRenderer(frames: List<Frame>, index: Int, modifier: Modifier = Modifier) {
    val current = frames.getOrNull(index) ?: return
    val phases = frames.map { it.phase }.distinct()
    val activeIdx = phases.indexOf(current.phase)
    val chipScroll = rememberScrollState()

    Column(
        modifier = modifier.fillMaxSize().padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "STEP ${activeIdx + 1} OF ${phases.size}",
            style = MaterialTheme.typography.labelSmall,
            fontFamily = MonospaceFamily,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(Modifier.horizontalScroll(chipScroll), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            phases.forEachIndexed { i, phase ->
                val active = i == activeIdx
                val done = i < activeIdx
                Text(
                    label(phase),
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = MonospaceFamily,
                    color = when {
                        active -> MaterialTheme.colorScheme.onPrimary
                        done -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier
                        .background(
                            when {
                                active -> MaterialTheme.colorScheme.primary
                                done -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
        Text(
            label(current.phase),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            current.stats.entries.chunked(2).forEach { pair ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pair.forEach { (k, v) ->
                        Column(
                            Modifier
                                .weight(1f)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Text(
                                k.replace('_', ' ').uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                v,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = MonospaceFamily,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    if (pair.size == 1) Column(Modifier.weight(1f)) {}
                }
            }
        }
    }
}
