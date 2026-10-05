package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.ListScene
import com.interviewpreplab.core.model.Node
import java.util.UUID

/**
 * Singly linked list operations: insert at head, insert at tail, delete.
 * Generates frame-by-frame narration.
 */
object LinkedListRunner {
    /** Runner-side linked node; converted to scene [Node]s (next = id) for rendering. */
    data class ListNode(
        val id: String,
        val value: Int,
        val next: ListNode? = null,
        val roles: Set<String> = emptySet()
    )

    data class LinkedListState(
        val head: ListNode? = null,
        val size: Int = 0
    )

    private fun copyNode(node: ListNode?): ListNode? {
        if (node == null) return null
        return node.copy(
            next = copyNode(node.next)
        )
    }

    fun insertAtHead(state: LinkedListState, value: Int): Pair<List<Frame>, LinkedListState> {
        val frames = mutableListOf<Frame>()

        // Create new node
        val newNode = ListNode(
            id = UUID.randomUUID().toString().take(8),
            value = value,
            next = copyNode(state.head),
            roles = setOf("landed")
        )

        // Frame 1: Create node
        frames.add(Frame(
            narr = "Create a new node with value $value.",
            phase = "insert-create",
            stats = mapOf("size" to (state.size + 1).toString()),
            scene = ListScene(
                nodes = nodeListToNodes(newNode),
                head = 0
            )
        ))

        // Frame 2: Link to old head
        frames.add(Frame(
            narr = "Link the new node to the previous head.",
            phase = "insert-link",
            stats = mapOf("size" to (state.size + 1).toString()),
            scene = ListScene(
                nodes = nodeListToNodes(newNode),
                head = 0
            )
        ))

        // Frame 3: Update head pointer
        frames.add(Frame(
            narr = "Update head pointer to point to the new node.",
            phase = "insert-done",
            stats = mapOf("size" to (state.size + 1).toString()),
            scene = ListScene(
                nodes = nodeListToNodes(newNode),
                head = 0,
                pointers = mapOf("HEAD" to 0)
            )
        ))

        val newState = LinkedListState(newNode, state.size + 1)
        return frames to newState
    }

    fun deleteAtHead(state: LinkedListState): Pair<List<Frame>, LinkedListState> {
        val frames = mutableListOf<Frame>()

        if (state.head == null) {
            frames.add(Frame(
                narr = "List is empty! Cannot delete.",
                phase = "error",
                stats = mapOf("size" to "0"),
                scene = ListScene(nodes = emptyList())
            ))
            return frames to state
        }

        val deletedValue = state.head.value

        // Frame 1: Mark node for deletion
        frames.add(Frame(
            narr = "Node with value $deletedValue is at head.",
            phase = "delete-mark",
            stats = mapOf("size" to state.size.toString()),
            scene = ListScene(
                nodes = nodeListToNodes(state.head),
                head = 0
            )
        ))

        // Frame 2: Update head pointer
        val newHead = copyNode(state.head.next)
        frames.add(Frame(
            narr = "Update head pointer to the next node.",
            phase = "delete-unlink",
            stats = mapOf("size" to (state.size - 1).toString()),
            scene = ListScene(
                nodes = if (newHead != null) nodeListToNodes(newHead) else emptyList(),
                head = if (newHead != null) 0 else null
            )
        ))

        val newState = LinkedListState(newHead, state.size - 1)
        return frames to newState
    }

    fun buildInitial(): Frame {
        return Frame(
            narr = "Singly Linked List: each node contains a value and a pointer to the next node.",
            phase = "start",
            stats = mapOf("size" to "0"),
            scene = ListScene(
                nodes = emptyList(),
                head = null
            )
        )
    }

    private fun nodeListToNodes(head: ListNode?): List<Node> {
        val result = mutableListOf<Node>()
        var current = head
        while (current != null) {
            result.add(Node(id = current.id, value = current.value, next = current.next?.id, roles = current.roles))
            current = current.next
        }
        return result
    }
}
