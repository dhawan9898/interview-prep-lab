package com.interviewpreplab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.ListScene
import com.interviewpreplab.core.model.SlotsScene
import com.interviewpreplab.core.player.PlayerViewModel
import com.interviewpreplab.core.ui.BarsRenderer
import com.interviewpreplab.core.ui.ListRenderer
import com.interviewpreplab.core.ui.PlayerControls
import com.interviewpreplab.core.ui.SlotsRenderer
import com.interviewpreplab.core.ui.TreeRenderer
import com.interviewpreplab.features.data_structures.BSTRunner
import com.interviewpreplab.features.data_structures.CircularQueueRunner
import com.interviewpreplab.features.data_structures.LinkedListRunner
import com.interviewpreplab.features.data_structures.QueueRunner
import com.interviewpreplab.features.data_structures.StackRunner
import com.interviewpreplab.features.sorts.BubbleSortRunner
import com.interviewpreplab.ui.theme.InterviewPrepLabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val playerVM: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterviewPrepLabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(playerVM)
                }
            }
        }
    }
}

data class Topic(
    val id: String,
    val title: String,
    val category: String,
    val description: String
)

@Composable
fun MainScreen(playerVM: PlayerViewModel) {
    var selectedTopic by remember { mutableStateOf<Topic?>(null) }

    if (selectedTopic == null) {
        TopicListScreen(
            onTopicSelected = { topic ->
                selectedTopic = topic
                loadTopicFrames(playerVM, topic)
            }
        )
    } else {
        TopicDetailScreen(
            topic = selectedTopic!!,
            playerVM = playerVM,
            onBack = { selectedTopic = null }
        )
    }
}

@Composable
fun TopicListScreen(onTopicSelected: (Topic) -> Unit) {
    val topics = listOf(
        // Sorting
        Topic("bubble-sort", "Bubble Sort", "Sorting", "Simple comparison-based sort"),

        // Data Structures
        Topic("stack", "Stack", "Data Structures", "LIFO data structure"),
        Topic("queue", "Queue", "Data Structures", "FIFO data structure with dead space"),
        Topic("circular-queue", "Circular Queue", "Data Structures", "FIFO with modulo wraparound"),
        Topic("linked-list", "Singly Linked List", "Data Structures", "Dynamic list with pointers"),
        Topic("bst", "Binary Search Tree", "Data Structures", "Ordered tree for efficient search"),
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Interview Prep Lab") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(topics) { topic ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTopicSelected(topic) }
                        .padding(0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            topic.title,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            topic.category,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            topic.description,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopicDetailScreen(
    topic: Topic,
    playerVM: PlayerViewModel,
    onBack: () -> Unit
) {
    val state by playerVM.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text(topic.title) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Visualization
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                when (topic.id) {
                    "bubble-sort" -> {
                        val scene = state.currentFrame?.scene as? BarsScene
                        if (scene != null) {
                            BarsRenderer(scene, 9)
                        }
                    }
                    "stack" -> {
                        val scene = state.currentFrame?.scene as? SlotsScene
                        if (scene != null) {
                            SlotsRenderer(scene, direction = "v")
                        }
                    }
                    "linked-list" -> {
                        val scene = state.currentFrame?.scene as? ListScene
                        if (scene != null) {
                            ListRenderer(scene)
                        }
                    }
                    "bst" -> {
                        val scene = state.currentFrame?.scene as? com.interviewpreplab.core.model.TreeScene
                        if (scene != null) {
                            TreeRenderer(scene)
                        }
                    }
                }
            }

            // Narration
            state.currentFrame?.let { frame ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        "Phase: ${frame.phase}",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        frame.narr,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Stats
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        frame.stats.forEach { (key, value) ->
                            Text(
                                "$key: $value",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }

            // Controls
            PlayerControls(
                state = state,
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
}

private fun loadTopicFrames(playerVM: PlayerViewModel, topic: Topic) {
    val frames = when (topic.id) {
        "bubble-sort" -> BubbleSortRunner.run(listOf(5, 2, 8, 1, 9, 3))
        "stack" -> {
            // Interactive stack demo: push 10, push 20, pop
            val frames = mutableListOf(StackRunner.buildInitial())
            var state = StackRunner.StackState()

            val (f1, s1) = StackRunner.runPush(state, 10)
            frames.addAll(f1)
            state = s1

            val (f2, s2) = StackRunner.runPush(state, 20)
            frames.addAll(f2)
            state = s2

            val (f3, s3) = StackRunner.runPop(state)
            frames.addAll(f3)

            frames
        }
        "queue" -> {
            // Interactive queue demo: enqueue 10, 20, dequeue
            val frames = mutableListOf(QueueRunner.buildInitial())
            var state = QueueRunner.QueueState(mutableListOf(), 0, -1)

            val (f1, s1) = QueueRunner.enqueue(state, 10)
            frames.addAll(f1)
            state = s1

            val (f2, s2) = QueueRunner.enqueue(state, 20)
            frames.addAll(f2)
            state = s2

            val (f3, s3) = QueueRunner.dequeue(state)
            frames.addAll(f3)

            frames
        }
        "circular-queue" -> {
            // Interactive circular queue demo: enqueue values, show wraparound
            val frames = mutableListOf(CircularQueueRunner.buildInitial())
            var state = CircularQueueRunner.CircularQueueState(MutableList(5) { null }, 0, 0)

            for (i in 1..3) {
                val (f, s) = CircularQueueRunner.enqueue(state, i * 10)
                frames.addAll(f)
                state = s
            }

            val (fd, sd) = CircularQueueRunner.dequeue(state)
            frames.addAll(fd)

            frames
        }
        "linked-list" -> {
            val frames = mutableListOf(LinkedListRunner.buildInitial())
            var state = LinkedListRunner.LinkedListState()

            val (f1, s1) = LinkedListRunner.insertAtHead(state, 10)
            frames.addAll(f1)
            state = s1

            val (f2, s2) = LinkedListRunner.insertAtHead(state, 20)
            frames.addAll(f2)
            state = s2

            val (f3, s3) = LinkedListRunner.insertAtHead(state, 30)
            frames.addAll(f3)

            frames
        }
        "bst" -> {
            val frames = mutableListOf(BSTRunner.buildInitial())
            var state = BSTRunner.BSTState(null, 0)

            val (f1, s1) = BSTRunner.insert(state, 50)
            frames.addAll(f1)
            state = s1

            val (f2, s2) = BSTRunner.insert(state, 30)
            frames.addAll(f2)
            state = s2

            val (f3, s3) = BSTRunner.insert(state, 70)
            frames.addAll(f3)
            state = s3

            val (f4, s4) = BSTRunner.search(state, 30)
            frames.addAll(f4)

            frames
        }
        else -> emptyList()
    }

    playerVM.loadFrames(frames)
}
