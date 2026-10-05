package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object MACLearningRunner {
    fun run(): List<Frame> = listOf(
        Frame("MAC learning: switch learns source MAC → port mapping. Builds forwarding table dynamically.", "intro",
            mapOf("mechanism" to "learning", "table" to "MAC-to-port")),

        Frame("Initial: switch MAC table empty. Frame arrives on port 1 from src MAC AA:BB:CC:DD:EE:01.", "step1_arrival",
            mapOf("source_mac" to "AA:BB:CC:DD:EE:01", "arrived_on" to "port1", "table_before" to "empty")),

        Frame("Switch learns: 'MAC AA:BB:CC:DD:EE:01 is on port 1.' Adds entry to table. Ages out after 5 minutes (default).", "step2_learning",
            mapOf("entry" to "AA:BB:CC:DD:EE:01 → port1", "aging" to "5 minutes")),

        Frame("Destination unknown: frame for dest MAC FF:FF:FF:FF:FF:FF (broadcast) → flood to all ports except input.", "step3_broadcast",
            mapOf("dest" to "broadcast", "action" to "flood")),

        Frame("Known destination: if table has 'BB:AA:00:11:22:33 → port 3', forward there. Don't flood.", "step4_forward",
            mapOf("dest" to "BB:AA:00:11:22:33", "action" to "forward to port3")),

        Frame("Learning continues: return frames reveal dest MAC → port. Eventually all common paths learned.", "step5_bidirectional",
            mapOf("learning" to "bidirectional", "result" to "efficient forwarding")),

        Frame("MAC table size: 16K-256K entries depending on switch. Old entries age out to handle device moves.", "table_limits",
            mapOf("typical_size" to "64K", "aging" to "300 seconds")),

        Frame("Spanning Tree Protocol (STP): prevents loops while keeping backup links. Disables some ports. MAC learning still works.", "stp_interaction",
            mapOf("role" to "redundancy", "impact" to "some ports blocked")),

        Frame("MAC learning = intelligent switching. Bridges efficiency gaps between hubs (no learning, flood all) and routers (ignore MAC).", "summary",
            mapOf("efficiency" to "high", "simplicity" to "elegant"))
    )
}
