package com.interviewpreplab.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.interviewpreplab.core.model.BarsScene

/**
 * Renders a BarsScene (sorting visualization) on Canvas.
 * Bars scale to fill available width; height represents value.
 */
@Composable
fun BarsRenderer(
    scene: BarsScene,
    maxValue: Int,
    modifier: Modifier = Modifier
) {
    val bars = scene.bars
    if (bars.isEmpty()) return

    val paper = MaterialTheme.colorScheme.surface
    val ink = MaterialTheme.colorScheme.onSurface
    val stamp = Color(0xFFDD4B39)    // red: active/compare
    val plot = Color(0xFF4285F4)     // blue: pointer B
    val seal = Color(0xFF0F9D58)     // green: done/sorted
    val dim = Color.Gray.copy(alpha = 0.3f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(paper)
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxWidth()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val barWidth = canvasWidth / bars.size
            val scale = canvasHeight / (maxValue.toFloat() * 1.1f)

            for ((idx, bar) in bars.withIndex()) {
                val barHeight = bar.value * scale
                val x = idx * barWidth
                val y = canvasHeight - barHeight

                val color = when {
                    "sorted" in bar.roles -> seal
                    "compare" in bar.roles -> stamp
                    "pivot" in bar.roles -> plot
                    "dim" in bar.roles -> dim
                    else -> ink
                }

                drawRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(barWidth - 2.dp.toPx(), barHeight)
                )
            }
        }
    }
}
