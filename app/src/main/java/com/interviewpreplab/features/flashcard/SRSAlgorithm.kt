package com.interviewpreplab.features.flashcard

import javax.inject.Inject

/**
 * SM-2 (SuperMemo 2) Spaced Repetition Algorithm
 *
 * The industry-standard algorithm for optimal learning retention.
 * Scientifically proven to maximize long-term memory retention with minimal review time.
 *
 * Based on: https://www.supermemo.com/en/archives1990-2015/english/ol/2sm2
 */
class SRSAlgorithm @Inject constructor() {

    companion object {
        const val MIN_EASE_FACTOR = 1.3
        const val DEFAULT_EASE_FACTOR = 2.5

        // Quality of response scale (SM-2)
        const val QUALITY_COMPLETE_BLACKOUT = 0  // Completely forgot
        const val QUALITY_INCORRECT = 1           // Incorrect response
        const val QUALITY_DIFFICULT = 2           // Correct response with difficulty
        const val QUALITY_CORRECT_HESITANT = 3    // Correct response after hesitation
        const val QUALITY_CORRECT = 4              // Correct response with easy recall
        const val QUALITY_PERFECT = 5              // Perfect response
    }

    /**
     * Calculate next review parameters using SM-2 algorithm
     *
     * @param quality Quality of response (0-5)
     * @param easeFactor Current ease factor (difficulty multiplier)
     * @param interval Current interval (days until next review)
     * @param repetitions Number of successful repetitions
     *
     * @return Pair of (nextInterval in days, newEaseFactor)
     */
    fun calculateNextReview(
        quality: Int,
        easeFactor: Double,
        interval: Int,
        repetitions: Int
    ): Pair<Int, Double> {

        // Validate quality input
        val validQuality = quality.coerceIn(0, 5)

        // SM-2 formula: EF' = EF + (0.1 - (5-q) * (0.08 + (5-q) * 0.02))
        val newEaseFactor = when {
            validQuality < 3 -> {
                // Forgotten: reset ease factor to minimum
                (easeFactor - 0.2).coerceAtLeast(MIN_EASE_FACTOR)
            }
            else -> {
                // Learned: adjust ease factor based on quality
                val adjustment = 0.1 - (5 - validQuality) * (0.08 + (5 - validQuality) * 0.02)
                (easeFactor + adjustment).coerceAtLeast(MIN_EASE_FACTOR)
            }
        }

        // Calculate interval based on repetition number
        val newInterval = when {
            validQuality < 3 -> {
                // Failed: restart learning
                1
            }
            repetitions == 0 -> {
                // First review: 1 day
                1
            }
            repetitions == 1 -> {
                // Second review: 3 days
                3
            }
            else -> {
                // Subsequent reviews: multiply previous interval by new ease factor
                (interval * newEaseFactor).toInt().coerceAtLeast(1)
            }
        }

        return Pair(newInterval, newEaseFactor)
    }

    /**
     * Calculate next review timestamp
     *
     * @param daysUntilReview Number of days until next review
     * @return Timestamp (milliseconds) for next review
     */
    fun calculateNextReviewTimestamp(daysUntilReview: Int): Long {
        val daysInMillis = daysUntilReview.toLong() * 24 * 60 * 60 * 1000
        return System.currentTimeMillis() + daysInMillis
    }

    /**
     * Get human-readable description of quality level
     */
    fun getQualityDescription(quality: Int): String {
        return when (quality) {
            QUALITY_COMPLETE_BLACKOUT -> "Complete blackout"
            QUALITY_INCORRECT -> "Incorrect response"
            QUALITY_DIFFICULT -> "Difficult response"
            QUALITY_CORRECT_HESITANT -> "Correct with hesitation"
            QUALITY_CORRECT -> "Correct response"
            QUALITY_PERFECT -> "Perfect response"
            else -> "Unknown quality"
        }
    }

    /**
     * Calculate average retention rate from ease factors
     *
     * High ease factor = well-learned, low ease factor = difficult
     */
    fun estimateRetentionRate(easeFactor: Double): Double {
        // Simple linear estimation: ease factor maps to retention
        // 2.5 = 80% (default), 3.0 = 100%, higher = higher retention
        return (80.0 + (easeFactor - DEFAULT_EASE_FACTOR) * 40.0).coerceIn(0.0, 100.0)
    }
}
