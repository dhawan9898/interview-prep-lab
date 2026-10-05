package com.interviewpreplab.features.c_programming
import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.*

class UseAfterFreeRunnerTest {
    @Test fun testFrameCount() = assertEquals(15, UseAfterFreeRunner.run().size)
    @Test fun testPhaseSequence() {
        val phases = UseAfterFreeRunner.run().map { it.phase }
        assertEquals("allocation", phases[0])
        assertEquals("valid_use", phases[1])
        assertEquals("deallocation", phases[2])
        assertEquals("dangling_pointer", phases[3])
        assertEquals("summary", phases.last())
    }
    @Test fun testAllFramesValid() {
        UseAfterFreeRunner.run().forEach { frame ->
            assertTrue(frame.narr.isNotEmpty())
            assertTrue(frame.phase.isNotEmpty())
        }
    }
    @Test fun testMemoryScenes() {
        UseAfterFreeRunner.run().forEach { frame ->
            assertTrue(frame.scene is MemoryScene)
        }
    }
    @Test fun testDanglingPointerFrame() {
        val frames = UseAfterFreeRunner.run()
        assertTrue(frames[3].narr.contains("freed") || frames[3].narr.contains("dangling"))
    }
}
