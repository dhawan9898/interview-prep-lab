package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PointersRunnerTest {

    @Test
    fun testFrameCount() {
        val frames = PointersRunner.run()
        assertEquals(10, frames.size, "PointersRunner should generate 10 frames")
    }

    @Test
    fun testAllFramesHaveContent() {
        val frames = PointersRunner.run()
        frames.forEachIndexed { index, frame ->
            assertTrue(frame.narr.isNotEmpty(), "Frame $index: narration should not be empty")
            assertTrue(frame.phase.isNotEmpty(), "Frame $index: phase should not be empty")
            assertNotNull(frame.scene, "Frame $index: scene should not be null")
        }
    }

    @Test
    fun testPhaseProgression() {
        val frames = PointersRunner.run()
        val expectedPhases = listOf(
            "variable_intro",
            "address_of",
            "pointer_declaration",
            "pointer_assignment",
            "dereference",
            "modify_through_pointer",
            "multiple_pointers",
            "pointer_to_pointer",
            "null_pointer",
            "summary"
        )

        frames.forEachIndexed { index, frame ->
            assertEquals(expectedPhases[index], frame.phase)
        }
    }

    @Test
    fun testPointerAssignmentFrame() {
        val frames = PointersRunner.run()
        val assignmentFrame = frames[3] // pointer_assignment frame

        assertEquals("pointer_assignment", assignmentFrame.phase)
        assertTrue(assignmentFrame.narr.contains("ptr = &x"))

        val scene = assignmentFrame.scene as MemoryScene
        // Should have pointer relationship
        assertTrue(scene.pointers.isNotEmpty(), "Assignment frame should show pointer relationships")
        assertEquals(1, scene.pointers.size, "Should have one pointer relationship")
    }

    @Test
    fun testDereferenceFrame() {
        val frames = PointersRunner.run()
        val derefFrame = frames[4] // dereference frame

        assertEquals("dereference", derefFrame.phase)
        assertTrue(derefFrame.narr.contains("*ptr"))
        assertTrue(derefFrame.stats.containsKey("dereferenced"))
    }

    @Test
    fun testPointerToPointerFrame() {
        val frames = PointersRunner.run()
        val ptrToPtrFrame = frames[7] // pointer_to_pointer frame

        assertEquals("pointer_to_pointer", ptrToPtrFrame.phase)
        assertTrue(ptrToPtrFrame.narr.contains("int**"))

        val scene = ptrToPtrFrame.scene as MemoryScene
        assertTrue(scene.pointers.size >= 2, "Should have multiple pointer relationships")
    }

    @Test
    fun testNullPointerFrame() {
        val frames = PointersRunner.run()
        val nullFrame = frames[8] // null_pointer frame

        assertEquals("null_pointer", nullFrame.phase)
        assertTrue(nullFrame.narr.contains("NULL") || nullFrame.narr.contains("crash"))

        val scene = nullFrame.scene as MemoryScene
        // Should mention dangerous NULL access
        assertTrue(scene.legend.contains("NULL") || scene.legend.contains("dangerous"))
    }

    @Test
    fun testMemoryCellConsistency() {
        val frames = PointersRunner.run()

        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                // All addresses should be hexadecimal
                assertTrue(cell.address.startsWith("0x"), "Address should be hex: ${cell.address}")
                // All should have values and labels
                assertNotNull(cell.label)
                assertNotNull(cell.value)
            }
        }
    }

    @Test
    fun testPointerFrameHasStats() {
        val frames = PointersRunner.run()

        frames.forEach { frame ->
            assertNotNull(frame.stats, "Frame should have statistics")
            // Stats should contain meaningful data about pointers
            if (frame.phase.contains("pointer")) {
                assertTrue(
                    frame.stats.isNotEmpty(),
                    "Pointer frame should have non-empty stats"
                )
            }
        }
    }

    @Test
    fun testAddressValuesRealistic() {
        val frames = PointersRunner.run()
        val scene = frames[0].scene as MemoryScene

        // First variable should be on stack
        val firstCell = scene.cells[0]
        assertTrue(firstCell.address.startsWith("0x7FFF"))

        // Value should be reasonable
        assertEquals("42", firstCell.value)
    }

    @Test
    fun testPointerAddressDifferences() {
        val frames = PointersRunner.run()
        val assignmentFrame = frames[3]
        val scene = assignmentFrame.scene as MemoryScene

        // Get the pointer cell
        val ptrCell = scene.cells.find { "ptr" in it.label }
        assertNotNull(ptrCell, "Should have pointer cell")

        // Pointer value should be an address
        assertTrue(ptrCell.value.startsWith("0x"), "Pointer value should be an address")
    }

    @Test
    fun testRoleAssignments() {
        val frames = PointersRunner.run()

        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                // Check that roles make sense
                if ("pointer" in cell.roles) {
                    // Pointer cells should contain addresses
                    assertTrue(cell.value.startsWith("0x"))
                }
            }
        }
    }

    @Test
    fun testSummaryFrame() {
        val frames = PointersRunner.run()
        val summaryFrame = frames.last()

        assertEquals("summary", summaryFrame.phase)
        assertTrue(summaryFrame.narr.contains("&") && summaryFrame.narr.contains("*"))
    }

    @Test
    fun testFrameProgression() {
        val frames = PointersRunner.run()

        // Should start with simple variable
        assertTrue(frames[0].narr.contains("variable"))

        // Should progress to pointers
        assertTrue(frames[3].narr.contains("ptr ="))

        // Should end with summary
        assertTrue(frames.last().phase == "summary")
    }
}
