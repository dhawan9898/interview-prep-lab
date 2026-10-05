package com.interviewpreplab.features.sorts

import com.interviewpreplab.core.model.BarsScene
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShellSortRunnerTest {
    private val input = listOf(9, 4, 7, 1, 8, 2, 6, 3)

    @Test
    fun finalFrameIsSorted() {
        val last = ShellSortRunner.run(input).last().scene as BarsScene
        assertEquals(input.sorted(), last.bars.map { it.value })
    }

    @Test
    fun framesKeepArrayLengthAndHaveComparePhase() {
        val frames = ShellSortRunner.run(input)
        assertTrue(frames.all { (it.scene as BarsScene).bars.size == input.size })
        assertTrue(frames.any { it.phase == "compare" })
        assertTrue(frames.any { it.phase == "swap" })
    }

    @Test
    fun alreadySortedInputNeverSwaps() {
        val frames = ShellSortRunner.run(input.sorted())
        assertTrue(frames.none { it.phase == "swap" })
    }
}
