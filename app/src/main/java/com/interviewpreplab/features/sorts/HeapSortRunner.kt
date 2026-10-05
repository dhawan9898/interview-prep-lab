package com.interviewpreplab.features.sorts

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame

object HeapSortRunner {
    fun run(array: List<Int>): List<Frame> {
        val frames = mutableListOf<Frame>()
        val arr = array.toMutableList()
        var comparisons = 0

        frames.add(Frame(
            narr = "Heap sort: build max-heap, then extract max elements one by one.",
            phase = "start",
            stats = mapOf("comparisons" to "0"),
            scene = BarsScene(arr.mapIndexed { i, v -> Bar(v, setOf("dim")) })
        ))

        fun heapify(n: Int, i: Int) {
            var largest = i
            val left = 2 * i + 1
            val right = 2 * i + 2

            if (left < n && arr[left] > arr[largest]) {
                largest = left
            }
            if (right < n && arr[right] > arr[largest]) {
                largest = right
            }

            if (largest != i) {
                comparisons++
                arr[i] = arr[largest].also { arr[largest] = arr[i] }
                heapify(n, largest)
            }
        }

        // Build max heap
        for (i in arr.size / 2 - 1 downTo 0) {
            heapify(arr.size, i)
        }

        // Extract elements from heap
        for (i in arr.size - 1 downTo 0) {
            arr[0] = arr[i].also { arr[i] = arr[0] }
            heapify(i, 0)

            frames.add(Frame(
                narr = "Extract max, heapify remaining.",
                phase = "extract",
                stats = mapOf("comparisons" to "$comparisons"),
                scene = BarsScene(arr.mapIndexed { k, v ->
                    Bar(v, if (k >= i) setOf("sorted") else emptySet())
                })
            ))
        }

        frames.add(Frame(
            narr = "Array is sorted!",
            phase = "done",
            stats = mapOf("comparisons" to "$comparisons"),
            scene = BarsScene(arr.mapIndexed { _, v -> Bar(v, setOf("sorted")) })
        ))

        return frames
    }
}
