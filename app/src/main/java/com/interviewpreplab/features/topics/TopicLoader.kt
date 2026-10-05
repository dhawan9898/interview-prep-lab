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

fun loadTopicFrames(playerVM: PlayerViewModel, topic: Topic) {
    val frames = when (topic.id) {
        // C Programming Topics
        "memory-layout" -> MemoryLayoutRunner.run()
        "pointers" -> PointersRunner.run()
        "stack-vs-heap" -> StackVsHeapRunner.run()
        "pointer-arithmetic" -> PointerArithmeticRunner.run()
        "null-pointers" -> NullPointersRunner.run()
        "function-pointers" -> FunctionPointersRunner.run()
        "buffer-overflow" -> BufferOverflowRunner.run()
        "use-after-free" -> UseAfterFreeRunner.run()
        "memory-leaks" -> MemoryLeaksRunner.run()
        "struct-padding" -> StructPaddingRunner.run()
        "endianness" -> EndiannessRunner.run()
        "undefined-behavior" -> UndefinedBehaviorRunner.run()

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

        // Networking Topics
        "ethernet" -> EthernetRunner.run()
        "arp" -> ARPRunner.run()
        "ipv4-header" -> IPv4HeaderRunner.run()
        "ipv6-header" -> IPv6HeaderRunner.run()
        "subnetting" -> SubnettingRunner.run()
        "lpm" -> LongestPrefixMatchRunner.run()
        "icmp-traceroute" -> ICMPTracerouteRunner.run()
        "nat" -> NATRunner.run()
        "ospf" -> OSPFRunner.run()
        "bgp" -> BGPRunner.run()
        "tcp-teardown" -> TCPTeardownRunner.run()
        "dns-resolution" -> DNSResolutionRunner.run()
        "dhcp-lease" -> DHCPLeaseRunner.run()
        "tls-handshake" -> TLSHandshakeRunner.run()
        "mac-learning" -> MACLearningRunner.run()
        "vlan" -> VLANRunner.run()
        "stp" -> STPRunner.run()
        "lacp" -> LACPRunner.run()
        "switch-diag" -> SwitchDiagnosticsRunner.run()
        "tcp-handshake" -> TCPHandshakeRunner.run()

        else -> emptyList()
    }

    playerVM.loadFrames(frames)
}
