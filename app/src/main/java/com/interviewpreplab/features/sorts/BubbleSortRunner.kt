package com.interviewpreplab.features.sorts

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame

/**
 * Bubble sort implementation that generates frame-by-frame narration.
 * Ported from js/sorting/bubble-sort/bubble-sort-visualizer.html's build() function.
 */
object BubbleSortRunner {
    fun run(array: List<Int>): List<Frame> {
        val frames = mutableListOf<Frame>()
        val arr = array.toMutableList()
        var comparisons = 0
        var swaps = 0

        // Start frame
        frames.add(Frame(
            narr = "Bubble sort: repeatedly step through the list, compare adjacent elements, and swap if they're out of order.",
            phase = "start",
            stats = mapOf("comparisons" to "0", "swaps" to "0"),
            scene = BarsScene(arr.mapIndexed { i, v ->
                Bar(v, setOf("dim"))
            })
        ))

        val n = arr.size
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                // Compare frame
                comparisons++
                val roles = mutableListOf<Set<String>>()
                for (k in arr.indices) {
                    when {
                        k == j || k == j + 1 -> roles.add(setOf("compare"))
                        k >= n - i -> roles.add(setOf("sorted"))
                        else -> roles.add(setOf())
                    }
                }
                frames.add(Frame(
                    narr = "Compare position $j (${arr[j]}) with position ${j + 1} (${arr[j + 1]}).",
                    phase = "compare",
                    stats = mapOf("comparisons" to "$comparisons", "swaps" to "$swaps"),
                    scene = BarsScene(arr.mapIndexed { k, v -> Bar(v, roles[k]) })
                ))

                if (arr[j] > arr[j + 1]) {
                    // Swap frame
                    swaps++
                    arr[j] = arr[j + 1].also { arr[j + 1] = arr[j] }
                    val swapRoles = mutableListOf<Set<String>>()
                    for (k in arr.indices) {
                        when {
                            k == j || k == j + 1 -> swapRoles.add(setOf("compare", "landed"))
                            k >= n - i -> swapRoles.add(setOf("sorted"))
                            else -> swapRoles.add(setOf())
                        }
                    }
                    frames.add(Frame(
                        narr = "Out of order, so swap them.",
                        phase = "swap",
                        stats = mapOf("comparisons" to "$comparisons", "swaps" to "$swaps"),
                        scene = BarsScene(arr.mapIndexed { k, v -> Bar(v, swapRoles[k]) })
                    ))
                }
            }

            // Pass done frame
            val passRoles = mutableListOf<Set<String>>()
            for (k in arr.indices) {
                when {
                    k >= n - i - 1 -> passRoles.add(setOf("sorted"))
                    else -> passRoles.add(setOf())
                }
            }
            frames.add(Frame(
                narr = "Pass ${i + 1} complete. Largest unsorted element is in place.",
                phase = "pass-done",
                stats = mapOf("comparisons" to "$comparisons", "swaps" to "$swaps"),
                scene = BarsScene(arr.mapIndexed { k, v -> Bar(v, passRoles[k]) })
            ))
        }

        // Final frame
        frames.add(Frame(
            narr = "Array is sorted!",
            phase = "done",
            stats = mapOf("comparisons" to "$comparisons", "swaps" to "$swaps"),
            scene = BarsScene(arr.mapIndexed { _, v -> Bar(v, setOf("sorted")) })
        ))

        return frames
    }
}
