package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MACLearningRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = MACLearningRunner.run()
        assertEquals(9, frames.size, "MACLearningRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = MACLearningRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narr, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testLearningPresent() {
        val frames = MACLearningRunner.run()
        assertTrue(frames.any { it.phase == "step2_learning" }, "Should explain MAC learning")
    }

    @Test
    fun testForwardingPresent() {
        val frames = MACLearningRunner.run()
        assertTrue(frames.any { it.phase == "step4_forward" }, "Should explain frame forwarding")
    }
}
