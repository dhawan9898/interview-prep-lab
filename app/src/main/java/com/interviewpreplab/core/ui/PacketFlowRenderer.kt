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
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

/**
 * Renders a PacketFlowScene showing protocol message exchanges as sequence diagrams.
 * Shows nodes on vertical axis, packets flowing between them, with timeline on horizontal axis.
 */
@Composable
fun PacketFlowRenderer(
    scene: PacketFlowScene,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
    ) {
        // Legend
        Text(
            text = scene.description,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Packet flow diagram
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            drawPacketFlow(scene, textMeasurer)
        }
    }
}

private fun DrawScope.drawPacketFlow(
    scene: PacketFlowScene,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    val canvasWidth = size.width
    val canvasHeight = size.height
    val nodeSpacing = canvasWidth / (scene.nodes.size + 1)
    val padding = 20f

    // Draw vertical timeline axes for each node
    scene.nodes.forEachIndexed { index, node ->
        val xPos = nodeSpacing * (index + 1)

        // Node box
        drawCircle(
            color = Color(0xFF4285F4),
            radius = 15f,
            center = Offset(xPos, padding)
        )

        // Timeline line
        drawLine(
            color = Color.Gray,
            start = Offset(xPos, padding + 15f),
            end = Offset(xPos, canvasHeight - padding),
            strokeWidth = 2f
        )

        // Node label
        val textLayout = textMeasurer.measure(
            text = node.label,
            style = TextStyle(fontSize = 10.sp)
        )
        drawText(
            textLayout = textLayout,
            topLeft = Offset(xPos - textLayout.size.width / 2, padding + 20f)
        )
    }

    // Draw packets as arrows
    scene.packets.forEachIndexed { index, packet ->
        val fromNode = scene.nodes.find { it.id == packet.from }
        val toNode = scene.nodes.find { it.id == packet.to }

        if (fromNode != null && toNode != null) {
            val fromNodeIndex = scene.nodes.indexOf(fromNode)
            val toNodeIndex = scene.nodes.indexOf(toNode)

            val fromX = nodeSpacing * (fromNodeIndex + 1)
            val toX = nodeSpacing * (toNodeIndex + 1)
            val yPos = padding + 50f + (index * 40f)

            // Arrow line
            drawLine(
                color = getProtocolColor(packet.protocol),
                start = Offset(fromX, yPos),
                end = Offset(toX, yPos),
                strokeWidth = 2f
            )

            // Arrowhead
            drawArrowHead(fromX, toX, yPos, getProtocolColor(packet.protocol))

            // Packet label
            val midX = (fromX + toX) / 2
            val textLayout = textMeasurer.measure(
                text = packet.protocol,
                style = TextStyle(fontSize = 9.sp)
            )
            drawText(
                textLayout = textLayout,
                topLeft = Offset(midX - textLayout.size.width / 2, yPos - 12f)
            )
        }
    }
}

private fun DrawScope.drawArrowHead(
    fromX: Float,
    toX: Float,
    yPos: Float,
    color: Color
) {
    val arrowSize = 10f
    if (toX > fromX) {
        // Arrow pointing right
        drawLine(color, Offset(toX - arrowSize, yPos - arrowSize / 2), Offset(toX, yPos), strokeWidth = 2f)
        drawLine(color, Offset(toX - arrowSize, yPos + arrowSize / 2), Offset(toX, yPos), strokeWidth = 2f)
    } else {
        // Arrow pointing left
        drawLine(color, Offset(toX + arrowSize, yPos - arrowSize / 2), Offset(toX, yPos), strokeWidth = 2f)
        drawLine(color, Offset(toX + arrowSize, yPos + arrowSize / 2), Offset(toX, yPos), strokeWidth = 2f)
    }
}

private fun getProtocolColor(protocol: String): Color = when (protocol.uppercase()) {
    "TCP" -> Color(0xFF4285F4)      // Blue
    "ARP" -> Color(0xFFEA4335)      // Red
    "IP", "IPv4", "IPv6" -> Color(0xFF34A853)  // Green
    "DNS" -> Color(0xFFBBBBBB)      // Gray
    "DHCP" -> Color(0xFFFBBC04)     // Yellow
    "ICMP" -> Color(0xFF5F6368)     // Dark Gray
    else -> Color(0xFF9AA0A6)       // Light Gray
}
