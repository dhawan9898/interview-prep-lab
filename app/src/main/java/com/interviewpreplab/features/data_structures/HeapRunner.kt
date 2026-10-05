package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.Slot
import com.interviewpreplab.core.model.SlotsScene

/**
 * Min Heap operations: insert and extract-min.
 * Maintains heap property: parent ≤ children.
 */
object HeapRunner {
    data class HeapState(
        val items: List<Int> = emptyList()
    )

    fun insert(state: HeapState, value: Int): Pair<List<Frame>, HeapState> {
        val frames = mutableListOf<Frame>()
        val newItems = state.items.toMutableList()
        newItems.add(value)

        frames.add(Frame(
            narr = "Insert $value at end.",
            phase = "insert-add",
            stats = mapOf("size" to newItems.size.toString()),
            scene = SlotsScene(
                slots = newItems.mapIndexed { idx, v ->
                    Slot(v, if (idx == newItems.size - 1) setOf("landed") else emptySet())
                }
            )
        ))

        // Bubble up (simplified: just show final state)
        var idx = newItems.size - 1
        while (idx > 0) {
            val parentIdx = (idx - 1) / 2
            if (newItems[idx] < newItems[parentIdx]) {
                newItems[idx] = newItems[parentIdx].also { newItems[parentIdx] = newItems[idx] }
                idx = parentIdx

                frames.add(Frame(
                    narr = "Bubble up: swap with parent.",
                    phase = "bubble-up",
                    stats = mapOf("size" to newItems.size.toString()),
                    scene = SlotsScene(slots = newItems.map { v -> Slot(v) })
                ))
            } else {
                break
            }
        }

        frames.add(Frame(
            narr = "Min heap property restored.",
            phase = "insert-done",
            stats = mapOf("size" to newItems.size.toString()),
            scene = SlotsScene(slots = newItems.map { v -> Slot(v, setOf("sorted")) })
        ))

        return frames to HeapState(newItems)
    }

    fun extractMin(state: HeapState): Pair<List<Frame>, HeapState> {
        val frames = mutableListOf<Frame>()

        if (state.items.isEmpty()) {
            frames.add(Frame(
                narr = "Heap is empty!",
                phase = "error",
                stats = mapOf("size" to "0"),
                scene = SlotsScene(slots = emptyList())
            ))
            return frames to state
        }

        val minValue = state.items[0]

        frames.add(Frame(
            narr = "Extract minimum: $minValue.",
            phase = "extract-min",
            stats = mapOf("size" to state.items.size.toString()),
            scene = SlotsScene(
                slots = state.items.mapIndexed { idx, v ->
                    Slot(v, if (idx == 0) setOf("selected") else emptySet())
                }
            )
        ))

        val newItems = state.items.toMutableList()
        val lastVal = newItems.last()
        newItems.removeAt(newItems.size - 1)

        if (newItems.isNotEmpty()) {
            newItems[0] = lastVal

            frames.add(Frame(
                narr = "Move last element to root.",
                phase = "extract-move",
                stats = mapOf("size" to newItems.size.toString()),
                scene = SlotsScene(slots = newItems.map { v -> Slot(v) })
            ))

            // Bubble down (simplified)
            var idx = 0
            while (2 * idx + 1 < newItems.size) {
                var smallest = idx
                val left = 2 * idx + 1
                val right = 2 * idx + 2

                if (left < newItems.size && newItems[left] < newItems[smallest]) {
                    smallest = left
                }
                if (right < newItems.size && newItems[right] < newItems[smallest]) {
                    smallest = right
                }

                if (smallest != idx) {
                    newItems[idx] = newItems[smallest].also { newItems[smallest] = newItems[idx] }
                    idx = smallest

                    frames.add(Frame(
                        narr = "Bubble down: swap with smaller child.",
                        phase = "bubble-down",
                        stats = mapOf("size" to newItems.size.toString()),
                        scene = SlotsScene(slots = newItems.map { v -> Slot(v) })
                    ))
                } else {
                    break
                }
            }
        }

        frames.add(Frame(
            narr = "Min heap property restored.",
            phase = "extract-done",
            stats = mapOf("size" to newItems.size.toString()),
            scene = SlotsScene(slots = newItems.map { v -> Slot(v, setOf("sorted")) })
        ))

        return frames to HeapState(newItems)
    }

    fun buildInitial(): Frame {
        return Frame(
            narr = "Min Heap: parent ≤ children. Fast min extraction.",
            phase = "start",
            stats = mapOf("size" to "0"),
            scene = SlotsScene(slots = emptyList())
        )
    }
}
