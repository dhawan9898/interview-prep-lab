package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.GraphEdge
import com.interviewpreplab.core.model.GraphNode
import com.interviewpreplab.core.model.GraphScene
import java.util.LinkedList
import java.util.Queue

/**
 * Graph algorithms: BFS and DFS traversal.
 * Generates frame-by-frame visualization of node exploration.
 */
object GraphRunner {
    data class GraphState(
        val nodes: List<GraphNode> = emptyList(),
        val edges: List<GraphEdge> = emptyList(),
        val adjacency: Map<String, List<String>> = emptyMap()
    )

    fun bfs(state: GraphState, startId: String): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Start frame
        frames.add(Frame(
            narr = "Breadth-First Search: explore nodes level by level.",
            phase = "start",
            stats = mapOf("nodes_visited" to "0"),
            scene = GraphScene(state.nodes, state.edges)
        ))

        if (startId !in state.nodes.map { it.id }) {
            return frames
        }

        val visited = mutableSetOf<String>()
        val queue: Queue<String> = LinkedList()
        queue.add(startId)
        visited.add(startId)
        var visitCount = 0

        // Mark start node
        frames.add(Frame(
            narr = "Start at node $startId.",
            phase = "bfs-start",
            stats = mapOf("nodes_visited" to "1"),
            scene = updateNodeRoles(state, mapOf(startId to setOf("done")))
        ))

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            visitCount++

            val neighbors = state.adjacency[current] ?: emptyList()

            for (neighbor in neighbors) {
                if (neighbor !in visited) {
                    visited.add(neighbor)

                    // Frame: discover new node
                    frames.add(Frame(
                        narr = "Discover $neighbor from $current. Add to queue.",
                        phase = "bfs-discover",
                        stats = mapOf("nodes_visited" to visitCount.toString()),
                        scene = updateNodeRoles(
                            state,
                            visited.associateWith { if (it == neighbor) setOf("frontier") else setOf("done") }
                        )
                    ))

                    queue.add(neighbor)
                }
            }
        }

        // Final frame
        frames.add(Frame(
            narr = "BFS complete. Visited ${visited.size} nodes.",
            phase = "bfs-done",
            stats = mapOf("nodes_visited" to visited.size.toString()),
            scene = updateNodeRoles(state, visited.associateWith { setOf("done") })
        ))

        return frames
    }

    fun dfs(state: GraphState, startId: String): List<Frame> {
        val frames = mutableListOf<Frame>()

        frames.add(Frame(
            narr = "Depth-First Search: explore as far as possible before backtracking.",
            phase = "start",
            stats = mapOf("nodes_visited" to "0"),
            scene = GraphScene(state.nodes, state.edges)
        ))

        if (startId !in state.nodes.map { it.id }) {
            return frames
        }

        val visited = mutableSetOf<String>()

        fun dfsHelper(nodeId: String) {
            visited.add(nodeId)

            frames.add(Frame(
                narr = "Visit $nodeId.",
                phase = "dfs-visit",
                stats = mapOf("nodes_visited" to visited.size.toString()),
                scene = updateNodeRoles(state, visited.associateWith { setOf("done") })
            ))

            val neighbors = state.adjacency[nodeId] ?: emptyList()
            for (neighbor in neighbors) {
                if (neighbor !in visited) {
                    frames.add(Frame(
                        narr = "Explore $neighbor from $nodeId.",
                        phase = "dfs-explore",
                        stats = mapOf("nodes_visited" to visited.size.toString()),
                        scene = updateNodeRoles(
                            state,
                            (visited + neighbor).associateWith { if (it == neighbor) setOf("frontier") else setOf("done") }
                        )
                    ))
                    dfsHelper(neighbor)
                }
            }
        }

        dfsHelper(startId)

        frames.add(Frame(
            narr = "DFS complete. Visited ${visited.size} nodes.",
            phase = "dfs-done",
            stats = mapOf("nodes_visited" to visited.size.toString()),
            scene = updateNodeRoles(state, visited.associateWith { setOf("done") })
        ))

        return frames
    }

    fun buildInitial(): Frame {
        return Frame(
            narr = "Graph algorithms: explore nodes and edges to find paths or order.",
            phase = "start",
            stats = mapOf("nodes_visited" to "0"),
            scene = GraphScene(emptyList(), emptyList())
        )
    }

    private fun updateNodeRoles(
        state: GraphState,
        roleMap: Map<String, Set<String>>
    ): GraphScene {
        val updatedNodes = state.nodes.map { node ->
            node.copy(roles = roleMap[node.id] ?: emptySet())
        }
        return GraphScene(updatedNodes, state.edges)
    }

    // Helper to create a simple test graph
    fun createSimpleGraph(): GraphState {
        val nodes = listOf(
            GraphNode("A", "A"),
            GraphNode("B", "B"),
            GraphNode("C", "C"),
            GraphNode("D", "D"),
            GraphNode("E", "E"),
        )

        val edges = listOf(
            GraphEdge("A", "B"),
            GraphEdge("A", "D"),
            GraphEdge("B", "C"),
            GraphEdge("D", "C"),
            GraphEdge("D", "E"),
            GraphEdge("C", "E"),
        )

        val adjacency = mapOf(
            "A" to listOf("B", "D"),
            "B" to listOf("C"),
            "C" to listOf("E"),
            "D" to listOf("C", "E"),
            "E" to listOf(),
        )

        return GraphState(nodes, edges, adjacency)
    }
}
