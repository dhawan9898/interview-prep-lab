package com.interviewpreplab.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.interviewpreplab.core.model.PacketHeaderScene

/**
 * Renders a PacketHeaderScene showing protocol header fields with bit-level precision.
 * Displays each field's bit position, value, and meaning.
 */
@Composable
fun PacketHeaderRenderer(
    scene: PacketHeaderScene,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Title
        Text(
            text = "${scene.protocol} Header (${scene.totalSize} bytes)",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Field rows
        scene.fields.forEach { field ->
            FieldRow(field)
        }

        // Description
        Text(
            text = scene.description,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun FieldRow(field: com.interviewpreplab.core.model.HeaderField) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .border(1.dp, Color.Gray)
            .background(if (field.highlighted) Color(0xFFFFFF99) else Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Field name
        Box(
            modifier = Modifier
                .weight(0.3f)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp)
        ) {
            Text(
                text = field.name,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Bit range
        Text(
            text = "Bits ${field.bitOffset}-${field.bitOffset + field.bitLength - 1}",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(0.25f)
        )

        // Value (hex/decimal)
        Text(
            text = field.value,
            style = MaterialTheme.typography.labelSmall,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
            modifier = Modifier.weight(0.2f)
        )

        // Meaning
        Text(
            text = field.meaning.take(30),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.weight(0.25f)
        )
    }
}
