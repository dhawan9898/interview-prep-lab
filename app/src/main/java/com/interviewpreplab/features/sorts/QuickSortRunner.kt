package com.interviewpreplab.features.sorts

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame

object QuickSortRunner {
    fun run(array: List<Int>): List<Frame> {
        val frames = mutableListOf<Frame>()
        val arr = array.toMutableList()

        frames.add(Frame(
            narr = "Quick sort: divide and conquer using a pivot element.",
            phase = "start",
            stats = mapOf("comparisons" to "0"),
            scene = BarsScene(arr.mapIndexed { i, v -> Bar(v, setOf("dim")) })
        ))

        var comparisons = 0

        fun partition(low: Int, high: Int): Int {
            val pivot = arr[high]
            var i = low - 1

            for (j in low until high) {
                comparisons++
                if (arr[j] < pivot) {
                    i++
                    arr[i] = arr[j].also { arr[j] = arr[i] }
                }
            }

            arr[i + 1] = arr[high].also { arr[high] = arr[i + 1] }
            return i + 1
        }

        fun quickSort(low: Int, high: Int) {
            if (low < high) {
                val pi = partition(low, high)

                frames.add(Frame(
                    narr = "Pivot: ${arr[pi]}. Partition complete.",
                    phase = "partition",
                    stats = mapOf("comparisons" to "$comparisons"),
                    scene = BarsScene(arr.mapIndexed { k, v ->
                        Bar(v, when {
                            k == pi -> setOf("pivot")
                            k < pi -> setOf("dim")
                            k > pi -> setOf("dim")
                            else -> emptySet()
                        })
                    })
                ))

                quickSort(low, pi - 1)
                quickSort(pi + 1, high)
            }
        }

        quickSort(0, arr.size - 1)

        frames.add(Frame(
            narr = "Array is sorted!",
            phase = "done",
            stats = mapOf("comparisons" to "$comparisons"),
            scene = BarsScene(arr.mapIndexed { _, v -> Bar(v, setOf("sorted")) })
        ))

        return frames
    }
}
