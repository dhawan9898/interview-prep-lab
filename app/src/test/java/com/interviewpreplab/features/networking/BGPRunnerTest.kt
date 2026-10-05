package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BGPRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = BGPRunner.run()
        assertEquals(9, frames.size, "BGPRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = BGPRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narr, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testASPresent() {
        val frames = BGPRunner.run()
        assert(frames.any { it.phase == "as" }, "Should explain autonomous systems")
    }

    @Test
    fun testBestPathPresent() {
        val frames = BGPRunner.run()
        assert(frames.any { it.phase == "best_path" }, "Should explain best path selection")
    }
}
