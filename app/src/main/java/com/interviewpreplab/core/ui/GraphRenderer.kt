package com.interviewpreplab.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.interviewpreplab.core.model.GraphScene
import kotlin.math.cos
import kotlin.math.sin

/**
 * Renders a GraphScene (graph algorithms: BFS, DFS, Dijkstra, MST).
 * Nodes arranged in a circle, edges connect them with optional weights/arrows.
 */
@Composable
fun GraphRenderer(
    scene: GraphScene,
    modifier: Modifier = Modifier
) {
    val nodes = scene.nodes
    val edges = scene.edges

    if (nodes.isEmpty()) return

    val paper = MaterialTheme.colorScheme.surface
    val ink = MaterialTheme.colorScheme.onSurface
    val stamp = Color(0xFFDD4B39)    // red: active
    val plot = Color(0xFF4285F4)     // blue: frontier
    val seal = Color(0xFF0F9D58)     // green: done

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .background(paper)
            .padding(12.dp)
            .size(500.dp, 500.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = minOf(size.width, size.height) / 2 - 50.dp.toPx()
            val nodeRadius = 30.dp.toPx()

            // Compute positions: nodes in a circle
            val positions = mutableMapOf<String, Offset>()
            for ((idx, node) in nodes.withIndex()) {
                val angle = (2 * Math.PI * idx) / nodes.size
                val x = centerX + radius * cos(angle)
                val y = centerY + radius * sin(angle)
                positions[node.id] = Offset(x.toFloat(), y.toFloat())
            }

            // Draw edges first (so they're behind nodes)
            for (edge in edges) {
                val fromPos = positions[edge.from] ?: continue
                val toPos = positions[edge.to] ?: continue

                // Shorten edge to stop at node boundary
                val dx = toPos.x - fromPos.x
                val dy = toPos.y - fromPos.y
                val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                if (dist == 0f) continue

                val scale = (dist - nodeRadius) / dist
                val endX = fromPos.x + dx * scale
                val endY = fromPos.y + dy * scale

                val edgeColor = when {
                    "active" in edge.roles -> stamp
                    "visited" in edge.roles -> seal
                    else -> ink.copy(alpha = 0.3f)
                }

                drawLine(
                    color = edgeColor,
                    start = Offset(fromPos.x + dx * (nodeRadius / dist), fromPos.y + dy * (nodeRadius / dist)),
                    end = Offset(endX, endY),
                    strokeWidth = 2f
                )

                // Draw weight label if exists
                if (edge.weight != null) {
                    val midX = (fromPos.x + toPos.x) / 2
                    val midY = (fromPos.y + toPos.y) / 2
                    val weightStyle = TextStyle(fontSize = 10.sp, color = ink)
                    val weightText = textMeasurer.measure(edge.weight.toString(), weightStyle)
                    drawText(
                        weightText,
                        topLeft = Offset(midX - weightText.size.width / 2, midY - weightText.size.height / 2)
                    )
                }
            }

            // Draw nodes
            for (node in nodes) {
                val pos = positions[node.id] ?: continue

                val nodeColor = when {
                    "active" in node.roles -> stamp
                    "frontier" in node.roles -> plot
                    "done" in node.roles -> seal
                    else -> ink.copy(alpha = 0.2f)
                }

                // Draw circle
                drawCircle(
                    color = nodeColor,
                    radius = nodeRadius,
                    center = pos,
                    style = androidx.compose.ui.graphics.Stroke(width = 2f)
                )

                // Draw label (node ID)
                val labelStyle = TextStyle(fontSize = 14.sp, color = ink)
                val labelText = textMeasurer.measure(node.label, labelStyle)
                drawText(
                    labelText,
                    topLeft = Offset(pos.x - labelText.size.width / 2, pos.y - labelText.size.height / 2)
                )
            }
        }
    }
}
