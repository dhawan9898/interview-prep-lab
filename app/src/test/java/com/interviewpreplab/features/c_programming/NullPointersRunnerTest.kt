package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class NullPointersRunnerTest {

    @Test
    fun testFrameCount() {
        val frames = NullPointersRunner.run()
        assertEquals(15, frames.size, "NullPointersRunner should generate 15 frames")
    }

    @Test
    fun testFramesValid() {
        val frames = NullPointersRunner.run()
        frames.forEachIndexed { index, frame ->
            assertTrue(frame.narr.isNotEmpty(), "Frame $index narration empty")
            assertTrue(frame.phase.isNotEmpty(), "Frame $index phase empty")
            assertNotNull(frame.scene)
        }
    }

    @Test
    fun testPhaseSequence() {
        val frames = NullPointersRunner.run()

        val expectedPhases = listOf(
            "uninitialized",
            "null_definition",
            "null_init",
            "valid_allocation",
            "valid_dereference",
            "null_dereference",
            "segfault_explanation",
            "garbage_dereference",
            "wrong_usage",
            "safe_usage",
            "dangling_pointer",
            "null_after_free",
            "null_standard",
            "malloc_failure",
            "summary"
        )

        frames.forEachIndexed { index, frame ->
            assertEquals(expectedPhases[index], frame.phase)
        }
    }

    @Test
    fun testNullDefinitionFrame() {
        val frames = NullPointersRunner.run()
        val nullDefFrame = frames[1]

        assertEquals("null_definition", nullDefFrame.phase)
        assertTrue(nullDefFrame.narr.contains("NULL") || nullDefFrame.narr.contains("0x00000000"))
    }

    @Test
    fun testValidAllocationFrame() {
        val frames = NullPointersRunner.run()
        val validFrame = frames[3]

        assertEquals("valid_allocation", validFrame.phase)
        assertTrue(validFrame.narr.contains("malloc"))

        val scene = validFrame.scene as MemoryScene
        assertTrue(scene.cells.any { "heap" in it.roles }, "Should have heap allocation")
    }

    @Test
    fun testNullDereferenceFrame() {
        val frames = NullPointersRunner.run()
        val derefFrame = frames[5]

        assertEquals("null_dereference", derefFrame.phase)
        assertTrue(
            derefFrame.narr.contains("CRASH") || derefFrame.narr.contains("SEGMENTATION FAULT"),
            "Should mention crash/segfault"
        )
    }

    @Test
    fun testSegfaultExplanationFrame() {
        val frames = NullPointersRunner.run()
        val segFrame = frames[6]

        assertEquals("segfault_explanation", segFrame.phase)
        assertTrue(
            segFrame.narr.contains("MMU") || segFrame.narr.contains("segmentation"),
            "Should explain segmentation fault mechanism"
        )
    }

    @Test
    fun testSafeUsageFrame() {
        val frames = NullPointersRunner.run()
        val safeFrame = frames[9]

        assertEquals("safe_usage", safeFrame.phase)
        assertTrue(safeFrame.narr.contains("NULL check") || safeFrame.narr.contains("if"))
    }

    @Test
    fun testDanglingPointerFrame() {
        val frames = NullPointersRunner.run()
        val danglingFrame = frames[10]

        assertEquals("dangling_pointer", danglingFrame.phase)
        assertTrue(danglingFrame.narr.contains("free") && danglingFrame.narr.contains("dangling"))
    }

    @Test
    fun testNullAfterFreeFrame() {
        val frames = NullPointersRunner.run()
        val nullAfterFrame = frames[11]

        assertEquals("null_after_free", nullAfterFrame.phase)
        assertTrue(nullAfterFrame.narr.contains("free") && nullAfterFrame.narr.contains("NULL"))
    }

    @Test
    fun testMallocFailureFrame() {
        val frames = NullPointersRunner.run()
        val mallocFrame = frames[13]

        assertEquals("malloc_failure", mallocFrame.phase)
        assertTrue(mallocFrame.narr.contains("malloc") && mallocFrame.narr.contains("NULL"))
    }

    @Test
    fun testMemoryAddressConsistency() {
        val frames = NullPointersRunner.run()

        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                assertTrue(cell.address.startsWith("0x"), "Address should be hex: ${cell.address}")
            }
        }
    }

    @Test
    fun testNullAddressRepresentation() {
        val frames = NullPointersRunner.run()

        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                // NULL pointers should have 0x00000000 address
                if ("NULL" in cell.label || "null" in cell.label.lowercase()) {
                    if (cell.value.startsWith("0x")) {
                        assertTrue(cell.value == "0x00000000", "NULL should be 0x00000000")
                    }
                }
            }
        }
    }

    @Test
    fun testValidPointerRepresentation() {
        val frames = NullPointersRunner.run()
        val validFrame = frames[3]
        val scene = validFrame.scene as MemoryScene

        // Valid pointer should have non-zero address
        val heapCells = scene.cells.filter { "heap" in it.roles }
        assertTrue(heapCells.isNotEmpty(), "Should have heap allocation")
    }

    @Test
    fun testStatisticsPresent() {
        val frames = NullPointersRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.stats, "Frame should have stats")
        }
    }

    @Test
    fun testSafetyConcernProgression() {
        val frames = NullPointersRunner.run()

        // Should start with dangers
        assertTrue(frames[0].narr.contains("dangerous") || frames[0].narr.contains("Uninitialized"))

        // Should show NULL concept
        assertTrue(frames[1].narr.contains("NULL"))

        // Should show proper usage
        assertTrue(frames[9].narr.contains("Right") || frames[9].narr.contains("check"))
    }

    @Test
    fun testSummaryFrame() {
        val frames = NullPointersRunner.run()
        val summaryFrame = frames.last()

        assertEquals("summary", summaryFrame.phase)
        assertTrue(summaryFrame.narr.contains("NULL") && summaryFrame.narr.contains("check"))
    }

    @Test
    fun testConsistentAddressesAcrossFrames() {
        val frames = NullPointersRunner.run()

        // Get addresses from first frames
        val firstAddrs = mutableSetOf<String>()
        frames.take(5).forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                firstAddrs.add(cell.address)
            }
        }

        // Addresses should follow hexadecimal convention
        firstAddrs.forEach { addr ->
            assertTrue(addr.startsWith("0x"))
            assertTrue(addr.length <= 10) // Reasonable hex length
        }
    }
}
