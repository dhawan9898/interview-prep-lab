package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class STPRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = STPRunner.run()
        assertEquals(9, frames.size, "STPRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = STPRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narration, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testRootElectionPresent() {
        val frames = STPRunner.run()
        assert(frames.any { it.phase == "root_election" }, "Should explain root bridge election")
    }

    @Test
    fun testBPDUPresent() {
        val frames = STPRunner.run()
        assert(frames.any { it.phase == "bpdu" }, "Should explain BPDU mechanism")
    }
}
