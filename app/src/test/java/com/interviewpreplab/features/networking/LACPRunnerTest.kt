package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class LACPRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = LACPRunner.run()
        assertEquals(9, frames.size, "LACPRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = LACPRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narration, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testBundlePresent() {
        val frames = LACPRunner.run()
        assert(frames.any { it.phase == "bundle" }, "Should explain link bundling")
    }

    @Test
    fun testLoadBalancingPresent() {
        val frames = LACPRunner.run()
        assert(frames.any { it.phase == "load_balancing" }, "Should explain load balancing")
    }
}
