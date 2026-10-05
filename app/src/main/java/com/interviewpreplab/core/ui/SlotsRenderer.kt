package com.interviewpreplab.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.interviewpreplab.core.model.Slot
import com.interviewpreplab.core.model.SlotsScene

/**
 * Renders a SlotsScene (fixed-slot structures like stack, queue, deque).
 * Draws a vertical (dir="v") or horizontal (dir="h") row of slots with pointer badges.
 */
@Composable
fun SlotsRenderer(
    scene: SlotsScene,
    direction: String = "h", // "h" for horizontal (queue), "v" for vertical (stack)
    modifier: Modifier = Modifier
) {
    val slots = scene.slots
    if (slots.isEmpty()) return

    val paper = MaterialTheme.colorScheme.surface
    val ink = MaterialTheme.colorScheme.onSurface
    val stamp = Color(0xFFDD4B39)    // red: active/compare
    val plot = Color(0xFF4285F4)     // blue: pointer
    val seal = Color(0xFF0F9D58)     // green: done

    val textMeasurer = rememberTextMeasurer()

    Column(modifier = modifier.background(paper).padding(12.dp)) {
        Canvas(modifier = Modifier.size(400.dp)) {
            val slotSize = 50.dp.toPx()
            val slotGap = 8.dp.toPx()
            val pointerHeight = 30.dp.toPx()

            if (direction == "v") {
                // Vertical stack (index 0 at bottom)
                for ((idx, slot) in slots.withIndex()) {
                    val y = size.height - (idx + 1) * (slotSize + slotGap)
                    val x = (size.width - slotSize) / 2

                    // Draw slot box
                    val color = when {
                        "landed" in slot.roles -> stamp.copy(alpha = 0.7f)
                        "selected" in slot.roles -> plot
                        slot.value == null -> Color.Gray.copy(alpha = 0.2f)
                        else -> ink
                    }

                    drawRect(
                        color = color,
                        topLeft = Offset(x, y),
                        size = Size(slotSize, slotSize),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                    )

                    // Draw value
                    if (slot.value != null) {
                        val textStyle = TextStyle(fontSize = 18.sp, color = ink)
                        val textLayoutResult = textMeasurer.measure(
                            slot.value.toString(),
                            textStyle
                        )
                        drawText(
                            textLayoutResult,
                            topLeft = Offset(
                                x + (slotSize - textLayoutResult.size.width) / 2,
                                y + (slotSize - textLayoutResult.size.height) / 2
                            )
                        )
                    }

                    // Draw index label
                    val indexStyle = TextStyle(fontSize = 12.sp, color = Color.Gray)
                    val indexText = textMeasurer.measure(idx.toString(), indexStyle)
                    drawText(
                        indexText,
                        topLeft = Offset(x + slotSize + 4.dp.toPx(), y + 8.dp.toPx())
                    )
                }

                // Draw pointers (TOP, etc.)
                scene.pointers.forEach { (label, idx) ->
                    if (idx in slots.indices) {
                        val y = size.height - (idx + 1) * (slotSize + slotGap)
                        val x = (size.width - slotSize) / 2 - 50.dp.toPx()

                        // Pointer label
                        val pointerStyle = TextStyle(fontSize = 12.sp, color = plot, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        val pointerText = textMeasurer.measure(label, pointerStyle)
                        drawText(pointerText, topLeft = Offset(x, y + 15.dp.toPx()))
                    }
                }
            } else {
                // Horizontal queue (index 0 at left)
                for ((idx, slot) in slots.withIndex()) {
                    val x = idx * (slotSize + slotGap)
                    val y = (size.height - slotSize) / 2

                    val color = when {
                        "landed" in slot.roles -> stamp.copy(alpha = 0.7f)
                        "selected" in slot.roles -> plot
                        slot.value == null -> Color.Gray.copy(alpha = 0.2f)
                        else -> ink
                    }

                    drawRect(
                        color = color,
                        topLeft = Offset(x, y),
                        size = Size(slotSize, slotSize),
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                    )

                    if (slot.value != null) {
                        val textStyle = TextStyle(fontSize = 18.sp, color = ink)
                        val textLayoutResult = textMeasurer.measure(
                            slot.value.toString(),
                            textStyle
                        )
                        drawText(
                            textLayoutResult,
                            topLeft = Offset(
                                x + (slotSize - textLayoutResult.size.width) / 2,
                                y + (slotSize - textLayoutResult.size.height) / 2
                            )
                        )
                    }
                }

                // Draw pointers
                scene.pointers.forEach { (label, idx) ->
                    if (idx in slots.indices) {
                        val x = idx * (slotSize + slotGap)
                        val y = (size.height - slotSize) / 2 - 35.dp.toPx()
                        val pointerStyle = TextStyle(fontSize = 12.sp, color = plot, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        val pointerText = textMeasurer.measure(label, pointerStyle)
                        drawText(pointerText, topLeft = Offset(x + 5.dp.toPx(), y))
                    }
                }
            }
        }
    }
}
