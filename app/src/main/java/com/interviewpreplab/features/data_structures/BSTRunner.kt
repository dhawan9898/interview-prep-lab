package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.TreeNode
import com.interviewpreplab.core.model.TreeScene

/**
 * Binary Search Tree operations: insert and search.
 * Generates frame-by-frame narration showing tree structure changes.
 */
object BSTRunner {
    data class BSTState(
        val root: TreeNode? = null,
        val size: Int = 0
    )

    fun insert(state: BSTState, value: Int): Pair<List<Frame>, BSTState> {
        val frames = mutableListOf<Frame>()

        // Start frame
        frames.add(Frame(
            narr = "Insert value $value into the BST.",
            phase = "insert-start",
            stats = mapOf("size" to (state.size + 1).toString()),
            scene = TreeScene(root = state.root)
        ))

        if (state.root == null) {
            // Create root
            val newRoot = TreeNode(
                id = value.toString(),
                value = value,
                roles = setOf("landed")
            )

            frames.add(Frame(
                narr = "Tree is empty. Create root node with value $value.",
                phase = "insert-root",
                stats = mapOf("size" to "1"),
                scene = TreeScene(root = newRoot)
            ))

            return frames to BSTState(newRoot, 1)
        }

        // Insert by traversal (simplified: just show the final tree)
        val newRoot = copyAndInsert(state.root, value)

        frames.add(Frame(
            narr = "Value $value inserted into the tree.",
            phase = "insert-done",
            stats = mapOf("size" to (state.size + 1).toString()),
            scene = TreeScene(root = newRoot)
        ))

        return frames to BSTState(newRoot, state.size + 1)
    }

    fun search(state: BSTState, value: Int): Pair<List<Frame>, BSTState> {
        val frames = mutableListOf<Frame>()

        if (state.root == null) {
            frames.add(Frame(
                narr = "Tree is empty. Value $value not found.",
                phase = "search-not-found",
                stats = mapOf("size" to "0"),
                scene = TreeScene(root = null)
            ))
            return frames to state
        }

        // Simple recursive search (show start and result frames)
        frames.add(Frame(
            narr = "Search for value $value in the BST.",
            phase = "search-start",
            stats = mapOf("size" to state.size.toString()),
            scene = TreeScene(root = state.root)
        ))

        if (findInTree(state.root, value) != null) {
            val foundRoot = copyAndHighlight(state.root, value)
            frames.add(Frame(
                narr = "Value $value found!",
                phase = "search-found",
                stats = mapOf("size" to state.size.toString()),
                scene = TreeScene(root = foundRoot, highlighted = setOf(value.toString()))
            ))
        } else {
            frames.add(Frame(
                narr = "Value $value not found in the BST.",
                phase = "search-not-found",
                stats = mapOf("size" to state.size.toString()),
                scene = TreeScene(root = state.root)
            ))
        }

        return frames to state
    }

    fun buildInitial(): Frame {
        return Frame(
            narr = "Binary Search Tree: left child < parent < right child. Fast search and insertion.",
            phase = "start",
            stats = mapOf("size" to "0"),
            scene = TreeScene(root = null)
        )
    }

    private fun copyAndInsert(node: TreeNode, value: Int): TreeNode {
        return when {
            value < node.value -> {
                node.copy(left = copyAndInsert(node.left ?: createNode(value), value))
            }
            value > node.value -> {
                node.copy(right = copyAndInsert(node.right ?: createNode(value), value))
            }
            else -> node // Duplicate, no insertion
        }
    }

    private fun createNode(value: Int): TreeNode {
        return TreeNode(
            id = value.toString(),
            value = value,
            roles = setOf("landed")
        )
    }

    private fun findInTree(node: TreeNode?, value: Int): TreeNode? {
        return when {
            node == null -> null
            value == node.value -> node
            value < node.value -> findInTree(node.left, value)
            else -> findInTree(node.right, value)
        }
    }

    private fun copyAndHighlight(node: TreeNode, value: Int): TreeNode {
        return when {
            node.value == value -> node.copy(roles = setOf("found"))
            value < node.value && node.left != null -> {
                node.copy(left = copyAndHighlight(node.left, value))
            }
            value > node.value && node.right != null -> {
                node.copy(right = copyAndHighlight(node.right, value))
            }
            else -> node
        }
    }
}
