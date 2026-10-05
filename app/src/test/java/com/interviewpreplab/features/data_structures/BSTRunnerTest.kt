package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.TreeScene
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BSTRunnerTest {
    @Test
    fun `insert into empty tree creates root`() {
        val state = BSTRunner.BSTState(null, 0)
        val (frames, newState) = BSTRunner.insert(state, 50)

        val root = assertNotNull(newState.root)
        assertEquals(50, root.value)
        assertEquals(1, newState.size)
    }

    @Test
    fun `insert maintains BST property`() {
        var state = BSTRunner.BSTState(null, 0)

        val (f1, s1) = BSTRunner.insert(state, 50)
        state = s1

        val (f2, s2) = BSTRunner.insert(state, 30)
        state = s2

        val (f3, s3) = BSTRunner.insert(state, 70)
        state = s3

        // Verify BST property: left < parent < right
        assertEquals(50, state.root?.value)
        assertEquals(30, state.root?.left?.value)
        assertEquals(70, state.root?.right?.value)
    }

    @Test
    fun `search finds inserted value`() {
        var state = BSTRunner.BSTState(null, 0)

        val (f1, s1) = BSTRunner.insert(state, 50)
        state = s1
        val (f2, s2) = BSTRunner.insert(state, 30)
        state = s2
        val (f3, s3) = BSTRunner.insert(state, 70)
        state = s3

        val (searchFrames, _) = BSTRunner.search(state, 30)

        assertEquals("search-found", searchFrames.last().phase)
    }

    @Test
    fun `search reports not found for missing value`() {
        var state = BSTRunner.BSTState(null, 0)

        val (f1, s1) = BSTRunner.insert(state, 50)
        state = s1

        val (searchFrames, _) = BSTRunner.search(state, 99)

        assertEquals("search-not-found", searchFrames.last().phase)
    }

    @Test
    fun `frames contain valid tree scene data`() {
        var state = BSTRunner.BSTState(null, 0)

        val (frames1, s1) = BSTRunner.insert(state, 50)
        state = s1

        val (frames2, s2) = BSTRunner.insert(state, 30)
        state = s2

        for (frame in frames1 + frames2) {
            val scene = frame.scene as TreeScene
            // Root should exist after insertion
            if (frame.phase.contains("done") || frame.phase.contains("root")) {
                assertNotNull(scene.root)
            }
        }
    }

    @Test
    fun `initial frame is correct`() {
        val frame = BSTRunner.buildInitial()
        assertEquals("start", frame.phase)
        assertEquals("0", frame.stats["size"])
        val scene = frame.scene as TreeScene
        assertEquals(null, scene.root)
    }
}
