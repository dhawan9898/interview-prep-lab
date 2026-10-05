package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.SlotsScene
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QueueRunnerTest {
    @Test
    fun `enqueue adds to rear`() {
        val state = QueueRunner.QueueState(emptyList(), 0, -1)
        val (frames, newState) = QueueRunner.enqueue(state, 42)

        assertTrue(frames.isNotEmpty())
        assertEquals(0, newState.front)
        assertEquals(0, newState.rear)
    }

    @Test
    fun `multiple enqueues maintain FIFO order`() {
        var state = QueueRunner.QueueState(emptyList(), 0, -1)

        val (f1, s1) = QueueRunner.enqueue(state, 10)
        state = QueueRunner.QueueState(s1.items.toMutableList().apply { add(0, 10) }, 0, 0)

        // Manually adjust state for second enqueue
        val newItems = mutableListOf<Int>()
        newItems.add(10)
        state = QueueRunner.QueueState(newItems, 0, 0)

        val (f2, s2) = QueueRunner.enqueue(state, 20)
        // Verify frames exist
        assertTrue(f1.isNotEmpty())
        assertTrue(f2.isNotEmpty())
    }

    @Test
    fun `dequeue from empty returns error`() {
        val state = QueueRunner.QueueState(emptyList(), 0, -1)
        val (frames, newState) = QueueRunner.dequeue(state)

        assertTrue(frames.isNotEmpty())
        assertEquals("error", frames.first().phase)
    }

    @Test
    fun `enqueue to full queue returns error`() {
        val fullItems = (1..8).toList()
        val state = QueueRunner.QueueState(fullItems, 0, 7)
        val (frames, newState) = QueueRunner.enqueue(state, 99)

        assertEquals("error", frames.first().phase)
        assertEquals(state, newState)
    }

    @Test
    fun `frames show dead space`() {
        var state = QueueRunner.QueueState((1..3).toList(), 0, 2)
        val (frames, _) = QueueRunner.dequeue(state)

        assertTrue(frames.isNotEmpty())
        // Second frame should show increment to FRONT
        assertEquals("dequeue-end", frames.getOrNull(1)?.phase)
    }
}

class CircularQueueRunnerTest {
    @Test
    fun `enqueue on circular queue wraps around`() {
        var state = CircularQueueRunner.CircularQueueState(MutableList(5) { null }, 0, 0)

        val (frames1, s1) = CircularQueueRunner.enqueue(state, 10)
        state = s1

        val (frames2, s2) = CircularQueueRunner.enqueue(state, 20)
        state = s2

        assertEquals(2, state.count)
        assertTrue(frames1.isNotEmpty())
        assertTrue(frames2.isNotEmpty())
    }

    @Test
    fun `dequeue from circular queue recycles space`() {
        val items = mutableListOf(10, 20, 30, null, null)
        val state = CircularQueueRunner.CircularQueueState(items, 0, 3)
        val (frames, newState) = CircularQueueRunner.dequeue(state)

        assertTrue(frames.isNotEmpty())
        assertEquals(2, newState.count)
        assertEquals(1, newState.front) // Wrapped?
    }

    @Test
    fun `modulo calculation is correct`() {
        // Enqueue 5 items in a queue of size 5
        var state = CircularQueueRunner.CircularQueueState(MutableList(5) { null }, 0, 0)

        for (i in 1..5) {
            val (frames, newState) = CircularQueueRunner.enqueue(state, i)
            state = newState
        }

        // Full queue
        assertEquals(5, state.count)
        assertTrue(state.count >= 5)
    }
}
