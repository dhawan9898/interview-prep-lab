package com.interviewpreplab.features.sorts

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame

object InsertionSortRunner {
    fun run(array: List<Int>): List<Frame> {
        val frames = mutableListOf<Frame>()
        val arr = array.toMutableList()
        var comparisons = 0
        var shifts = 0

        frames.add(Frame(
            narr = "Insertion sort: build a sorted array by inserting elements one at a time.",
            phase = "start",
            stats = mapOf("comparisons" to "0", "shifts" to "0"),
            scene = BarsScene(arr.mapIndexed { i, v -> Bar(v, if (i == 0) setOf("sorted") else setOf("dim")) })
        ))

        for (i in 1 until arr.size) {
            val key = arr[i]

            frames.add(Frame(
                narr = "Insert element ${arr[i]} into sorted portion.",
                phase = "insert-start",
                stats = mapOf("comparisons" to "$comparisons", "shifts" to "$shifts"),
                scene = BarsScene(arr.mapIndexed { k, v ->
                    Bar(v, when {
                        k < i -> setOf("sorted")
                        k == i -> setOf("compare")
                        else -> setOf("dim")
                    })
                })
            ))

            var j = i - 1
            while (j >= 0 && arr[j] > key) {
                comparisons++
                arr[j + 1] = arr[j]
                shifts++

                frames.add(Frame(
                    narr = "Element ${arr[j]} is larger. Shift right.",
                    phase = "shift",
                    stats = mapOf("comparisons" to "$comparisons", "shifts" to "$shifts"),
                    scene = BarsScene(arr.mapIndexed { k, v ->
                        Bar(v, when {
                            k <= i -> setOf("compare")
                            else -> setOf("dim")
                        })
                    })
                ))

                j--
            }

            arr[j + 1] = key

            frames.add(Frame(
                narr = "Insert $key at position ${j + 1}.",
                phase = "insert-done",
                stats = mapOf("comparisons" to "$comparisons", "shifts" to "$shifts"),
                scene = BarsScene(arr.mapIndexed { k, v ->
                    Bar(v, when {
                        k <= i -> setOf("sorted", "landed")
                        else -> setOf("dim")
                    })
                })
            ))
        }

        frames.add(Frame(
            narr = "Array is sorted!",
            phase = "done",
            stats = mapOf("comparisons" to "$comparisons", "shifts" to "$shifts"),
            scene = BarsScene(arr.mapIndexed { _, v -> Bar(v, setOf("sorted")) })
        ))

        return frames
    }
}
