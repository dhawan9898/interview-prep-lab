package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DHCPLeaseRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = DHCPLeaseRunner.run()
        assertEquals(9, frames.size, "DHCPLeaseRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = DHCPLeaseRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narration, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testDiscoverPresent() {
        val frames = DHCPLeaseRunner.run()
        assert(frames.any { it.phase == "step1_discover" }, "Should explain DHCP Discover")
    }

    @Test
    fun testLeasePresent() {
        val frames = DHCPLeaseRunner.run()
        assert(frames.any { it.phase == "lease" }, "Should explain lease mechanism")
    }
}
