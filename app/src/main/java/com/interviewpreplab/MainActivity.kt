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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.ListScene
import com.interviewpreplab.core.model.SlotsScene
import com.interviewpreplab.core.player.PlayerViewModel
import com.interviewpreplab.core.ui.BarsRenderer
import com.interviewpreplab.core.ui.GraphRenderer
import com.interviewpreplab.core.ui.ListRenderer
import com.interviewpreplab.core.ui.PlayerControls
import com.interviewpreplab.core.ui.SlotsRenderer
import com.interviewpreplab.core.ui.TreeRenderer
import com.interviewpreplab.features.data_structures.BSTRunner
import com.interviewpreplab.features.progress.ProgressScreen
import com.interviewpreplab.features.data_structures.CircularQueueRunner
import com.interviewpreplab.features.data_structures.GraphRunner
import com.interviewpreplab.features.data_structures.HeapRunner
import com.interviewpreplab.features.data_structures.LinkedListRunner
import com.interviewpreplab.features.data_structures.QueueRunner
import com.interviewpreplab.features.data_structures.StackRunner
import com.interviewpreplab.features.searches.BinarySearchRunner
import com.interviewpreplab.features.sorts.BubbleSortRunner
import com.interviewpreplab.features.sorts.InsertionSortRunner
import com.interviewpreplab.features.sorts.QuickSortRunner
import com.interviewpreplab.features.sorts.SelectionSortRunner
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

val topicList = listOf(
    // Sorting
    Topic("bubble-sort", "Bubble Sort", "Sorting", "Simple comparison-based sort"),
    Topic("selection-sort", "Selection Sort", "Sorting", "Find minimum and swap"),
    Topic("insertion-sort", "Insertion Sort", "Sorting", "Build sorted array incrementally"),
    Topic("quick-sort", "Quick Sort", "Sorting", "Divide and conquer with pivot"),

    // Searching
    Topic("binary-search", "Binary Search", "Searching", "Halve search space each iteration"),

    // Data Structures
    Topic("stack", "Stack", "Data Structures", "LIFO data structure"),
    Topic("queue", "Queue", "Data Structures", "FIFO data structure with dead space"),
    Topic("circular-queue", "Circular Queue", "Data Structures", "FIFO with modulo wraparound"),
    Topic("linked-list", "Singly Linked List", "Data Structures", "Dynamic list with pointers"),
    Topic("bst", "Binary Search Tree", "Data Structures", "Ordered tree for efficient search"),
    Topic("heap", "Min Heap", "Data Structures", "Priority queue with heap property"),
    Topic("graph", "Graph (BFS/DFS)", "Data Structures", "Node and edge traversal"),
)

@Composable
fun MainScreen(playerVM: PlayerViewModel) {
    var selectedTopic by remember { mutableStateOf<Topic?>(null) }
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Topics) }

    if (selectedTopic != null) {
        TopicDetailScreen(
            topic = selectedTopic!!,
            playerVM = playerVM,
            onBack = { selectedTopic = null }
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                when (currentScreen) {
                    Screen.Topics -> TopicListScreen(
                        onTopicSelected = { topic ->
                            selectedTopic = topic
                            loadTopicFrames(playerVM, topic)
                        }
                    )
                    Screen.Progress -> ProgressScreen(
                        onTopicClick = { topicId ->
                            selectedTopic = topicList.find { it.id == topicId }
                            selectedTopic?.let { loadTopicFrames(playerVM, it) }
                        }
                    )
                }
            }

            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Topics") },
                    label = { Text("Topics") },
                    selected = currentScreen == Screen.Topics,
                    onClick = { currentScreen = Screen.Topics }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Progress") },
                    label = { Text("Progress") },
                    selected = currentScreen == Screen.Progress,
                    onClick = { currentScreen = Screen.Progress }
                )
            }
        }
    }
}

enum class Screen {
    Topics,
    Progress
}

@Composable
fun TopicListScreen(onTopicSelected: (Topic) -> Unit) {
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
            items(topicList) { topic ->
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
            .background(MaterialTheme.colorScheme.background)
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Frame Counter
            Text(
                text = "Frame ${state.frameIdx + 1} of ${state.frames.size}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Visualization Container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.currentFrame != null) {
                        when (topic.id) {
                            "bubble-sort", "selection-sort", "insertion-sort", "quick-sort" -> {
                                val scene = state.currentFrame?.scene as? BarsScene
                                if (scene != null) {
                                    BarsRenderer(scene, 9, modifier = Modifier.fillMaxWidth())
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            "merge-sort", "heap-sort", "shell-sort", "counting-sort" -> {
                                val scene = state.currentFrame?.scene as? BarsScene
                                if (scene != null) {
                                    BarsRenderer(scene, 15, modifier = Modifier.fillMaxWidth())
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            "binary-search", "jump-search", "linear-search" -> {
                                val scene = state.currentFrame?.scene as? BarsScene
                                if (scene != null) {
                                    BarsRenderer(scene, 20, modifier = Modifier.fillMaxWidth())
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            "stack", "queue", "circular-queue", "heap" -> {
                                val scene = state.currentFrame?.scene as? SlotsScene
                                if (scene != null) {
                                    SlotsRenderer(scene, direction = "v")
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            "linked-list" -> {
                                val scene = state.currentFrame?.scene as? ListScene
                                if (scene != null) {
                                    ListRenderer(scene)
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            "bst" -> {
                                val scene = state.currentFrame?.scene as? com.interviewpreplab.core.model.TreeScene
                                if (scene != null) {
                                    TreeRenderer(scene)
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            "heap" -> {
                                val scene = state.currentFrame?.scene as? com.interviewpreplab.core.model.TreeScene
                                if (scene != null) {
                                    TreeRenderer(scene)
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            "graph" -> {
                                val scene = state.currentFrame?.scene as? com.interviewpreplab.core.model.GraphScene
                                if (scene != null) {
                                    GraphRenderer(scene)
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            else -> {
                                Text("Visualization not available", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    } else {
                        Text("No frames loaded", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Narration & Explanation (CLEAR AND PROMINENT)
            state.currentFrame?.let { frame ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Phase label
                        Text(
                            text = "Phase: ${frame.phase.uppercase()}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Main explanation (LARGE, READABLE)
                        Text(
                            text = frame.narr,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Statistics
                        if (frame.stats.isNotEmpty()) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                frame.stats.forEach { (key, value) ->
                                    Column {
                                        Text(
                                            text = key.replace("_", " ").uppercase(),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = value.toString(),
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
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
        "selection-sort" -> SelectionSortRunner.run(listOf(5, 2, 8, 1, 9, 3))
        "insertion-sort" -> InsertionSortRunner.run(listOf(5, 2, 8, 1, 9, 3))
        "quick-sort" -> QuickSortRunner.run(listOf(5, 2, 8, 1, 9, 3))
        "binary-search" -> BinarySearchRunner.run(listOf(1, 2, 3, 5, 8, 9), 5)
        "stack" -> {
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
        "heap" -> {
            val frames = mutableListOf(HeapRunner.buildInitial())
            var state = HeapRunner.HeapState()
            for (v in listOf(10, 5, 20, 3)) {
                val (f, s) = HeapRunner.insert(state, v)
                frames.addAll(f)
                state = s
            }
            val (fe, se) = HeapRunner.extractMin(state)
            frames.addAll(fe)
            frames
        }
        "graph" -> {
            val graph = GraphRunner.createSimpleGraph()
            val frames = mutableListOf<Frame>(
                Frame(
                    narr = "BFS from node A",
                    phase = "start",
                    stats = mapOf("nodes_visited" to "0"),
                    scene = com.interviewpreplab.core.model.GraphScene(graph.nodes, graph.edges)
                )
            )
            frames.addAll(GraphRunner.bfs(graph, "A"))
            frames
        }
        else -> emptyList()
    }

    playerVM.loadFrames(frames)
}
