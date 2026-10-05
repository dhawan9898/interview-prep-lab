package com.interviewpreplab.features.searches

import com.interviewpreplab.core.model.Bar
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame

object BinarySearchRunner {
    fun run(array: List<Int>, target: Int): List<Frame> {
        val frames = mutableListOf<Frame>()
        val sorted = array.sorted()
        var comparisons = 0

        frames.add(Frame(
            narr = "Binary search: halve the search space with each comparison.",
            phase = "start",
            stats = mapOf("comparisons" to "0"),
            scene = BarsScene(sorted.mapIndexed { i, v -> Bar(v, setOf("dim")) })
        ))

        var left = 0
        var right = sorted.size - 1

        while (left <= right) {
            val mid = (left + right) / 2
            comparisons++

            frames.add(Frame(
                narr = "Compare ${sorted[mid]} with target $target.",
                phase = "compare",
                stats = mapOf("comparisons" to "$comparisons"),
                scene = BarsScene(sorted.mapIndexed { i, v ->
                    Bar(v, when {
                        i == mid -> setOf("compare")
                        i < left || i > right -> setOf("dim")
                        else -> emptySet()
                    })
                })
            ))

            when {
                sorted[mid] == target -> {
                    frames.add(Frame(
                        narr = "Target $target found at index $mid!",
                        phase = "found",
                        stats = mapOf("comparisons" to "$comparisons"),
                        scene = BarsScene(sorted.mapIndexed { i, v ->
                            Bar(v, if (i == mid) setOf("found") else emptySet())
                        })
                    ))
                    return frames
                }
                sorted[mid] < target -> {
                    left = mid + 1
                    frames.add(Frame(
                        narr = "${sorted[mid]} is too small. Search right half.",
                        phase = "search-right",
                        stats = mapOf("comparisons" to "$comparisons"),
                        scene = BarsScene(sorted.mapIndexed { i, v ->
                            Bar(v, when {
                                i < left || i > right -> setOf("dim")
                                else -> emptySet()
                            })
                        })
                    ))
                }
                else -> {
                    right = mid - 1
                    frames.add(Frame(
                        narr = "${sorted[mid]} is too large. Search left half.",
                        phase = "search-left",
                        stats = mapOf("comparisons" to "$comparisons"),
                        scene = BarsScene(sorted.mapIndexed { i, v ->
                            Bar(v, when {
                                i < left || i > right -> setOf("dim")
                                else -> emptySet()
                            })
                        })
                    ))
                }
            }
        }

        frames.add(Frame(
            narr = "Target $target not found.",
            phase = "not-found",
            stats = mapOf("comparisons" to "$comparisons"),
            scene = BarsScene(sorted.mapIndexed { _, v -> Bar(v) })
        ))

        return frames
    }
}
