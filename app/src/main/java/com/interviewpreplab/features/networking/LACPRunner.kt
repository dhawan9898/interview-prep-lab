package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object LACPRunner {
    fun run(): List<Frame> = listOf(
        Frame("LACP (Link Aggregation Control Protocol): combines multiple links into one logical link. Increases bandwidth & redundancy.", "intro",
            mapOf("purpose" to "link aggregation", "benefit" to "higher BW + redundancy")),

        Frame("Without LACP: 8 1Gbps links to switch = 1 link is active (STP blocks 7 as redundant). Wastes bandwidth.", "problem",
            mapOf("links" to "8", "active" to "1", "wasted" to "7")),

        Frame("With LACP: 8 1Gbps links bundled into single 8Gbps logical link. All active (no STP blocking needed).", "solution",
            mapOf("logical_link" to "8Gbps", "utilization" to "100%")),

        Frame("Link bundle: group of 1-8 physical links treated as single interface. Single MAC, IP address (for L3).", "bundle",
            mapOf("max_links" to "8", "representation" to "single logical interface")),

        Frame("LACP PDU (Protocol Data Unit): sent every 1 second. Exchanges actor/partner system ID, key, port priority.", "lacp_pdu",
            mapOf("interval" to "1 second", "carries" to "actor, partner, key")),

        Frame("Load balancing: frames distributed across links using hash (src/dest IP, src/dest MAC, port). Equal-cost multipath (ECMP).", "load_balancing",
            mapOf("algorithm" to "hash", "goal" to "uniform distribution")),

        Frame("Link failure: if one link down, others carry traffic. LACP detects in ~3 seconds. No traffic loss (unlike STP failover).", "fault_tolerance",
            mapOf("detection_time" to "3 seconds", "impact" to "graceful degradation")),

        Frame("Active/Passive: Active mode → sends LACP PDUs continuously. Passive → responds only. Active-Active = two active, peers with each other.", "modes",
            mapOf("common" to "active-active", "recovery" to "fast")),

        Frame("LACP powers high-speed data center links. 25G, 40G, 100G uplinks use LACP. Essential for modern networks.", "summary",
            mapOf("scale" to "modern data centers", "speeds" to "up to 100G"))
    )
}
