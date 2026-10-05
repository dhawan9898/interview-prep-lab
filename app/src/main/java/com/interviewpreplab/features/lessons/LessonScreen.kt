package com.interviewpreplab.features.lessons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.interviewpreplab.core.player.PlayerViewModel
import com.interviewpreplab.core.ui.PlayerControls
import com.interviewpreplab.core.ui.SceneRenderer
import com.interviewpreplab.features.progress.ProgressViewModel
import com.interviewpreplab.features.topics.Topic
import com.interviewpreplab.features.topics.courseOf
import com.interviewpreplab.ui.components.color
import com.interviewpreplab.ui.theme.MonospaceFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val verticalSlotTopics = setOf("stack", "heap")

@Composable
fun LessonScreen(
    topic: Topic,
    playerVM: PlayerViewModel,
    onBack: () -> Unit,
    onOpenTopic: (Topic) -> Unit,
    progressVM: ProgressViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val player by playerVM.state.collectAsState()
    val progress by progressVM.uiState.collectAsState()

    val course = courseOf(topic)
    val siblings = course?.topics.orEmpty()
    val position = siblings.indexOfFirst { it.id == topic.id }
    val prev = siblings.getOrNull(position - 1)
    val next = siblings.getOrNull(position + 1)
    val accent = course?.accent?.color() ?: MaterialTheme.colorScheme.primary

    val completed = progress.allProgress.any { it.topicId == topic.id && it.isCompleted }
    val lesson by produceState<Lesson?>(null, topic.id) {
        value = withContext(Dispatchers.IO) { LessonLoader.load(context, topic.id) }
    }

    // Finishing the visualization counts as completing the lesson.
    val lastIdx = player.frames.lastIndex
    LaunchedEffect(topic.id, player.frameIdx, player.frames.size) {
        if (lastIdx > 0 && player.frameIdx == lastIdx && !completed) {
            progressVM.setCompleted(topic.id, topic.title, topic.category, true)
        }
    }
    DisposableEffect(topic.id) {
        val start = System.currentTimeMillis()
        onDispose {
            progressVM.recordView(topic.id, topic.title, topic.category, System.currentTimeMillis() - start)
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
            Column(Modifier.weight(1f)) {
                Text(topic.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    "${course?.title ?: topic.category}  ·  ${position + 1}/${siblings.size}",
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = MonospaceFamily,
                    color = accent
                )
            }
            Icon(
                if (completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = if (completed) "Completed" else "Not completed",
                tint = if (completed) accent else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(end = 12.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    lesson?.summary ?: topic.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (player.frames.isNotEmpty()) {
                item {
                    SectionCard(accent) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "FRAME ${player.frameIdx + 1} / ${player.frames.size}",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = MonospaceFamily,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Column(Modifier.fillMaxWidth().height(300.dp)) {
                                SceneRenderer(player.frames, player.frameIdx, topic.id in verticalSlotTopics)
                            }
                        }
                    }
                }
                player.currentFrame?.let { frame ->
                    item {
                        SectionCard(accent) {
                            Text(frame.phase.replace('_', ' ').uppercase(), style = MaterialTheme.typography.labelMedium, color = accent)
                            Text(frame.narr, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 6.dp))
                        }
                    }
                }
                item {
                    PlayerControls(
                        state = player,
                        onPlay = playerVM::play,
                        onPause = playerVM::pause,
                        onStepBack = playerVM::stepBack,
                        onStepForward = playerVM::stepForward,
                        onReset = playerVM::reset,
                        onScrub = playerVM::scrubTo,
                        onSpeedChange = playerVM::setSpeed
                    )
                }
            }

            val sections = lesson?.sections.orEmpty()
            if (lesson == null || (sections.isEmpty() && lesson?.subtopics.orEmpty().isEmpty())) {
                item {
                    SectionCard(accent) {
                        Text("Notes", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        Text(
                            "Detailed notes for this lesson are not written yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
            items(sections.size) { i -> SectionBlock(sections[i], accent) }

            val subtopics = lesson?.subtopics.orEmpty()
            if (subtopics.isNotEmpty()) {
                item { Text("Go deeper", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface) }
                items(subtopics.size) { i -> SubtopicCard(subtopics[i], accent) }
            }

            val takeaways = lesson?.takeaways.orEmpty()
            if (takeaways.isNotEmpty()) {
                item {
                    SectionCard(accent) {
                        Text("Key takeaways", style = MaterialTheme.typography.titleMedium, color = accent)
                        takeaways.forEach { Bullet(it) }
                    }
                }
            }
            val practice = lesson?.practice.orEmpty()
            if (practice.isNotEmpty()) {
                item {
                    SectionCard(accent) {
                        Text("Practice problems", style = MaterialTheme.typography.titleMedium, color = accent)
                        practice.forEach { PracticeRow(it) }
                    }
                }
            }
            val refs = lesson?.references.orEmpty()
            if (refs.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("References", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        refs.forEach {
                            Text("${it.label.orEmpty()}  ${it.url.orEmpty()}", style = MaterialTheme.typography.bodySmall, fontFamily = MonospaceFamily, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        Surface(tonalElevation = 3.dp, color = MaterialTheme.colorScheme.surface) {
            Row(
                Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(onClick = { prev?.let(onOpenTopic) }, enabled = prev != null, modifier = Modifier.weight(1f)) { Text("‹ Prev") }
                Button(
                    onClick = { progressVM.setCompleted(topic.id, topic.title, topic.category, !completed) },
                    modifier = Modifier.weight(1.4f)
                ) { Text(if (completed) "Completed ✓" else "Mark complete") }
                OutlinedButton(onClick = { next?.let(onOpenTopic) }, enabled = next != null, modifier = Modifier.weight(1f)) { Text("Next ›") }
            }
        }
    }
}

@Composable
private fun SectionCard(accent: androidx.compose.ui.graphics.Color, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
    ) { Column(Modifier.padding(14.dp)) { content() } }
}

@Composable
private fun Bullet(text: String) {
    Row(Modifier.padding(top = 4.dp)) {
        Text("•  ", color = MaterialTheme.colorScheme.primary)
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun SectionBlock(section: LessonSection, accent: androidx.compose.ui.graphics.Color) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        section.heading?.let { Text(it, style = MaterialTheme.typography.titleMedium, color = accent) }
        section.body?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface) }
        section.bullets.orEmpty().forEach { Bullet(it) }
        section.code?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = MonospaceFamily,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                    .horizontalScroll(rememberScrollState())
                    .padding(12.dp)
            )
        }
    }
}

@Composable
private fun SubtopicCard(sub: Subtopic, accent: androidx.compose.ui.graphics.Color) {
    var expanded by rememberSaveable(sub.title) { mutableStateOf(false) }
    SectionCard(accent) {
        Row(
            Modifier.fillMaxWidth().clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(sub.title.orEmpty(), style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                sub.summary?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, if (expanded) "Collapse" else "Expand", tint = accent)
        }
        if (expanded) {
            Column(Modifier.padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                sub.sections.orEmpty().forEach { SectionBlock(it, accent) }
            }
        }
    }
}

@Composable
private fun PracticeRow(item: PracticeItem) {
    val color = when (item.difficulty) {
        "Easy" -> MaterialTheme.colorScheme.secondary
        "Hard" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.tertiary
    }
    Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(
            item.difficulty.orEmpty().uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontFamily = MonospaceFamily,
            color = color,
            modifier = Modifier.width(56.dp)
        )
        Text(item.title.orEmpty(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}
