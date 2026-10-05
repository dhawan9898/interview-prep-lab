package com.interviewpreplab.features.topics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.foundation.Canvas
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.ListScene
import com.interviewpreplab.core.model.MemoryScene
import com.interviewpreplab.core.model.SlotsScene
import com.interviewpreplab.core.player.PlayerViewModel
import com.interviewpreplab.core.ui.BarsRenderer
import com.interviewpreplab.core.ui.GraphRenderer
import com.interviewpreplab.core.ui.ListRenderer
import com.interviewpreplab.core.ui.MemoryRenderer
import com.interviewpreplab.core.ui.PlayerControls
import com.interviewpreplab.core.ui.SlotsRenderer
import com.interviewpreplab.core.ui.TreeRenderer
import com.interviewpreplab.core.ui.PacketFlowRenderer
import com.interviewpreplab.core.ui.PacketHeaderRenderer
import com.interviewpreplab.core.ui.TopologyRenderer
import com.interviewpreplab.ui.theme.MemoryAccent
import com.interviewpreplab.ui.theme.StackAccent
import com.interviewpreplab.ui.theme.PacketAccent
import com.interviewpreplab.ui.theme.KernelAccent
import com.interviewpreplab.features.c_programming.MemoryLayoutRunner
import com.interviewpreplab.features.c_programming.PointersRunner
import com.interviewpreplab.features.c_programming.StackVsHeapRunner
import com.interviewpreplab.features.c_programming.PointerArithmeticRunner
import com.interviewpreplab.features.c_programming.NullPointersRunner
import com.interviewpreplab.features.c_programming.FunctionPointersRunner
import com.interviewpreplab.features.c_programming.BufferOverflowRunner
import com.interviewpreplab.features.c_programming.UseAfterFreeRunner
import com.interviewpreplab.features.c_programming.MemoryLeaksRunner
import com.interviewpreplab.features.c_programming.StructPaddingRunner
import com.interviewpreplab.features.c_programming.EndiannessRunner
import com.interviewpreplab.features.c_programming.UndefinedBehaviorRunner
import com.interviewpreplab.features.networking.ARPRunner
import com.interviewpreplab.features.networking.TCPHandshakeRunner
import com.interviewpreplab.features.networking.EthernetRunner
import com.interviewpreplab.features.networking.IPv4HeaderRunner
import com.interviewpreplab.features.networking.IPv6HeaderRunner
import com.interviewpreplab.features.networking.SubnettingRunner
import com.interviewpreplab.features.networking.LongestPrefixMatchRunner
import com.interviewpreplab.features.networking.ICMPTracerouteRunner
import com.interviewpreplab.features.networking.NATRunner
import com.interviewpreplab.features.networking.OSPFRunner
import com.interviewpreplab.features.networking.BGPRunner
import com.interviewpreplab.features.networking.TCPTeardownRunner
import com.interviewpreplab.features.networking.DNSResolutionRunner
import com.interviewpreplab.features.networking.DHCPLeaseRunner
import com.interviewpreplab.features.networking.TLSHandshakeRunner
import com.interviewpreplab.features.networking.MACLearningRunner
import com.interviewpreplab.features.networking.VLANRunner
import com.interviewpreplab.features.networking.STPRunner
import com.interviewpreplab.features.networking.LACPRunner
import com.interviewpreplab.features.networking.SwitchDiagnosticsRunner
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
                            // C Programming - Memory Visualization
                            "memory-layout", "pointers", "stack-vs-heap", "pointer-arithmetic",
                            "null-pointers", "function-pointers", "buffer-overflow", "use-after-free",
                            "memory-leaks", "struct-padding", "endianness", "undefined-behavior" -> {
                                val scene = state.currentFrame?.scene as? MemoryScene
                                if (scene != null) {
                                    MemoryRenderer(scene, modifier = Modifier.fillMaxWidth())
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }

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

                            // Networking Topics
                            "ethernet", "arp", "ipv4-header", "ipv6-header", "dns-resolution",
                            "dhcp-lease", "tcp-teardown" -> {
                                val scene = state.currentFrame?.scene as? com.interviewpreplab.core.model.PacketHeaderScene
                                if (scene != null) {
                                    PacketHeaderRenderer(scene, modifier = Modifier.fillMaxWidth())
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            "tcp-handshake", "icmp-traceroute", "nat", "ospf", "bgp" -> {
                                val scene = state.currentFrame?.scene as? com.interviewpreplab.core.model.PacketFlowScene
                                if (scene != null) {
                                    PacketFlowRenderer(scene, modifier = Modifier.fillMaxWidth())
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            "mac-learning", "vlan", "stp", "lacp", "switch-diag" -> {
                                val scene = state.currentFrame?.scene as? com.interviewpreplab.core.model.TopologyScene
                                if (scene != null) {
                                    TopologyRenderer(scene, modifier = Modifier.fillMaxWidth())
                                } else {
                                    Text("Loading visualization...", style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            // Other topics that use simple text visualization
                            "subnetting", "lpm", "tls-handshake" -> {
                                Text("Protocol overview - see narration below", style = MaterialTheme.typography.bodySmall)
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
