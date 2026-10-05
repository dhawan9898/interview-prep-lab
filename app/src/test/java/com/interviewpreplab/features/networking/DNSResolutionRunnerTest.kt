package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DNSResolutionRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = DNSResolutionRunner.run()
        assertEquals(9, frames.size, "DNSResolutionRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = DNSResolutionRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narr, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testHierarchyPresent() {
        val frames = DNSResolutionRunner.run()
        assert(frames.any { it.phase.contains("step") }, "Should explain DNS resolution steps")
    }

    @Test
    fun testRecordTypesPresent() {
        val frames = DNSResolutionRunner.run()
        assert(frames.any { it.phase == "record_types" }, "Should explain DNS record types")
    }
}
