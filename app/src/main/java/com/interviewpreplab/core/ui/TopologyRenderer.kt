package com.interviewpreplab.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.interviewpreplab.core.model.TopologyScene

/**
 * Renders a TopologyScene showing network topology with nodes, links, and protocols.
 * Displays routers, switches, hosts with their connections and status.
 */
@Composable
fun TopologyRenderer(
    scene: TopologyScene,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    ) {
        Text(
            text = scene.description,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            drawTopology(scene, textMeasurer)
        }
    }
}

private fun DrawScope.drawTopology(
    scene: TopologyScene,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    val canvasWidth = size.width
    val canvasHeight = size.height
    val padding = 30f

    // Draw links first (behind nodes)
    scene.links.forEach { link ->
        val fromNode = scene.nodes.find { it.id == link.from }
        val toNode = scene.nodes.find { it.id == link.to }

        if (fromNode != null && toNode != null) {
            val fromPos = Offset(
                padding + fromNode.x * (canvasWidth - 2 * padding),
                padding + fromNode.y * (canvasHeight - 2 * padding)
            )
            val toPos = Offset(
                padding + toNode.x * (canvasWidth - 2 * padding),
                padding + toNode.y * (canvasHeight - 2 * padding)
            )

            // Draw link with status color
            val linkColor = when (link.status) {
                "up" -> Color(0xFF34A853)  // Green
                "down" -> Color(0xFFEA4335) // Red
                "blocked" -> Color(0xFFFBBC04) // Yellow
                else -> Color.Gray
            }

            drawLine(linkColor, fromPos, toPos, strokeWidth = 2f)
        }
    }

    // Draw nodes
    scene.nodes.forEach { node ->
        val nodeX = padding + node.x * (canvasWidth - 2 * padding)
        val nodeY = padding + node.y * (canvasHeight - 2 * padding)

        // Node circle
        val color = when {
            node.highlighted -> Color(0xFFFFD700)  // Gold
            node.type == "router" -> Color(0xFF4285F4)  // Blue
            node.type == "switch" -> Color(0xFFEA4335)  // Red
            else -> Color(0xFF34A853)  // Green
        }

        drawCircle(color, radius = 12f, center = Offset(nodeX, nodeY))

        // Node label
        val textLayout = textMeasurer.measure(
            text = node.label.take(10),
            style = TextStyle(fontSize = 8.sp)
        )
        drawText(
            textLayout = textLayout,
            topLeft = Offset(nodeX - textLayout.size.width / 2, nodeY + 15f)
        )
    }
}
