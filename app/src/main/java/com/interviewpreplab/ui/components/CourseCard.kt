package com.interviewpreplab.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.interviewpreplab.features.topics.Course
import com.interviewpreplab.features.topics.CourseAccent
import com.interviewpreplab.features.topics.Topic
import com.interviewpreplab.ui.theme.HeapAccent
import com.interviewpreplab.ui.theme.KernelAccent
import com.interviewpreplab.ui.theme.MemoryAccent
import com.interviewpreplab.ui.theme.MonospaceFamily
import com.interviewpreplab.ui.theme.PacketAccent

fun CourseAccent.color(): Color = when (this) {
    CourseAccent.Memory -> MemoryAccent
    CourseAccent.Packet -> PacketAccent
    CourseAccent.Kernel -> KernelAccent
    CourseAccent.Stack -> HeapAccent
}

@Composable
fun CourseCard(
    course: Course,
    completedIds: Set<String>,
    onContinue: (Topic) -> Unit,
    modifier: Modifier = Modifier
) {
    val accent = course.accent.color()
    val topics = course.topics
    val done = topics.count { it.id in completedIds }
    val progress = if (topics.isEmpty()) 0f else done.toFloat() / topics.size
    val next = topics.firstOrNull { it.id !in completedIds }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(course.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(course.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = if (topics.isEmpty()) "Coming soon" else "$done / ${topics.size} lessons",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = MonospaceFamily,
                        color = accent,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                RadialProgress(progress = progress, color = accent)
            }

            if (next != null) {
                Text(
                    text = "NEXT  ${next.title}",
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = MonospaceFamily,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (done == 0) "Start Chapter →" else "Continue Chapter →",
                    style = MaterialTheme.typography.labelLarge,
                    color = accent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(accent.copy(alpha = 0.12f))
                        .clickable { onContinue(next) }
                        .padding(12.dp)
                )
            }
        }
    }
}
