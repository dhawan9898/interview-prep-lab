package com.interviewpreplab.features.sorts

import com.interviewpreplab.core.model.BarsScene
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BubbleSortRunnerTest {
    @Test
    fun `run generates frames for valid array`() {
        val input = listOf(5, 2, 8, 1, 9)
        val frames = BubbleSortRunner.run(input)

        // Should generate frames (start + comparisons/swaps + passes + done)
        assertTrue(frames.isNotEmpty())
        assertEquals("start", frames.first().phase)
        assertEquals("done", frames.last().phase)
    }

    @Test
    fun `final frame contains sorted array`() {
        val input = listOf(5, 2, 8, 1, 9)
        val frames = BubbleSortRunner.run(input)
        val lastScene = frames.last().scene as BarsScene

        val values = lastScene.bars.map { it.value }
        assertEquals(listOf(1, 2, 5, 8, 9), values)
    }

    @Test
    fun `all frames have valid scene data`() {
        val input = listOf(5, 2, 8, 1, 9)
        val frames = BubbleSortRunner.run(input)

        for (frame in frames) {
            val scene = frame.scene as BarsScene
            assertEquals(input.size, scene.bars.size, "Frame ${frame.phase} has wrong bar count")
            assertNotNull(frame.narr, "Frame ${frame.phase} missing narration")
        }
    }

    @Test
    fun `single element array is handled`() {
        val input = listOf(42)
        val frames = BubbleSortRunner.run(input)
        assertTrue(frames.isNotEmpty())
    }

    @Test
    fun `already sorted array is handled`() {
        val input = listOf(1, 2, 3, 4, 5)
        val frames = BubbleSortRunner.run(input)
        assertEquals(listOf(1, 2, 3, 4, 5), (frames.last().scene as BarsScene).bars.map { it.value })
    }

    @Test
    fun `stats are accumulated correctly`() {
        val input = listOf(5, 2, 8)
        val frames = BubbleSortRunner.run(input)

        // Last frame should have final stats
        val lastStats = frames.last().stats
        assertTrue(lastStats.containsKey("comparisons"))
        assertTrue(lastStats.containsKey("swaps"))
        assertTrue(lastStats["comparisons"]?.toIntOrNull() ?: 0 > 0)
    }
}
