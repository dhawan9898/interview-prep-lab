package com.interviewpreplab.features.sorts

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame

/** Shell sort: gapped insertion sort with a shrinking gap, one frame per compare and swap. */
object ShellSortRunner {
    fun run(array: List<Int>): List<Frame> {
        val frames = mutableListOf<Frame>()
        val arr = array.toMutableList()
        val n = arr.size
        var comparisons = 0
        var swaps = 0

        fun snapshot(narr: String, phase: String, gap: Int, roles: Map<Int, Set<String>>) {
            frames.add(
                Frame(
                    narr = narr,
                    phase = phase,
                    stats = mapOf("gap" to "$gap", "comparisons" to "$comparisons", "swaps" to "$swaps"),
                    scene = BarsScene(arr.mapIndexed { k, v -> Bar(v, roles[k] ?: emptySet()) })
                )
            )
        }

        snapshot(
            "Shell sort: run insertion sort on elements a gap apart, then shrink the gap until it is 1.",
            "start", n / 2, arr.indices.associateWith { setOf("dim") }
        )

        var gap = n / 2
        while (gap > 0) {
            snapshot("Gap is $gap: compare elements that are $gap positions apart.", "gap", gap, emptyMap())
            for (i in gap until n) {
                var j = i
                while (j >= gap) {
                    comparisons++
                    snapshot(
                        "Compare position ${j - gap} (${arr[j - gap]}) with position $j (${arr[j]}).",
                        "compare", gap, mapOf(j - gap to setOf("compare"), j to setOf("compare"))
                    )
                    if (arr[j - gap] > arr[j]) {
                        arr[j] = arr[j - gap].also { arr[j - gap] = arr[j] }
                        swaps++
                        snapshot(
                            "Out of order, so swap them across the gap.",
                            "swap", gap, mapOf(j - gap to setOf("compare", "landed"), j to setOf("compare", "landed"))
                        )
                        j -= gap
                    } else {
                        break
                    }
                }
            }
            gap /= 2
        }

        snapshot("Gap 1 pass done: the array is sorted.", "done", 1, arr.indices.associateWith { setOf("sorted") })
        return frames
    }
}
