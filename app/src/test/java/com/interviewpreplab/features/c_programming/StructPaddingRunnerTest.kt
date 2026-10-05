package com.interviewpreplab.features.c_programming
import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.*

class StructPaddingRunnerTest {
    @Test fun testFrameCount() = assertEquals(15, StructPaddingRunner.run().size)
    @Test fun testAllFramesValid() {
        StructPaddingRunner.run().forEach { frame ->
            assertTrue(frame.narr.isNotEmpty())
            assertTrue(frame.phase.isNotEmpty())
        }
    }
    @Test fun testMemoryScenes() {
        StructPaddingRunner.run().forEach { frame ->
            assertTrue(frame.scene is MemoryScene)
        }
    }
}
