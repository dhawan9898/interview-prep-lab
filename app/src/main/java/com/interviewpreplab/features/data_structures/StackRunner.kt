package com.interviewpreplab.features.data_structures

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.Slot
import com.interviewpreplab.core.model.SlotsScene

/**
 * Stack operations: push and pop.
 * Generates frame-by-frame narration for interactive playground.
 */
object StackRunner {
    private const val MAX_SIZE = 8

    data class StackState(
        val items: List<Int> = emptyList(),
        val top: Int = -1
    )

    sealed class StackOp {
        data class Push(val value: Int) : StackOp()
        object Pop : StackOp()
    }

    fun runPush(state: StackState, value: Int): Pair<List<Frame>, StackState> {
        val frames = mutableListOf<Frame>()

        if (state.top >= MAX_SIZE - 1) {
            // Stack overflow
            frames.add(Frame(
                narr = "Stack is full! Cannot push $value.",
                phase = "error",
                stats = mapOf("size" to state.items.size.toString()),
                scene = SlotsScene(
                    slots = state.items.mapIndexed { idx, v ->
                        Slot(v, if (idx == state.top) setOf("selected") else emptySet())
                    },
                    pointers = mapOf("TOP" to state.top)
                )
            ))
            return frames to state
        }

        // Push frame
        val newItems = state.items.toMutableList()
        newItems.add(value)
        val newTop = state.top + 1

        frames.add(Frame(
            narr = "Increment TOP pointer to $newTop.",
            phase = "push-start",
            stats = mapOf("size" to (newTop + 1).toString()),
            scene = SlotsScene(
                slots = newItems.mapIndexed { idx, v ->
                    Slot(v, if (idx == newTop) setOf("selected") else emptySet())
                },
                pointers = mapOf("TOP" to newTop)
            )
        ))

        frames.add(Frame(
            narr = "Insert value $value at TOP position.",
            phase = "push-insert",
            stats = mapOf("size" to (newTop + 1).toString()),
            scene = SlotsScene(
                slots = newItems.mapIndexed { idx, v ->
                    Slot(v, if (idx == newTop) setOf("landed", "selected") else emptySet())
                },
                pointers = mapOf("TOP" to newTop)
            )
        ))

        return frames to StackState(newItems, newTop)
    }

    fun runPop(state: StackState): Pair<List<Frame>, StackState> {
        val frames = mutableListOf<Frame>()

        if (state.top < 0) {
            // Stack underflow
            frames.add(Frame(
                narr = "Stack is empty! Cannot pop.",
                phase = "error",
                stats = mapOf("size" to "0"),
                scene = SlotsScene(
                    slots = emptyList(),
                    pointers = emptyMap()
                )
            ))
            return frames to state
        }

        // Pop frame
        val removedValue = state.items[state.top]
        val newItems = state.items.dropLast(1)
        val newTop = state.top - 1

        frames.add(Frame(
            narr = "Retrieve value $removedValue from TOP position.",
            phase = "pop-retrieve",
            stats = mapOf("size" to state.items.size.toString()),
            scene = SlotsScene(
                slots = state.items.mapIndexed { idx, v ->
                    Slot(v, if (idx == state.top) setOf("selected") else emptySet())
                },
                pointers = mapOf("TOP" to state.top)
            )
        ))

        frames.add(Frame(
            narr = "Decrement TOP pointer to $newTop.",
            phase = "pop-end",
            stats = mapOf("size" to newItems.size.toString()),
            scene = SlotsScene(
                slots = newItems.mapIndexed { idx, v ->
                    Slot(v, emptySet())
                },
                pointers = if (newTop >= 0) mapOf("TOP" to newTop) else emptyMap()
            )
        ))

        return frames to StackState(newItems, newTop)
    }

    fun buildInitial(): Frame {
        return Frame(
            narr = "Stack: LIFO (Last In, First Out). Push adds to the top, pop removes from the top.",
            phase = "start",
            stats = mapOf("size" to "0"),
            scene = SlotsScene(
                slots = emptyList(),
                pointers = emptyMap()
            )
        )
    }
}
