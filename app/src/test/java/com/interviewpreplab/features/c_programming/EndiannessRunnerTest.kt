package com.interviewpreplab.features.c_programming
import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.*

class EndiannessRunnerTest {
    @Test fun testFrameCount() = assertEquals(15, EndiannessRunner.run().size)
    @Test fun testAllFramesValid() {
        EndiannessRunner.run().forEach { frame ->
            assertTrue(frame.narr.isNotEmpty())
            assertTrue(frame.phase.isNotEmpty())
        }
    }
    @Test fun testMemoryScenes() {
        EndiannessRunner.run().forEach { frame ->
            assertTrue(frame.scene is MemoryScene)
        }
    }
    @Test fun testBigAndLittleEndian() {
        val frames = EndiannessRunner.run()
        val phases = frames.map { it.phase }
        assertTrue("big_endian_intro" in phases)
        assertTrue("little_endian_intro" in phases)
    }
}
