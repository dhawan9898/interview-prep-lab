package com.interviewpreplab.features.searches

import com.interviewpreplab.core.model.BarsScene
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LinearSearchRunnerTest {
    private val data = listOf(4, 8, 2, 9, 5, 1)

    @Test
    fun foundTargetEndsOnFoundFrameAtRightIndex() {
        val last = LinearSearchRunner.run(data, 9).last()
        assertEquals("found", last.phase)
        val bars = (last.scene as BarsScene).bars
        assertTrue("found" in bars[3].roles)
        assertEquals("4", last.stats["comparisons"])
    }

    @Test
    fun missingTargetEndsOnNotFound() {
        val last = LinearSearchRunner.run(data, 7).last()
        assertEquals("not_found", last.phase)
        assertEquals("${data.size}", last.stats["comparisons"])
    }

    @Test
    fun firstElementMatchTakesOneComparison() {
        assertEquals("1", LinearSearchRunner.run(data, 4).last().stats["comparisons"])
    }
}
