package com.interviewpreplab.features.syllabus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.interviewpreplab.features.topics.Topic
import com.interviewpreplab.features.topics.courses
import com.interviewpreplab.ui.components.color
import com.interviewpreplab.ui.theme.MonospaceFamily

@Composable
fun SyllabusScreen(contentPadding: PaddingValues, onTopic: (Topic) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp, top = contentPadding.calculateTopPadding() + 16.dp,
            bottom = contentPadding.calculateBottomPadding() + 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        item {
            Text("Syllabus", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface)
        }
        courses.forEach { course ->
            item(key = "h-${course.id}") {
                Column(Modifier.padding(top = 20.dp, bottom = 6.dp)) {
                    Text(course.title, style = MaterialTheme.typography.titleMedium, color = course.accent.color())
                    Text(course.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (course.topics.isEmpty()) {
                item(key = "soon-${course.id}") {
                    Text(
                        "Coming soon",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = MonospaceFamily,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            course.topics.forEachIndexed { i, topic ->
                item(key = "t-${topic.id}") {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onTopic(topic) }
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            "%02d  %s".format(i + 1, topic.title),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(topic.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
