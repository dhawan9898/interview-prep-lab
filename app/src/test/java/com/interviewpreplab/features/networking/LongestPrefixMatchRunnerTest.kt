package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LongestPrefixMatchRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = LongestPrefixMatchRunner.run()
        assertEquals(9, frames.size, "LongestPrefixMatchRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = LongestPrefixMatchRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narration, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testAlgorithmPresent() {
        val frames = LongestPrefixMatchRunner.run()
        assert(frames.any { it.stats.containsKey("algorithm") }, "Should explain the algorithm")
    }
}
