package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SubnettingRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = SubnettingRunner.run()
        assertEquals(9, frames.size, "SubnettingRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = SubnettingRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narr, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testFirstFrameContent() {
        val frames = SubnettingRunner.run()
        val firstFrame = frames[0]
        assertEquals("intro", firstFrame.phase, "First frame should be intro phase")
        assertTrue(firstFrame.stats.containsKey("notation"), "Should mention CIDR notation")
    }

    @Test
    fun testCIDRFramePresent() {
        val frames = SubnettingRunner.run()
        assertTrue(frames.any { it.phase == "cidr" }, "Should have CIDR notation frame")
    }
}
