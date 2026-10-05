package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NATRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = NATRunner.run()
        assertEquals(9, frames.size, "NATRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = NATRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narr, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testPrivateRangesPresent() {
        val frames = NATRunner.run()
        assert(frames.any { it.phase == "private_ranges" }, "Should explain private IP ranges")
    }

    @Test
    fun testTranslationTablePresent() {
        val frames = NATRunner.run()
        assert(frames.any { it.phase == "translation_table" }, "Should explain translation table")
    }
}
