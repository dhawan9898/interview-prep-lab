package com.interviewpreplab.features.c_programming
import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.*

class UndefinedBehaviorRunnerTest {
    @Test fun testFrameCount() = assertEquals(15, UndefinedBehaviorRunner.run().size)
    @Test fun testAllFramesValid() {
        UndefinedBehaviorRunner.run().forEach { frame ->
            assertTrue(frame.narr.isNotEmpty())
            assertTrue(frame.phase.isNotEmpty())
        }
    }
    @Test fun testMemoryScenes() {
        UndefinedBehaviorRunner.run().forEach { frame ->
            assertTrue(frame.scene is MemoryScene)
        }
    }
    @Test fun testUBExamples() {
        val frames = UndefinedBehaviorRunner.run()
        val narrations = frames.map { it.narr }
        assertTrue(narrations.any { it.contains("overflow") || it.contains("uninitialized") })
    }
}
