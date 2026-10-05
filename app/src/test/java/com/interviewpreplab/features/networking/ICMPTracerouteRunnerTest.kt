package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ICMPTracerouteRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = ICMPTracerouteRunner.run()
        assertEquals(9, frames.size, "ICMPTracerouteRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = ICMPTracerouteRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narration, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testPingFramePresent() {
        val frames = ICMPTracerouteRunner.run()
        assert(frames.any { it.phase == "ping" }, "Should have ping frame")
    }

    @Test
    fun testTracerouteMethodPresent() {
        val frames = ICMPTracerouteRunner.run()
        assert(frames.any { it.phase == "traceroute_method" }, "Should explain traceroute method")
    }
}
