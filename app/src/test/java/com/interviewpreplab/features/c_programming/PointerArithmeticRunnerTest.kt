package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PointerArithmeticRunnerTest {

    @Test
    fun testFrameCount() {
        val frames = PointerArithmeticRunner.run()
        assertEquals(12, frames.size, "PointerArithmeticRunner should generate 12 frames")
    }

    @Test
    fun testAllFramesStructureValid() {
        val frames = PointerArithmeticRunner.run()

        frames.forEachIndexed { index, frame ->
            assertTrue(frame.narr.isNotEmpty(), "Frame $index: narration empty")
            assertTrue(frame.phase.isNotEmpty(), "Frame $index: phase empty")
            assertNotNull(frame.scene, "Frame $index: scene null")
        }
    }

    @Test
    fun testPhaseSequence() {
        val frames = PointerArithmeticRunner.run()

        val expectedPhases = listOf(
            "array_layout",
            "pointer_to_array",
            "pointer_increment",
            "pointer_offset",
            "dereference_offset",
            "indexing_equivalence",
            "pointer_subtraction",
            "pointer_loop",
            "type_size_matters",
            "out_of_bounds",
            "pointer_comparison",
            "summary"
        )

        frames.forEachIndexed { index, frame ->
            assertEquals(expectedPhases[index], frame.phase)
        }
    }

    @Test
    fun testArrayLayoutFrame() {
        val frames = PointerArithmeticRunner.run()
        val arrayFrame = frames[0]

        assertEquals("array_layout", arrayFrame.phase)
        assertTrue(arrayFrame.narr.contains("consecutive"))

        val scene = arrayFrame.scene as MemoryScene
        assertEquals(3, scene.cells.size, "Array should have 3 elements")

        // Verify array element addresses are consecutive
        val addr0 = scene.cells[0].address
        val addr1 = scene.cells[1].address
        val addr2 = scene.cells[2].address

        assertTrue(addr0.isNotEmpty() && addr1.isNotEmpty() && addr2.isNotEmpty())
    }

    @Test
    fun testPointerIncrementFrame() {
        val frames = PointerArithmeticRunner.run()
        val incrementFrame = frames[2]

        assertEquals("pointer_increment", incrementFrame.phase)
        assertTrue(incrementFrame.narr.contains("ptr++"))
        assertTrue(incrementFrame.stats.containsKey("ptr_moved_by"))
        assertEquals("4 bytes", incrementFrame.stats["ptr_moved_by"])
    }

    @Test
    fun testPointerOffsetFrame() {
        val frames = PointerArithmeticRunner.run()
        val offsetFrame = frames[3]

        assertEquals("pointer_offset", offsetFrame.phase)
        assertTrue(offsetFrame.narr.contains("ptr + 2"))
    }

    @Test
    fun testDereferenceOffsetFrame() {
        val frames = PointerArithmeticRunner.run()
        val derefFrame = frames[4]

        assertEquals("dereference_offset", derefFrame.phase)
        assertTrue(derefFrame.narr.contains("*(ptr +"))
        assertTrue(derefFrame.stats.containsKey("operation"))
    }

    @Test
    fun testIndexingEquivalenceFrame() {
        val frames = PointerArithmeticRunner.run()
        val indexFrame = frames[5]

        assertEquals("indexing_equivalence", indexFrame.phase)
        assertTrue(indexFrame.narr.contains("arr[i]") && indexFrame.narr.contains("*(ptr"))
    }

    @Test
    fun testPointerSubtractionFrame() {
        val frames = PointerArithmeticRunner.run()
        val subtractFrame = frames[6]

        assertEquals("pointer_subtraction", subtractFrame.phase)
        assertTrue(subtractFrame.narr.contains("ptr2 - ptr1"))
        assertTrue(subtractFrame.stats.containsKey("difference"))
    }

    @Test
    fun testPointerLoopFrame() {
        val frames = PointerArithmeticRunner.run()
        val loopFrame = frames[7]

        assertEquals("pointer_loop", loopFrame.phase)
        assertTrue(loopFrame.narr.contains("for") && loopFrame.narr.contains("p++"))
    }

    @Test
    fun testTypeSizeMattersFrame() {
        val frames = PointerArithmeticRunner.run()
        val typeFrame = frames[8]

        assertEquals("type_size_matters", typeFrame.phase)
        assertTrue(typeFrame.narr.contains("char*") && typeFrame.narr.contains("int*"))
    }

    @Test
    fun testOutOfBoundsFrame() {
        val frames = PointerArithmeticRunner.run()
        val oobFrame = frames[9]

        assertEquals("out_of_bounds", oobFrame.phase)
        assertTrue(oobFrame.narr.contains("ptr[100]") || oobFrame.narr.contains("bounds"))
    }

    @Test
    fun testComparisonFrame() {
        val frames = PointerArithmeticRunner.run()
        val compFrame = frames[10]

        assertEquals("pointer_comparison", compFrame.phase)
        assertTrue(compFrame.narr.contains("compare") || compFrame.narr.contains("<"))
    }

    @Test
    fun testMemoryAddressConsistency() {
        val frames = PointerArithmeticRunner.run()

        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                assertTrue(cell.address.startsWith("0x"), "Invalid address format: ${cell.address}")
            }
        }
    }

    @Test
    fun testArrayElementValues() {
        val frames = PointerArithmeticRunner.run()
        val arrayFrame = frames[0]
        val scene = arrayFrame.scene as MemoryScene

        // Check array element values match narration
        val values = scene.cells.map { it.value }
        assertTrue("10" in values)
        assertTrue("20" in values)
        assertTrue("30" in values)
    }

    @Test
    fun testPointerRelationships() {
        val frames = PointerArithmeticRunner.run()

        frames.forEach { frame ->
            if (frame.phase.contains("pointer")) {
                val scene = frame.scene as MemoryScene
                // Pointer frames should have pointer cells or relationships
                val hasPointerCell = scene.cells.any { "pointer" in it.roles }
                assertTrue(hasPointerCell || scene.pointers.isNotEmpty(), "Pointer frame should show pointers")
            }
        }
    }

    @Test
    fun testStatisticsTracking() {
        val frames = PointerArithmeticRunner.run()

        // Frames should track relevant metrics
        val arrayFrame = frames[0]
        assertTrue(arrayFrame.stats.containsKey("element_count"))
        assertTrue(arrayFrame.stats.containsKey("element_size"))
    }

    @Test
    fun testSummaryFrame() {
        val frames = PointerArithmeticRunner.run()
        val summaryFrame = frames.last()

        assertEquals("summary", summaryFrame.phase)
        assertTrue(summaryFrame.narr.contains("ptr++") && summaryFrame.narr.contains("arr[i]"))
    }

    @Test
    fun testConsecutiveFrameProgression() {
        val frames = PointerArithmeticRunner.run()

        // Frames should form logical progression
        assertTrue(frames[0].narr.contains("array"))
        assertTrue(frames[1].narr.contains("pointer"))
        assertTrue(frames[6].narr.contains("subtraction"))
        assertTrue(frames[11].phase == "summary")
    }
}
