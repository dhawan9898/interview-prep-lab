package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class OSPFRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = OSPFRunner.run()
        assertEquals(9, frames.size, "OSPFRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = OSPFRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narr, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testLinkStatePresent() {
        val frames = OSPFRunner.run()
        assertTrue(frames.any { it.phase == "link_state" }, "Should explain link-state concept")
    }

    @Test
    fun testSPFPresent() {
        val frames = OSPFRunner.run()
        assertTrue(frames.any { it.phase == "spf" }, "Should explain SPF algorithm")
    }
}
