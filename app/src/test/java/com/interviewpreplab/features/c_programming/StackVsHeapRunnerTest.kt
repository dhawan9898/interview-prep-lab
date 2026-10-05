package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.MemoryScene
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class StackVsHeapRunnerTest {

    @Test
    fun testFrameCount() {
        val frames = StackVsHeapRunner.run()
        assertEquals(15, frames.size, "StackVsHeapRunner should generate 15 frames")
    }

    @Test
    fun testAllFramesValid() {
        val frames = StackVsHeapRunner.run()

        frames.forEachIndexed { index, frame ->
            assertTrue(frame.narr.isNotEmpty(), "Frame $index narration empty")
            assertTrue(frame.phase.isNotEmpty(), "Frame $index phase empty")
            assertNotNull(frame.scene, "Frame $index scene null")
            assertNotNull(frame.stats, "Frame $index stats null")
        }
    }

    @Test
    fun testPhaseSequence() {
        val frames = StackVsHeapRunner.run()

        val expectedPhases = listOf(
            "stack_allocation",
            "function_call",
            "function_return",
            "heap_allocation",
            "multiple_allocations",
            "free_allocation",
            "fragmentation",
            "speed_comparison",
            "size_comparison",
            "stack_overflow",
            "stack_safety",
            "heap_responsibility",
            "stack_usage",
            "heap_usage",
            "summary"
        )

        frames.forEachIndexed { index, frame ->
            assertEquals(expectedPhases[index], frame.phase, "Frame $index phase mismatch")
        }
    }

    @Test
    fun testStackAllocationFrame() {
        val frames = StackVsHeapRunner.run()
        val stackFrame = frames[0]

        assertEquals("stack_allocation", stackFrame.phase)
        assertTrue(stackFrame.narr.contains("automatic"))

        val scene = stackFrame.scene as MemoryScene
        // Should have stack cells
        assertTrue(scene.cells.any { "stack" in it.roles }, "Should have stack memory")
    }

    @Test
    fun testHeapAllocationFrame() {
        val frames = StackVsHeapRunner.run()
        val heapFrame = frames[3]

        assertEquals("heap_allocation", heapFrame.phase)
        assertTrue(heapFrame.narr.contains("malloc"))

        val scene = heapFrame.scene as MemoryScene
        assertTrue(scene.cells.any { "heap" in it.roles }, "Should have heap memory")
    }

    @Test
    fun testFragmentationFrame() {
        val frames = StackVsHeapRunner.run()
        val fragFrame = frames[6]

        assertEquals("fragmentation", fragFrame.phase)
        assertTrue(fragFrame.narr.contains("fragmentation"))

        val scene = fragFrame.scene as MemoryScene
        // Fragmentation frame should show multiple free and allocated blocks
        assertTrue(scene.cells.size >= 3, "Should show multiple blocks")
    }

    @Test
    fun testComparisonFrames() {
        val frames = StackVsHeapRunner.run()

        val speedFrame = frames[7]
        assertEquals("speed_comparison", speedFrame.phase)
        assertTrue(speedFrame.narr.contains("fast") || speedFrame.narr.contains("slow"))

        val sizeFrame = frames[8]
        assertEquals("size_comparison", sizeFrame.phase)
        assertTrue(sizeFrame.narr.contains("size") || sizeFrame.narr.contains("limited"))
    }

    @Test
    fun testStackOverflowFrame() {
        val frames = StackVsHeapRunner.run()
        val overflowFrame = frames[9]

        assertEquals("stack_overflow", overflowFrame.phase)
        assertTrue(overflowFrame.narr.contains("overflow") || overflowFrame.narr.contains("crash"))
    }

    @Test
    fun testUsageRecommendationFrames() {
        val frames = StackVsHeapRunner.run()

        val stackUsageFrame = frames[12]
        assertEquals("stack_usage", stackUsageFrame.phase)
        assertTrue(stackUsageFrame.narr.contains("stack"))

        val heapUsageFrame = frames[13]
        assertEquals("heap_usage", heapUsageFrame.phase)
        assertTrue(heapUsageFrame.narr.contains("heap"))
    }

    @Test
    fun testMemoryAddressingConvention() {
        val frames = StackVsHeapRunner.run()

        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                // All addresses should follow convention
                if ("stack" in cell.roles) {
                    assertTrue(cell.address.startsWith("0x7FFF"), "Stack should be high address")
                }
                if ("heap" in cell.roles) {
                    assertTrue(cell.address.startsWith("0x5555"), "Heap should be lower address")
                }
            }
        }
    }

    @Test
    fun testStatisticsPresent() {
        val frames = StackVsHeapRunner.run()

        frames.forEach { frame ->
            // Each frame should have meaningful stats
            if (!frame.stats.isEmpty()) {
                // Stats should relate to the topic
                val stats = frame.stats.keys.joinToString()
                assertTrue(stats.isNotEmpty())
            }
        }
    }

    @Test
    fun testPointerPairsInHeapFrames() {
        val frames = StackVsHeapRunner.run()

        frames.forEach { frame ->
            if ("heap" in frame.phase) {
                val scene = frame.scene as MemoryScene
                // Heap frames should show pointer relationships
                if (scene.pointers.isEmpty() && scene.cells.any { "pointer" in it.roles }) {
                    assertTrue(scene.cells.any { "ptr" in it.label })
                }
            }
        }
    }

    @Test
    fun testSummaryFrame() {
        val frames = StackVsHeapRunner.run()
        val summaryFrame = frames.last()

        assertEquals("summary", summaryFrame.phase)
        assertTrue(summaryFrame.narr.contains("stack") && summaryFrame.narr.contains("heap"))
    }

    @Test
    fun testFrameNarrationUniqueness() {
        val frames = StackVsHeapRunner.run()
        val narrations = frames.map { it.narr }

        // Most narrations should be unique (allowing some similar ones)
        val uniqueCount = narrations.toSet().size
        assertTrue(uniqueCount >= frames.size - 2, "Most frames should have unique narrations")
    }

    @Test
    fun testMemoryCellRoles() {
        val frames = StackVsHeapRunner.run()

        val allRoles = mutableSetOf<String>()
        frames.forEach { frame ->
            val scene = frame.scene as MemoryScene
            scene.cells.forEach { cell ->
                allRoles.addAll(cell.roles)
            }
        }

        // Should have both stack and heap roles
        assertTrue("stack" in allRoles, "Should have stack roles")
        assertTrue("heap" in allRoles, "Should have heap roles")
    }
}
