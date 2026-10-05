package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.SlotsScene
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StackRunnerTest {
    @Test
    fun `push generates frames`() {
        val state = StackRunner.StackState(emptyList(), -1)
        val (frames, newState) = StackRunner.runPush(state, 42)

        assertTrue(frames.isNotEmpty())
        assertEquals(1, newState.items.size)
        assertEquals(42, newState.items[0])
        assertEquals(0, newState.top)
    }

    @Test
    fun `multiple pushes work`() {
        var state = StackRunner.StackState(emptyList(), -1)

        val (frames1, state1) = StackRunner.runPush(state, 10)
        state = state1
        assertEquals(1, state.items.size)
        assertEquals(0, state.top)

        val (frames2, state2) = StackRunner.runPush(state, 20)
        state = state2
        assertEquals(2, state.items.size)
        assertEquals(1, state.top)
        assertEquals(listOf(10, 20), state.items)
    }

    @Test
    fun `pop retrieves last item (LIFO)`() {
        val state = StackRunner.StackState(listOf(10, 20, 30), 2)
        val (frames, newState) = StackRunner.runPop(state)

        assertTrue(frames.isNotEmpty())
        assertEquals(2, newState.items.size)
        assertEquals(listOf(10, 20), newState.items)
        assertEquals(1, newState.top)
    }

    @Test
    fun `pop from empty stack returns error frame`() {
        val state = StackRunner.StackState(emptyList(), -1)
        val (frames, newState) = StackRunner.runPop(state)

        assertTrue(frames.isNotEmpty())
        assertEquals("error", frames.first().phase)
        assertEquals(state, newState) // state unchanged
    }

    @Test
    fun `push to full stack returns error frame`() {
        val fullStack = StackRunner.StackState((1..8).toList(), 7)
        val (frames, newState) = StackRunner.runPush(fullStack, 99)

        assertTrue(frames.isNotEmpty())
        assertEquals("error", frames.first().phase)
        assertEquals(fullStack, newState) // state unchanged
    }

    @Test
    fun `frames contain valid scene data`() {
        val state = StackRunner.StackState(listOf(5, 10), 1)
        val (frames, _) = StackRunner.runPush(state, 15)

        for (frame in frames) {
            val scene = frame.scene as SlotsScene
            assertTrue(scene.slots.isNotEmpty())
            assertEquals(frame.stats["size"]?.toIntOrNull(), scene.slots.size)
        }
    }

    @Test
    fun `initial frame is created correctly`() {
        val frame = StackRunner.buildInitial()
        assertEquals("start", frame.phase)
        assertEquals("0", frame.stats["size"])
        val scene = frame.scene as SlotsScene
        assertTrue(scene.slots.isEmpty())
    }
}
