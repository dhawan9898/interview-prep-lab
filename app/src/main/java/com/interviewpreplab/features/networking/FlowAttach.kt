package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.PacketFlowScene

data class FlowStep(
    val from: String,
    val to: String,
    val protocol: String,
    val details: String,
    val flags: Set<String> = emptySet()
)

/** Packets that appear when a frame is reached; [reset] clears earlier packets first. */
data class FlowStage(val steps: List<FlowStep>, val reset: Boolean = false)

/**
 * Gives narration-only frames a packet-flow diagram without changing their text, phase, or count.
 * Packets accumulate frame by frame so the sequence builds up as the lesson plays.
 */
fun List<Frame>.withFlow(
    nodes: List<FlowNode>,
    stages: Map<Int, FlowStage>,
    description: String
): List<Frame> {
    val shown = mutableListOf<FlowPacket>()
    return mapIndexed { index, frame ->
        stages[index]?.let { stage ->
            if (stage.reset) shown.clear()
            stage.steps.forEach { s ->
                val forward = nodes.indexOfFirst { it.id == s.from } <= nodes.indexOfFirst { it.id == s.to }
                shown += FlowPacket(
                    from = s.from, to = s.to, protocol = s.protocol, details = s.details,
                    direction = if (forward) 1 else -1, timestamp = shown.size, flags = s.flags
                )
            }
        }
        frame.copy(scene = PacketFlowScene(nodes, shown.toList(), description = description))
    }
}

internal fun stage(vararg steps: FlowStep, reset: Boolean = false) = FlowStage(steps.toList(), reset)
