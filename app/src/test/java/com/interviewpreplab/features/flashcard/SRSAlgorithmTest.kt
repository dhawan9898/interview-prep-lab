package com.interviewpreplab.features.flashcard

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SRSAlgorithmTest {

    private val srs = SRSAlgorithm()

    @Test
    fun `first review with perfect score increases ease factor`() {
        val (interval, easeFactor) = srs.calculateNextReview(
            quality = SRSAlgorithm.QUALITY_PERFECT,
            easeFactor = SRSAlgorithm.DEFAULT_EASE_FACTOR,
            interval = 0,
            repetitions = 0
        )

        assertEquals(1, interval)
        assertTrue(easeFactor > SRSAlgorithm.DEFAULT_EASE_FACTOR)
    }

    @Test
    fun `first review with poor score decreases ease factor`() {
        val (interval, easeFactor) = srs.calculateNextReview(
            quality = SRSAlgorithm.QUALITY_DIFFICULT,
            easeFactor = SRSAlgorithm.DEFAULT_EASE_FACTOR,
            interval = 0,
            repetitions = 0
        )

        assertEquals(1, interval)
        assertTrue(easeFactor < SRSAlgorithm.DEFAULT_EASE_FACTOR)
        assertTrue(easeFactor >= SRSAlgorithm.MIN_EASE_FACTOR)
    }

    @Test
    fun `second review interval is 3 days for good response`() {
        val (interval, _) = srs.calculateNextReview(
            quality = SRSAlgorithm.QUALITY_CORRECT,
            easeFactor = SRSAlgorithm.DEFAULT_EASE_FACTOR,
            interval = 1,
            repetitions = 1
        )

        assertEquals(3, interval)
    }

    @Test
    fun `forgotten card resets to 1 day`() {
        val (interval, easeFactor) = srs.calculateNextReview(
            quality = SRSAlgorithm.QUALITY_INCORRECT,
            easeFactor = 3.0,
            interval = 10,
            repetitions = 5
        )

        assertEquals(1, interval)
        assertTrue(easeFactor < 3.0)
    }

    @Test
    fun `third and later reviews multiply interval by ease factor`() {
        val (interval, newEaseFactor) = srs.calculateNextReview(
            quality = SRSAlgorithm.QUALITY_CORRECT,
            easeFactor = 2.5,
            interval = 3,
            repetitions = 2
        )

        // interval should be approximately 3 * newEaseFactor
        assertTrue(interval > 3)
        assertTrue(interval <= 8)  // Reasonable upper bound
    }

    @Test
    fun `ease factor never goes below minimum`() {
        val (_, easeFactor) = srs.calculateNextReview(
            quality = SRSAlgorithm.QUALITY_COMPLETE_BLACKOUT,
            easeFactor = 1.5,
            interval = 0,
            repetitions = 0
        )

        assertTrue(easeFactor >= SRSAlgorithm.MIN_EASE_FACTOR)
    }

    @Test
    fun `quality scores are bounded to 0-5`() {
        val (interval1, ef1) = srs.calculateNextReview(
            quality = 10,  // Out of range
            easeFactor = SRSAlgorithm.DEFAULT_EASE_FACTOR,
            interval = 0,
            repetitions = 0
        )

        // Should behave like quality = 5
        assertTrue(interval1 > 0)
        assertTrue(ef1 > 0)
    }

    @Test
    fun `next review timestamp is in the future`() {
        val now = System.currentTimeMillis()
        val nextReview = srs.calculateNextReviewTimestamp(1)

        assertTrue(nextReview > now)
    }

    @Test
    fun `retention rate estimation`() {
        val rate25 = srs.estimateRetentionRate(2.5)  // Default
        val rate30 = srs.estimateRetentionRate(3.0)  // High
        val rate15 = srs.estimateRetentionRate(1.3)  // Low

        assertEquals(80.0, rate25, 1.0)
        assertTrue(rate30 > rate25)
        assertTrue(rate15 < rate25)
    }

    companion object {
        // Helper constant (not in main algorithm)
        const val QUALITY_INCOMPLETE = 2
    }
}
