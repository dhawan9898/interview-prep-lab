package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.ListScene
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class LinkedListRunnerTest {
    @Test
    fun `insert at head creates new node`() {
        val state = LinkedListRunner.LinkedListState(null, 0)
        val (frames, newState) = LinkedListRunner.insertAtHead(state, 42)

        assertTrue(frames.isNotEmpty())
        assertEquals(1, newState.size)
        assertNotNull(newState.head)
        assertEquals(42, newState.head.value)
    }

    @Test
    fun `multiple inserts maintain order`() {
        var state = LinkedListRunner.LinkedListState(null, 0)

        val (frames1, state1) = LinkedListRunner.insertAtHead(state, 10)
        state = state1
        assertEquals(1, state.size)

        val (frames2, state2) = LinkedListRunner.insertAtHead(state, 20)
        state = state2
        assertEquals(2, state.size)

        // New head should be 20, pointing to 10
        assertEquals(20, state.head?.value)
        assertEquals(10, state.head?.next?.value)
    }

    @Test
    fun `delete from empty list returns error`() {
        val state = LinkedListRunner.LinkedListState(null, 0)
        val (frames, newState) = LinkedListRunner.deleteAtHead(state)

        assertTrue(frames.isNotEmpty())
        assertEquals("error", frames.first().phase)
        assertEquals(state, newState)
    }

    @Test
    fun `delete removes head node`() {
        val node1 = LinkedListRunner.LinkedListState(null, 0).head
        var state = LinkedListRunner.LinkedListState(null, 0)

        val (_, state1) = LinkedListRunner.insertAtHead(state, 10)
        state = state1
        val (_, state2) = LinkedListRunner.insertAtHead(state, 20)
        state = state2

        val (frames, newState) = LinkedListRunner.deleteAtHead(state)

        assertTrue(frames.isNotEmpty())
        assertEquals(1, newState.size)
        assertEquals(10, newState.head?.value)
    }

    @Test
    fun `frames contain valid list structures`() {
        var state = LinkedListRunner.LinkedListState(null, 0)
        val (frames1, state1) = LinkedListRunner.insertAtHead(state, 5)
        state = state1

        for (frame in frames1) {
            val scene = frame.scene as ListScene
            assertTrue(scene.nodes.isNotEmpty())
        }
    }

    @Test
    fun `initial frame is correct`() {
        val frame = LinkedListRunner.buildInitial()
        assertEquals("start", frame.phase)
        assertEquals("0", frame.stats["size"])
        val scene = frame.scene as ListScene
        assertTrue(scene.nodes.isEmpty())
    }
}
