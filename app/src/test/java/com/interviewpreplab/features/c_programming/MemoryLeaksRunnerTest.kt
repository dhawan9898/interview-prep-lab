package com.interviewpreplab.features.c_programming
import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.*

class MemoryLeaksRunnerTest {
    @Test fun testFrameCount() = assertEquals(15, MemoryLeaksRunner.run().size)
    @Test fun testAllFramesValid() {
        MemoryLeaksRunner.run().forEach { frame ->
            assertTrue(frame.narr.isNotEmpty())
            assertTrue(frame.phase.isNotEmpty())
        }
    }
    @Test fun testMemoryScenes() {
        MemoryLeaksRunner.run().forEach { frame ->
            assertTrue(frame.scene is MemoryScene)
        }
    }
    @Test fun testLeakAccumulation() {
        val frames = MemoryLeaksRunner.run()
        assertTrue(frames[3].phase.contains("accum") || frames[3].narr.contains("leak"))
    }
}
