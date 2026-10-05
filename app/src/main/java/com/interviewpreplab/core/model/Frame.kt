package com.interviewpreplab.core.model

/**
 * Immutable frame snapshot for playback. Every topic produces a list of these.
 * The scene field is domain-specific (BarsScene for sorts, TreeScene for trees, etc).
 */
data class Frame(
    val narr: String,                   // Narration: "Out of order, so swap them."
    val phase: String,                  // "compare", "swap", "done", etc.
    val codeLine: CodeLine? = null,     // Optional: which line to highlight
    val stats: Map<String, String> = emptyMap(), // {comparisons: "5", swaps: "2"}
    val scene: Scene                    // Domain-specific scene data
)

sealed class CodeLine {
    data class Single(val line: Int) : CodeLine()
    data class Range(val start: Int, val end: Int) : CodeLine()
}

/** Base for all scene types. Subclasses define what to draw on Canvas. */
sealed interface Scene

data class BarsScene(
    val bars: List<Bar>,        // one per array slot
    val narr: String = ""       // visual narration text
) : Scene

data class Bar(
    val value: Int,
    val roles: Set<String> = emptySet()  // "compare", "pivot", "sorted", "dim", "found", "landed"
)

data class SlotsScene(
    val slots: List<Slot>,
    val pointers: Map<String, Int> = emptyMap()  // "TOP" -> index, "FRONT" -> index, etc.
) : Scene

data class Slot(
    val value: Int?,                 // null = empty
    val roles: Set<String> = emptySet()
)

data class ListScene(
    val nodes: List<Node>,
    val head: Int? = null,           // index of head pointer
    val pointers: Map<String, Int> = emptyMap()
) : Scene

data class Node(
    val id: String,
    val value: Int,
    val next: String? = null,
    val roles: Set<String> = emptySet()
)

data class TreeScene(
    val root: TreeNode?,
    val highlighted: Set<String> = emptySet()  // node IDs
) : Scene

data class TreeNode(
    val id: String,
    val value: Int,
    val left: TreeNode? = null,
    val right: TreeNode? = null,
    val roles: Set<String> = emptySet()
)

data class GraphScene(
    val nodes: List<GraphNode>,
    val edges: List<GraphEdge>
) : Scene

data class GraphNode(
    val id: String,
    val label: String,
    val roles: Set<String> = emptySet()
)

data class GraphEdge(
    val from: String,
    val to: String,
    val weight: Int? = null,
    val roles: Set<String> = emptySet()
)
