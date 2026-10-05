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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.interviewpreplab.core.model.TreeNode
import com.interviewpreplab.core.model.TreeScene
import kotlin.math.abs

/**
 * Renders a TreeScene (binary tree visualization).
 * Uses in-order layout: x = in-order rank, y = depth.
 * Draws SVG-style edges connecting parent to children.
 */
@Composable
fun TreeRenderer(
    scene: TreeScene,
    modifier: Modifier = Modifier
) {
    val root = scene.root
    if (root == null) {
        return
    }

    val paper = MaterialTheme.colorScheme.surface
    val ink = MaterialTheme.colorScheme.onSurface
    val stamp = Color(0xFFDD4B39)    // red: active
    val plot = Color(0xFF4285F4)     // blue: pointer
    val seal = Color(0xFF0F9D58)     // green: done

    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .background(paper)
            .padding(12.dp)
            .size(600.dp, 500.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            // First, compute in-order positions
            val positions = mutableMapOf<String, Offset>()
            var inOrderIndex = 0

            fun computePositions(node: TreeNode?, depth: Int): Int {
                if (node == null) return inOrderIndex

                // Traverse left subtree
                inOrderIndex = computePositions(node.left, depth + 1)

                // Compute position for this node (in-order rank = x, depth = y)
                val nodeWidth = 60.dp.toPx()
                val nodeHeight = 50.dp.toPx()
                val xSpacing = 80.dp.toPx()
                val ySpacing = 100.dp.toPx()

                val x = 30.dp.toPx() + inOrderIndex * xSpacing
                val y = 30.dp.toPx() + depth * ySpacing

                positions[node.id] = Offset(x, y)
                inOrderIndex++

                // Traverse right subtree
                inOrderIndex = computePositions(node.right, depth + 1)

                return inOrderIndex
            }

            computePositions(root, 0)

            // Draw edges
            fun drawEdges(node: TreeNode?) {
                if (node == null) return

                val parentPos = positions[node.id] ?: return

                // Draw left edge
                if (node.left != null) {
                    val childPos = positions[node.left.id] ?: return
                    drawLine(
                        color = ink.copy(alpha = 0.3f),
                        start = Offset(parentPos.x + 30.dp.toPx(), parentPos.y + 25.dp.toPx()),
                        end = Offset(childPos.x + 30.dp.toPx(), childPos.y - 25.dp.toPx()),
                        strokeWidth = 2f
                    )
                }

                // Draw right edge
                if (node.right != null) {
                    val childPos = positions[node.right.id] ?: return
                    drawLine(
                        color = ink.copy(alpha = 0.3f),
                        start = Offset(parentPos.x + 30.dp.toPx(), parentPos.y + 25.dp.toPx()),
                        end = Offset(childPos.x + 30.dp.toPx(), childPos.y - 25.dp.toPx()),
                        strokeWidth = 2f
                    )
                }

                drawEdges(node.left)
                drawEdges(node.right)
            }

            drawEdges(root)

            // Draw nodes
            fun drawNodes(node: TreeNode?) {
                if (node == null) return

                val pos = positions[node.id] ?: return
                val nodeWidth = 60.dp.toPx()
                val nodeHeight = 50.dp.toPx()

                val nodeColor = when {
                    "landed" in node.roles -> stamp.copy(alpha = 0.7f)
                    "active" in node.roles -> plot
                    "found" in node.roles -> seal
                    else -> ink.copy(alpha = 0.2f)
                }

                // Draw node box
                drawRect(
                    color = nodeColor,
                    topLeft = Offset(pos.x, pos.y),
                    size = androidx.compose.ui.geometry.Size(nodeWidth, nodeHeight),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
                )

                // Draw value
                val valueStyle = TextStyle(fontSize = 16.sp, color = ink)
                val valueText = textMeasurer.measure(node.value.toString(), valueStyle)
                drawText(
                    valueText,
                    topLeft = Offset(
                        pos.x + (nodeWidth - valueText.size.width) / 2,
                        pos.y + (nodeHeight - valueText.size.height) / 2
                    )
                )

                drawNodes(node.left)
                drawNodes(node.right)
            }

            drawNodes(root)
        }
    }
}
