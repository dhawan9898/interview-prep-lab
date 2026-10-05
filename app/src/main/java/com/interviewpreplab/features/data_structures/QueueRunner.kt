package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.Slot
import com.interviewpreplab.core.model.SlotsScene

/**
 * Queue operations: enqueue and dequeue (FIFO).
 * Shows the "dead space" problem that leads to Circular Queue.
 */
object QueueRunner {
    private const val MAX_SIZE = 8

    data class QueueState(
        val items: List<Int> = emptyList(),
        val front: Int = 0,
        val rear: Int = -1
    )

    fun enqueue(state: QueueState, value: Int): Pair<List<Frame>, QueueState> {
        val frames = mutableListOf<Frame>()

        if (state.rear >= MAX_SIZE - 1) {
            frames.add(Frame(
                narr = "Queue is full! Cannot enqueue $value.",
                phase = "error",
                stats = mapOf("size" to (state.rear - state.front + 1).toString()),
                scene = SlotsScene(
                    slots = state.items.mapIndexed { idx, v ->
                        Slot(
                            v,
                            when {
                                idx == state.front -> setOf("head")
                                idx == state.rear -> setOf("selected")
                                idx < state.front || idx > state.rear -> setOf("dim")
                                else -> emptySet()
                            }
                        )
                    },
                    pointers = mapOf("FRONT" to state.front, "REAR" to state.rear)
                )
            ))
            return frames to state
        }

        val newItems = state.items.toMutableList()
        if (newItems.size < state.rear + 2) {
            newItems.add(value)
        } else {
            newItems[state.rear + 1] = value
        }
        val newRear = state.rear + 1

        // Enqueue frame 1
        frames.add(Frame(
            narr = "Increment REAR pointer to $newRear.",
            phase = "enqueue-start",
            stats = mapOf("size" to (newRear - state.front + 1).toString()),
            scene = SlotsScene(
                slots = newItems.mapIndexed { idx, v ->
                    Slot(
                        v,
                        when {
                            idx == state.front -> setOf("head")
                            idx == newRear -> setOf("selected")
                            idx < state.front || idx > newRear -> setOf("dim")
                            else -> emptySet()
                        }
                    )
                },
                pointers = mapOf("FRONT" to state.front, "REAR" to newRear)
            )
        ))

        // Enqueue frame 2
        frames.add(Frame(
            narr = "Insert value $value at REAR position.",
            phase = "enqueue-insert",
            stats = mapOf("size" to (newRear - state.front + 1).toString()),
            scene = SlotsScene(
                slots = newItems.mapIndexed { idx, v ->
                    Slot(
                        v,
                        when {
                            idx == state.front -> setOf("head")
                            idx == newRear -> setOf("landed", "selected")
                            idx < state.front || idx > newRear -> setOf("dim")
                            else -> emptySet()
                        }
                    )
                },
                pointers = mapOf("FRONT" to state.front, "REAR" to newRear)
            )
        ))

        return frames to QueueState(newItems, state.front, newRear)
    }

    fun dequeue(state: QueueState): Pair<List<Frame>, QueueState> {
        val frames = mutableListOf<Frame>()

        if (state.front > state.rear) {
            frames.add(Frame(
                narr = "Queue is empty! Cannot dequeue.",
                phase = "error",
                stats = mapOf("size" to "0"),
                scene = SlotsScene(
                    slots = emptyList(),
                    pointers = emptyMap()
                )
            ))
            return frames to state
        }

        val removedValue = state.items[state.front]
        val newFront = state.front + 1

        // Dequeue frame 1
        frames.add(Frame(
            narr = "Retrieve value $removedValue from FRONT position.",
            phase = "dequeue-retrieve",
            stats = mapOf("size" to (state.rear - state.front + 1).toString()),
            scene = SlotsScene(
                slots = state.items.mapIndexed { idx, v ->
                    Slot(
                        v,
                        when {
                            idx == state.front -> setOf("selected")
                            idx < state.front || idx > state.rear -> setOf("dim")
                            else -> emptySet()
                        }
                    )
                },
                pointers = mapOf("FRONT" to state.front, "REAR" to state.rear)
            )
        ))

        // Dequeue frame 2
        frames.add(Frame(
            narr = "Increment FRONT pointer to $newFront. Notice the dead space (slots 0 to ${newFront - 1}).",
            phase = "dequeue-end",
            stats = mapOf("size" to (state.rear - newFront + 1).toString()),
            scene = SlotsScene(
                slots = state.items.mapIndexed { idx, v ->
                    Slot(
                        v,
                        when {
                            idx < newFront -> setOf("dim")
                            idx > state.rear -> setOf("dim")
                            else -> emptySet()
                        }
                    )
                },
                pointers = mapOf("FRONT" to newFront, "REAR" to state.rear)
            )
        ))

        return frames to QueueState(state.items, newFront, state.rear)
    }

    fun buildInitial(): Frame {
        return Frame(
            narr = "Queue: FIFO (First In, First Out). Enqueue adds to REAR, dequeue removes from FRONT.",
            phase = "start",
            stats = mapOf("size" to "0"),
            scene = SlotsScene(
                slots = emptyList(),
                pointers = emptyMap()
            )
        )
    }
}
