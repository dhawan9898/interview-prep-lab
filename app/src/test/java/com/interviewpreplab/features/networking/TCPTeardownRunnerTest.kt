package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TCPTeardownRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = TCPTeardownRunner.run()
        assertEquals(9, frames.size, "TCPTeardownRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = TCPTeardownRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narr, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testFINPresent() {
        val frames = TCPTeardownRunner.run()
        assert(frames.any { it.phase.contains("fin") }, "Should explain FIN flag")
    }

    @Test
    fun testHalfClosePresent() {
        val frames = TCPTeardownRunner.run()
        assert(frames.any { it.phase == "half_close" }, "Should explain half-close state")
    }
}
