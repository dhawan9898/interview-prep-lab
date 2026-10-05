package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VLANRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = VLANRunner.run()
        assertEquals(9, frames.size, "VLANRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = VLANRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narr, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testTagFormatPresent() {
        val frames = VLANRunner.run()
        assertTrue(frames.any { it.phase == "tag_format" }, "Should explain 802.1Q tag format")
    }

    @Test
    fun testAccessPortPresent() {
        val frames = VLANRunner.run()
        assertTrue(frames.any { it.phase == "access_port" }, "Should explain access ports")
    }
}
