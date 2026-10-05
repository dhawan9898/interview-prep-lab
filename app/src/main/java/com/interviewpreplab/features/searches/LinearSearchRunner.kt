package com.interviewpreplab.features.searches

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame

/** Linear search: check each element from the left until the target is found or the array ends. */
object LinearSearchRunner {
    fun run(array: List<Int>, target: Int): List<Frame> {
        val frames = mutableListOf<Frame>()
        var comparisons = 0

        fun snapshot(narr: String, phase: String, roles: Map<Int, Set<String>>) {
            frames.add(
                Frame(
                    narr = narr,
                    phase = phase,
                    stats = mapOf("target" to "$target", "comparisons" to "$comparisons"),
                    scene = BarsScene(array.mapIndexed { k, v -> Bar(v, roles[k] ?: emptySet()) })
                )
            )
        }

        snapshot("Linear search for $target: look at each element from the left.", "start", emptyMap())
        for (i in array.indices) {
            comparisons++
            val seen = (0 until i).associateWith { setOf("dim") }
            snapshot("Is ${array[i]} equal to $target?", "compare", seen + (i to setOf("compare")))
            if (array[i] == target) {
                snapshot(
                    "Found $target at index $i after $comparisons comparison(s).",
                    "found", seen + (i to setOf("found"))
                )
                return frames
            }
        }
        snapshot(
            "Reached the end: $target is not in the array (${array.size} comparisons).",
            "not_found", array.indices.associateWith { setOf("dim") }
        )
        return frames
    }
}
