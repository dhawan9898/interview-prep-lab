package com.interviewpreplab.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
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
import com.interviewpreplab.core.model.ListScene
import kotlin.math.sqrt

/**
 * Renders a ListScene (linked list visualization).
 * Draws nodes as boxes with arrows connecting them.
 */
@Composable
fun ListRenderer(
    scene: ListScene,
    modifier: Modifier = Modifier
) {
    val nodes = scene.nodes
    if (nodes.isEmpty()) {
        return
    }

    val paper = MaterialTheme.colorScheme.surface
    val ink = MaterialTheme.colorScheme.onSurface
    val stamp = Color(0xFFDD4B39)    // red: active
    val plot = Color(0xFF4285F4)     // blue: pointer
    val seal = Color(0xFF0F9D58)     // green: done
    val arrowColor = ink.copy(alpha = 0.6f)

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .background(paper)
            .padding(12.dp)
            .size(600.dp, 200.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val nodeWidth = 60.dp.toPx()
            val nodeHeight = 50.dp.toPx()
            val spacing = 100.dp.toPx()
            val baseY = size.height / 2

            // Draw nodes left to right
            for ((idx, node) in nodes.withIndex()) {
                val x = 20.dp.toPx() + idx * spacing
                val y = baseY - nodeHeight / 2

                // Node box
                val nodeColor = when {
                    "landed" in node.roles -> stamp.copy(alpha = 0.7f)
                    "head" in node.roles -> plot
                    "found" in node.roles -> seal
                    else -> ink.copy(alpha = 0.3f)
                }

                drawRect(
                    color = nodeColor,
                    topLeft = Offset(x, y),
                    size = Size(nodeWidth, nodeHeight),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                )

                // Value text
                val valueStyle = TextStyle(fontSize = 16.sp, color = ink)
                val valueText = textMeasurer.measure(node.value.toString(), valueStyle)
                drawText(
                    valueText,
                    topLeft = Offset(
                        x + (nodeWidth / 2) - (valueText.size.width / 2),
                        y + (nodeHeight / 2) - (valueText.size.height / 2)
                    )
                )

                // Arrow to next node
                if (node.next != null && idx < nodes.size - 1) {
                    val nextX = 20.dp.toPx() + (idx + 1) * spacing
                    drawArrow(
                        from = Offset(x + nodeWidth, baseY),
                        to = Offset(nextX, baseY),
                        color = arrowColor
                    )
                }
            }

            // Draw HEAD pointer if exists
            scene.head?.let { headIdx ->
                if (headIdx in nodes.indices) {
                    val x = 20.dp.toPx() + headIdx * spacing + nodeWidth / 2
                    val y = baseY - nodeHeight / 2 - 30.dp.toPx()

                    val pointerStyle = TextStyle(fontSize = 12.sp, color = plot)
                    val pointerText = textMeasurer.measure("HEAD", pointerStyle)
                    drawText(pointerText, topLeft = Offset(x - pointerText.size.width / 2, y))

                    // Arrow from label to node
                    drawArrow(
                        from = Offset(x, y + 15.dp.toPx()),
                        to = Offset(x, baseY - nodeHeight / 2),
                        color = plot
                    )
                }
            }

            // Draw custom pointers
            scene.pointers.forEach { (label, idx) ->
                if (idx in nodes.indices) {
                    val x = 20.dp.toPx() + idx * spacing + nodeWidth / 2
                    val y = baseY + nodeHeight / 2 + 30.dp.toPx()

                    val pointerStyle = TextStyle(fontSize = 11.sp, color = plot)
                    val pointerText = textMeasurer.measure(label, pointerStyle)
                    drawText(pointerText, topLeft = Offset(x - pointerText.size.width / 2, y))

                    // Arrow from label to node
                    drawArrow(
                        from = Offset(x, y - 5.dp.toPx()),
                        to = Offset(x, baseY + nodeHeight / 2),
                        color = plot
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawArrow(
    from: Offset,
    to: Offset,
    color: Color,
    arrowHeadSize: Float = 12f
) {
    val dx = to.x - from.x
    val dy = to.y - from.y
    val distance = sqrt(dx * dx + dy * dy)

    if (distance == 0f) return

    val ux = dx / distance
    val uy = dy / distance

    // Draw line
    drawLine(
        color = color,
        start = from,
        end = to,
        strokeWidth = 2f
    )

    // Draw arrowhead
    val arrowPoint = to
    val perpX = -uy
    val perpY = ux

    val p1 = Offset(
        arrowPoint.x - ux * arrowHeadSize - perpX * arrowHeadSize / 2,
        arrowPoint.y - uy * arrowHeadSize - perpY * arrowHeadSize / 2
    )
    val p2 = Offset(
        arrowPoint.x - ux * arrowHeadSize + perpX * arrowHeadSize / 2,
        arrowPoint.y - uy * arrowHeadSize + perpY * arrowHeadSize / 2
    )

    // Draw arrowhead triangle
    val path = androidx.compose.ui.graphics.Path()
    path.moveTo(arrowPoint.x, arrowPoint.y)
    path.lineTo(p1.x, p1.y)
    path.lineTo(p2.x, p2.y)
    path.close()
    drawPath(path, color = color)
}
