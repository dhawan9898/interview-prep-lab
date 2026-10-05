package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MemoryLayoutRunnerTest {

    @Test
    fun testFrameCount() {
        val frames = MemoryLayoutRunner.run()
        assertEquals(8, frames.size, "MemoryLayoutRunner should generate 8 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = MemoryLayoutRunner.run()
        frames.forEachIndexed { index, frame ->
            assertNotNull(frame.narr, "Frame $index: narration should not be null")
            assertTrue(frame.narr.isNotEmpty(), "Frame $index: narration should not be empty")
            assertNotNull(frame.phase, "Frame $index: phase should not be null")
            assertTrue(frame.phase.isNotEmpty(), "Frame $index: phase should not be empty")
            assertNotNull(frame.scene, "Frame $index: scene should not be null")
        }
    }

    @Test
    fun testFramePhases() {
        val frames = MemoryLayoutRunner.run()
        val expectedPhases = listOf(
            "overview",
            "code_segment",
            "data_segment",
            "bss_segment",
            "heap",
            "stack",
            "comparison",
            "complete"
        )
        frames.forEachIndexed { index, frame ->
            assertEquals(expectedPhases[index], frame.phase, "Frame $index should have phase ${expectedPhases[index]}")
        }
    }

    @Test
    fun testMemoryScene() {
        val frames = MemoryLayoutRunner.run()
        frames.forEach { frame ->
            val scene = frame.scene as? MemoryScene
            assertNotNull(scene, "Scene should be MemoryScene")
            assertTrue(scene.cells.isNotEmpty(), "Memory scene should have cells")
            assertTrue(scene.legend.isNotEmpty(), "Legend should not be empty")
        }
    }

    @Test
    fun testMemoryCells() {
        val frames = MemoryLayoutRunner.run()
        val overviewFrame = frames[0]
        val scene = overviewFrame.scene as MemoryScene

        assertTrue(scene.cells.size >= 4, "Overview frame should show all 4 segments")

        scene.cells.forEach { cell ->
            assertTrue(cell.address.isNotEmpty(), "Cell should have address")
            assertTrue(cell.value.isNotEmpty(), "Cell should have value")
            assertTrue(cell.label.isNotEmpty(), "Cell should have label")
        }
    }

    @Test
    fun testAddressFormat() {
        val frames = MemoryLayoutRunner.run()
        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                assertTrue(
                    cell.address.startsWith("0x"),
                    "Address ${cell.address} should be in hexadecimal format"
                )
            }
        }
    }

    @Test
    fun testStatistics() {
        val frames = MemoryLayoutRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.stats, "Frame should have statistics")
        }

        // Last frame should track total segments
        val lastFrame = frames.last()
        assertTrue(
            lastFrame.stats.containsKey("total_segments"),
            "Last frame should track total segments"
        )
        assertEquals("4", lastFrame.stats["total_segments"])
    }

    @Test
    fun testConsecutivePhases() {
        val frames = MemoryLayoutRunner.run()
        assertTrue(frames.size > 1, "Should have multiple frames")

        for (i in 0 until frames.size - 1) {
            val currentNarr = frames[i].narr
            val nextNarr = frames[i + 1].narr
            assertTrue(
                currentNarr.isNotEmpty() && nextNarr.isNotEmpty(),
                "Consecutive frames should have non-empty narrations"
            )
        }
    }

    @Test
    fun testMemoryAddressRange() {
        val frames = MemoryLayoutRunner.run()
        val addresses = mutableSetOf<String>()

        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                addresses.add(cell.address)
            }
        }

        assertTrue(addresses.isNotEmpty(), "Should have memory addresses")

        // Verify realistic address ranges (code low, stack high)
        val codeAddrs = addresses.filter { it.startsWith("0x00") }
        val stackAddrs = addresses.filter { it.startsWith("0x7FFF") }

        assertTrue(codeAddrs.isNotEmpty(), "Should have code segment addresses")
        assertTrue(stackAddrs.isNotEmpty(), "Should have stack addresses")
    }

    @Test
    fun testLastFrameComplete() {
        val frames = MemoryLayoutRunner.run()
        val lastFrame = frames.last()

        assertEquals("complete", lastFrame.phase, "Final phase should be 'complete'")
        assertTrue(lastFrame.narr.contains("memory layout"), "Final narration should mention complete layout")

        val scene = lastFrame.scene as MemoryScene
        assertTrue(scene.cells.size >= 4, "Complete frame should show all memory segments")
    }
}
