package com.interviewpreplab.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.interviewpreplab.core.model.MemoryScene

/**
 * Renders a MemoryScene showing memory layout with cells, addresses, and pointers.
 *
 * Colors:
 * - Blue (#4285F4): Stack memory
 * - Red (#EA4335): Heap memory
 * - Green (#34A853): Data/BSS segment
 * - Gray (#9AA0A6): Inactive/uninitialized
 */
@Composable
fun MemoryRenderer(
    scene: MemoryScene,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Legend
        Text(
            text = scene.legend,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Memory visualization
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            drawMemoryLayout(scene, textMeasurer)
        }
    }
}

private fun DrawScope.drawMemoryLayout(
    scene: MemoryScene,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    val canvasWidth = size.width
    val cellWidth = 180f
    val cellHeight = 40f
    val padding = 10f

    // Colors
    val stackColor = Color(0xFF4285F4)      // Blue
    val heapColor = Color(0xFFEA4335)       // Red
    val dataColor = Color(0xFF34A853)       // Green
    val inactiveColor = Color(0xFF9AA0A6)   // Gray
    val textColor = Color.White
    val borderColor = Color.Black

    var yOffset = 20f

    // Stack section
    drawMemorySection(
        yOffset = yOffset,
        title = "STACK (High Address)",
        color = stackColor,
        canvasWidth = canvasWidth,
        cellWidth = cellWidth,
        cellHeight = cellHeight,
        padding = padding,
        textMeasurer = textMeasurer,
        textColor = textColor,
        borderColor = borderColor
    )
    yOffset += 40f

    // Draw stack cells
    scene.cells.filter { "stack" in it.roles }.forEachIndexed { index, cell ->
        drawMemoryCell(
            xOffset = padding,
            yOffset = yOffset + index * (cellHeight + padding),
            address = cell.address,
            label = cell.label,
            value = cell.value,
            color = getColorForRoles(cell.roles, stackColor, heapColor, dataColor, inactiveColor),
            cellWidth = cellWidth,
            cellHeight = cellHeight,
            textMeasurer = textMeasurer,
            textColor = textColor,
            borderColor = borderColor
        )
    }

    yOffset += (scene.cells.filter { "stack" in it.roles }.size * (cellHeight + padding)) + 20f

    // Heap section
    drawMemorySection(
        yOffset = yOffset,
        title = "HEAP (Low Address)",
        color = heapColor,
        canvasWidth = canvasWidth,
        cellWidth = cellWidth,
        cellHeight = cellHeight,
        padding = padding,
        textMeasurer = textMeasurer,
        textColor = textColor,
        borderColor = borderColor
    )
    yOffset += 40f

    // Draw heap cells
    scene.cells.filter { "heap" in it.roles }.forEachIndexed { index, cell ->
        drawMemoryCell(
            xOffset = padding,
            yOffset = yOffset + index * (cellHeight + padding),
            address = cell.address,
            label = cell.label,
            value = cell.value,
            color = getColorForRoles(cell.roles, stackColor, heapColor, dataColor, inactiveColor),
            cellWidth = cellWidth,
            cellHeight = cellHeight,
            textMeasurer = textMeasurer,
            textColor = textColor,
            borderColor = borderColor
        )
    }
}

private fun DrawScope.drawMemorySection(
    yOffset: Float,
    title: String,
    color: Color,
    canvasWidth: Float,
    cellWidth: Float,
    cellHeight: Float,
    padding: Float,
    textMeasurer: androidx.compose.ui.text.TextMeasurer,
    textColor: Color,
    borderColor: Color
) {
    // Section header
    drawRect(
        color = color.copy(alpha = 0.3f),
        topLeft = Offset(padding, yOffset),
        size = Size(canvasWidth - padding * 2, cellHeight)
    )

    drawRect(
        color = color,
        topLeft = Offset(padding, yOffset),
        size = Size(canvasWidth - padding * 2, cellHeight),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
    )

    // Section title text
    val textLayout = textMeasurer.measure(
        text = title,
        style = TextStyle(
            fontSize = 12.sp,
            color = color
        )
    )
    drawText(
        textLayout = textLayout,
        topLeft = Offset(padding + 8f, yOffset + (cellHeight - textLayout.size.height) / 2)
    )
}

private fun DrawScope.drawMemoryCell(
    xOffset: Float,
    yOffset: Float,
    address: String,
    label: String,
    value: String,
    color: Color,
    cellWidth: Float,
    cellHeight: Float,
    textMeasurer: androidx.compose.ui.text.TextMeasurer,
    textColor: Color,
    borderColor: Color
) {
    // Cell background
    drawRect(
        color = color,
        topLeft = Offset(xOffset, yOffset),
        size = Size(cellWidth, cellHeight)
    )

    // Cell border
    drawRect(
        color = borderColor,
        topLeft = Offset(xOffset, yOffset),
        size = Size(cellWidth, cellHeight),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5f)
    )

    val textPadding = 4f
    val textStartX = xOffset + textPadding
    val textStartY = yOffset + textPadding
    val textSize = 10.sp

    // Address text
    val addressText = textMeasurer.measure(
        text = address,
        style = TextStyle(fontSize = textSize, color = textColor)
    )
    drawText(textLayout = addressText, topLeft = Offset(textStartX, textStartY))

    // Value text
    val valueText = textMeasurer.measure(
        text = value,
        style = TextStyle(fontSize = textSize, color = textColor)
    )
    drawText(
        textLayout = valueText,
        topLeft = Offset(textStartX + 70f, textStartY)
    )

    // Label text
    val labelText = textMeasurer.measure(
        text = label,
        style = TextStyle(fontSize = textSize, color = textColor)
    )
    drawText(
        textLayout = labelText,
        topLeft = Offset(textStartX, textStartY + cellHeight / 2)
    )
}

private fun getColorForRoles(
    roles: Set<String>,
    stackColor: Color,
    heapColor: Color,
    dataColor: Color,
    inactiveColor: Color
): Color = when {
    "active" in roles -> Color.Yellow
    "pointer" in roles -> Color(0xFF34A853) // Green
    "heap" in roles -> heapColor
    "data" in roles -> dataColor
    else -> stackColor
}
