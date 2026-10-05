package com.interviewpreplab.features.sorts

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame

object MergeSortRunner {
    fun run(array: List<Int>): List<Frame> {
        val frames = mutableListOf<Frame>()
        val arr = array.toMutableList()
        var comparisons = 0

        frames.add(Frame(
            narr = "Merge sort: divide array in half, sort each half, merge them.",
            phase = "start",
            stats = mapOf("comparisons" to "0"),
            scene = BarsScene(arr.mapIndexed { i, v -> Bar(v, setOf("dim")) })
        ))

        fun mergeSort(low: Int, high: Int) {
            if (low < high) {
                val mid = (low + high) / 2
                mergeSort(low, mid)
                mergeSort(mid + 1, high)

                // Merge step
                val left = arr.subList(low, mid + 1).toMutableList()
                val right = arr.subList(mid + 1, high + 1).toMutableList()

                var i = 0
                var j = 0
                var k = low

                while (i < left.size && j < right.size) {
                    comparisons++
                    if (left[i] <= right[j]) {
                        arr[k++] = left[i++]
                    } else {
                        arr[k++] = right[j++]
                    }
                }

                while (i < left.size) {
                    arr[k++] = left[i++]
                }

                while (j < right.size) {
                    arr[k++] = right[j++]
                }

                frames.add(Frame(
                    narr = "Merge sorted ranges.",
                    phase = "merge",
                    stats = mapOf("comparisons" to "$comparisons"),
                    scene = BarsScene(arr.mapIndexed { idx, v ->
                        Bar(v, if (idx in low..high) setOf("sorted") else emptySet())
                    })
                ))
            }
        }

        mergeSort(0, arr.size - 1)

        frames.add(Frame(
            narr = "Array is sorted!",
            phase = "done",
            stats = mapOf("comparisons" to "$comparisons"),
            scene = BarsScene(arr.mapIndexed { _, v -> Bar(v, setOf("sorted")) })
        ))

        return frames
    }
}
