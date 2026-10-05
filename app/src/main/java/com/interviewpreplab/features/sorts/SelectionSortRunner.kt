package com.interviewpreplab.features.sorts

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame

object SelectionSortRunner {
    fun run(array: List<Int>): List<Frame> {
        val frames = mutableListOf<Frame>()
        val arr = array.toMutableList()
        var comparisons = 0
        var swaps = 0

        frames.add(Frame(
            narr = "Selection sort: find the minimum element and swap it to the front.",
            phase = "start",
            stats = mapOf("comparisons" to "0", "swaps" to "0"),
            scene = BarsScene(arr.mapIndexed { i, v -> Bar(v, setOf("dim")) })
        ))

        val n = arr.size
        for (i in 0 until n - 1) {
            var minIdx = i
            for (j in i + 1 until n) {
                comparisons++
                frames.add(Frame(
                    narr = "Compare position $i (${arr[i]}) with position $j (${arr[j]}).",
                    phase = "compare",
                    stats = mapOf("comparisons" to "$comparisons", "swaps" to "$swaps"),
                    scene = BarsScene(arr.mapIndexed { k, v ->
                        Bar(v, when {
                            k == i -> setOf("pivot")
                            k == j -> setOf("compare")
                            k < i -> setOf("sorted")
                            else -> emptySet()
                        })
                    })
                ))

                if (arr[j] < arr[minIdx]) {
                    minIdx = j
                }
            }

            if (minIdx != i) {
                swaps++
                arr[i] = arr[minIdx].also { arr[minIdx] = arr[i] }
                frames.add(Frame(
                    narr = "Minimum found. Swap position $i with position $minIdx.",
                    phase = "swap",
                    stats = mapOf("comparisons" to "$comparisons", "swaps" to "$swaps"),
                    scene = BarsScene(arr.mapIndexed { k, v ->
                        Bar(v, when {
                            k == i || k == minIdx -> setOf("landed", "sorted")
                            k < i -> setOf("sorted")
                            else -> emptySet()
                        })
                    })
                ))
            }
        }

        frames.add(Frame(
            narr = "Array is sorted!",
            phase = "done",
            stats = mapOf("comparisons" to "$comparisons", "swaps" to "$swaps"),
            scene = BarsScene(arr.mapIndexed { _, v -> Bar(v, setOf("sorted")) })
        ))

        return frames
    }
}
