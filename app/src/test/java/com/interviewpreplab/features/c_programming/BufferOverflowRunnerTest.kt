package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BufferOverflowRunnerTest {
    @Test
    fun testFrameCount() = assertEquals(15, BufferOverflowRunner.run().size)

    @Test
    fun testPhaseSequence() {
        val phases = BufferOverflowRunner.run().map { it.phase }
        assertEquals("normal_layout", phases[0])
        assertEquals("safe_copy", phases[1])
        assertEquals("overflow_start", phases[2])
        assertEquals("variable_corruption", phases[3])
        assertEquals("return_address_overwrite", phases[4])
        assertEquals("summary", phases.last())
    }

    @Test
    fun testAllFramesValid() {
        BufferOverflowRunner.run().forEach { frame ->
            assertTrue(frame.narr.isNotEmpty())
            assertTrue(frame.phase.isNotEmpty())
        }
    }

    @Test
    fun testMemoryScenes() {
        BufferOverflowRunner.run().forEach { frame ->
            assertTrue(frame.scene is MemoryScene)
        }
    }
}
