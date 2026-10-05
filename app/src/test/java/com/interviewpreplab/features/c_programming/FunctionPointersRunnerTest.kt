package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FunctionPointersRunnerTest {

    @Test
    fun testFrameCount() {
        val frames = FunctionPointersRunner.run()
        assertEquals(15, frames.size, "FunctionPointersRunner should generate 15 frames")
    }

    @Test
    fun testFramesValid() {
        val frames = FunctionPointersRunner.run()
        frames.forEachIndexed { index, frame ->
            assertTrue(frame.narr.isNotEmpty(), "Frame $index narration empty")
            assertTrue(frame.phase.isNotEmpty(), "Frame $index phase empty")
            assertNotNull(frame.scene)
        }
    }

    @Test
    fun testPhaseSequence() {
        val frames = FunctionPointersRunner.run()
        val expectedPhases = listOf(
            "function_location",
            "function_address",
            "function_pointer_type",
            "function_assign",
            "function_call_ptr",
            "function_switch",
            "function_array",
            "array_call",
            "callback_intro",
            "callback_register",
            "callback_execute",
            "type_safety",
            "null_function_ptr",
            "qsort_example",
            "summary"
        )

        frames.forEachIndexed { index, frame ->
            assertEquals(expectedPhases[index], frame.phase)
        }
    }

    @Test
    fun testFunctionLocationFrame() {
        val frames = FunctionPointersRunner.run()
        val locationFrame = frames[0]

        assertEquals("function_location", locationFrame.phase)
        assertTrue(locationFrame.narr.contains("function") && locationFrame.narr.contains("address"))
    }

    @Test
    fun testCallbackFrames() {
        val frames = FunctionPointersRunner.run()

        val callbackIntroFrame = frames[8]
        assertEquals("callback_intro", callbackIntroFrame.phase)
        assertTrue(callbackIntroFrame.narr.contains("callback") || callbackIntroFrame.narr.contains("CALLBACK"))

        val callbackRegisterFrame = frames[9]
        assertEquals("callback_register", callbackRegisterFrame.phase)
    }

    @Test
    fun testMemorySceneStructure() {
        val frames = FunctionPointersRunner.run()
        frames.forEach { frame ->
            val scene = frame.scene as? MemoryScene
            assertNotNull(scene, "Scene should be MemoryScene")
            assertTrue(scene.cells.isNotEmpty(), "Scene should have memory cells")
        }
    }

    @Test
    fun testAddressesHexFormat() {
        val frames = FunctionPointersRunner.run()
        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                assertTrue(cell.address.startsWith("0x"), "Address should be hex: ${cell.address}")
            }
        }
    }

    @Test
    fun testStatisticsPresent() {
        val frames = FunctionPointersRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.stats, "Frame should have statistics")
        }
    }

    @Test
    fun testQsortExample() {
        val frames = FunctionPointersRunner.run()
        val qsortFrame = frames[13]

        assertEquals("qsort_example", qsortFrame.phase)
        assertTrue(qsortFrame.narr.contains("qsort"))
    }

    @Test
    fun testSummaryFrame() {
        val frames = FunctionPointersRunner.run()
        val summaryFrame = frames.last()

        assertEquals("summary", summaryFrame.phase)
        assertTrue(summaryFrame.narr.contains("function"))
    }
}
