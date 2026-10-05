package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.Slot
import com.interviewpreplab.core.model.SlotsScene

/**
 * Circular Queue: solves the dead space problem using modulo wraparound.
 * Frames emphasize the circular nature and modulo index calculation.
 */
object CircularQueueRunner {
    private const val MAX_SIZE = 5

    data class CircularQueueState(
        val items: List<Int?> = MutableList(MAX_SIZE) { null },
        val front: Int = 0,
        val count: Int = 0
    )

    fun enqueue(state: CircularQueueState, value: Int): Pair<List<Frame>, CircularQueueState> {
        val frames = mutableListOf<Frame>()

        if (state.count >= MAX_SIZE) {
            frames.add(Frame(
                narr = "Circular queue is full! Cannot enqueue $value.",
                phase = "error",
                stats = mapOf("size" to state.count.toString()),
                scene = buildScene(state)
            ))
            return frames to state
        }

        val rear = (state.front + state.count) % MAX_SIZE
        val newItems = state.items.toMutableList()
        newItems[rear] = value

        // Frame 1: Calculate rear index
        frames.add(Frame(
            narr = "Calculate rear index: (FRONT + COUNT) % MAX = ($state.front + $state.count) % $MAX_SIZE = $rear.",
            phase = "enqueue-calc",
            stats = mapOf("size" to (state.count + 1).toString()),
            scene = SlotsScene(
                slots = newItems.mapIndexed { idx, v ->
                    Slot(
                        v,
                        when {
                            idx == state.front -> setOf("head")
                            idx == rear -> setOf("selected")
                            v == null -> setOf("dim")
                            else -> emptySet()
                        }
                    )
                },
                pointers = mapOf("FRONT" to state.front, "REAR" to rear)
            )
        ))

        // Frame 2: Insert value
        frames.add(Frame(
            narr = "Insert value $value at index $rear.",
            phase = "enqueue-insert",
            stats = mapOf("size" to (state.count + 1).toString()),
            scene = SlotsScene(
                slots = newItems.mapIndexed { idx, v ->
                    Slot(
                        v,
                        when {
                            idx == state.front -> setOf("head")
                            idx == rear -> setOf("landed", "selected")
                            v == null -> setOf("dim")
                            else -> emptySet()
                        }
                    )
                },
                pointers = mapOf("FRONT" to state.front, "REAR" to rear)
            )
        ))

        return frames to CircularQueueState(newItems, state.front, state.count + 1)
    }

    fun dequeue(state: CircularQueueState): Pair<List<Frame>, CircularQueueState> {
        val frames = mutableListOf<Frame>()

        if (state.count == 0) {
            frames.add(Frame(
                narr = "Circular queue is empty! Cannot dequeue.",
                phase = "error",
                stats = mapOf("size" to "0"),
                scene = buildScene(state)
            ))
            return frames to state
        }

        val removedValue = state.items[state.front]
        val newItems = state.items.toMutableList()
        newItems[state.front] = null
        val newFront = (state.front + 1) % MAX_SIZE

        // Frame 1: Remove value
        frames.add(Frame(
            narr = "Remove value $removedValue from index ${state.front}.",
            phase = "dequeue-remove",
            stats = mapOf("size" to state.count.toString()),
            scene = SlotsScene(
                slots = state.items.mapIndexed { idx, v ->
                    Slot(
                        v,
                        when {
                            idx == state.front -> setOf("selected")
                            v == null -> setOf("dim")
                            else -> emptySet()
                        }
                    )
                },
                pointers = mapOf("FRONT" to state.front)
            )
        ))

        // Frame 2: Update front pointer with modulo
        frames.add(Frame(
            narr = "Increment FRONT pointer: (FRONT + 1) % MAX = (${state.front} + 1) % $MAX_SIZE = $newFront. No dead space!",
            phase = "dequeue-advance",
            stats = mapOf("size" to (state.count - 1).toString()),
            scene = SlotsScene(
                slots = newItems.mapIndexed { idx, v ->
                    Slot(
                        v,
                        when {
                            idx == newFront -> setOf("head")
                            v == null -> setOf("dim")
                            else -> emptySet()
                        }
                    )
                },
                pointers = mapOf("FRONT" to newFront)
            )
        ))

        return frames to CircularQueueState(newItems, newFront, state.count - 1)
    }

    fun buildInitial(): Frame {
        return Frame(
            narr = "Circular Queue: uses modulo to wrap around and reuse dead space. SIZE = $MAX_SIZE slots.",
            phase = "start",
            stats = mapOf("size" to "0"),
            scene = buildScene(CircularQueueState())
        )
    }

    private fun buildScene(state: CircularQueueState): SlotsScene {
        return SlotsScene(
            slots = state.items.mapIndexed { idx, v ->
                Slot(
                    v,
                    when {
                        idx == state.front -> setOf("head")
                        v == null -> setOf("dim")
                        else -> emptySet()
                    }
                )
            },
            pointers = mapOf("FRONT" to state.front, "COUNT" to state.count)
        )
    }
}
