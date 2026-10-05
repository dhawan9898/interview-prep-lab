package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class IPv6HeaderRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = IPv6HeaderRunner.run()
        assertEquals(9, frames.size, "IPv6HeaderRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = IPv6HeaderRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narr, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
            assertNotNull(frame.scene, "Frame scene should not be null")
        }
    }

    @Test
    fun testFinalFrameContent() {
        val frames = IPv6HeaderRunner.run()
        val lastFrame = frames.last()
        assertEquals("summary", lastFrame.phase, "Last frame should be summary phase")
        assertTrue(lastFrame.stats.containsKey("size"), "Last frame should contain size stat")
    }

    @Test
    fun testPhaseSequence() {
        val frames = IPv6HeaderRunner.run()
        assertEquals("intro", frames[0].phase, "First frame should be intro")
        assertEquals("header_start", frames[1].phase, "Second frame should be header_start")
    }
}
