package com.interviewpreplab.features.searches

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame
import kotlin.math.sqrt

object JumpSearchRunner {
    fun run(array: List<Int>, target: Int): List<Frame> {
        val frames = mutableListOf<Frame>()
        val sorted = array.sorted()
        var comparisons = 0

        frames.add(Frame(
            narr = "Jump search: jump by √n steps, then linear search.",
            phase = "start",
            stats = mapOf("comparisons" to "0"),
            scene = BarsScene(sorted.mapIndexed { i, v -> Bar(v, setOf("dim")) })
        ))

        val n = sorted.size
        val step = sqrt(n.toFloat()).toInt()
        var prev = 0

        while (sorted[minOf(step, n) - 1] < target) {
            comparisons++
            prev = step
            step + prev
            if (prev >= n) {
                frames.add(Frame(
                    narr = "Target $target not found.",
                    phase = "not-found",
                    stats = mapOf("comparisons" to "$comparisons"),
                    scene = BarsScene(sorted.mapIndexed { _, v -> Bar(v) })
                ))
                return frames
            }
        }

        // Linear search in the identified block
        while (sorted[prev] < target) {
            comparisons++
            prev++
            if (prev == minOf(step, n)) {
                frames.add(Frame(
                    narr = "Target $target not found.",
                    phase = "not-found",
                    stats = mapOf("comparisons" to "$comparisons"),
                    scene = BarsScene(sorted.mapIndexed { _, v -> Bar(v) })
                ))
                return frames
            }
        }

        if (sorted[prev] == target) {
            comparisons++
            frames.add(Frame(
                narr = "Target $target found at index $prev!",
                phase = "found",
                stats = mapOf("comparisons" to "$comparisons"),
                scene = BarsScene(sorted.mapIndexed { i, v ->
                    Bar(v, if (i == prev) setOf("found") else emptySet())
                })
            ))
        } else {
            frames.add(Frame(
                narr = "Target $target not found.",
                phase = "not-found",
                stats = mapOf("comparisons" to "$comparisons"),
                scene = BarsScene(sorted.mapIndexed { _, v -> Bar(v) })
            ))
        }

        return frames
    }
}
