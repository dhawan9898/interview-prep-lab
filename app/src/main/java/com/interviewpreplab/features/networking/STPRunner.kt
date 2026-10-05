package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object STPRunner {
    fun run(): List<Frame> = listOf(
        Frame("STP (Spanning Tree Protocol): prevents loops in switched networks. Disables redundant links, keeps active tree.", "intro",
            mapOf("problem" to "loops", "solution" to "spanning tree")),

        Frame("Switch network with loop: frame floods forever. Broadcast storm. Network unusable.", "problem",
            mapOf("cause" to "cycles", "effect" to "broadcast storm")),

        Frame("STP builds spanning tree: n switches need n-1 links. Remaining links disabled (backup). One path between any two switches.", "solution",
            mapOf("links_active" to "n-1", "backup_links" to "disabled")),

        Frame("Root bridge election: switch with lowest bridge ID (MAC address) becomes root. All paths computed toward root.", "root_election",
            mapOf("criteria" to "lowest BID", "role" to "tree center")),

        Frame("BPDU (Bridge Protocol Data Unit): STP packets sent every 2 seconds. Carry root BID, cost, sender BID. Lowest cost wins.", "bpdu",
            mapOf("interval" to "2 seconds", "carries" to "root, cost, sender")),

        Frame("Port roles: Root Port (closest to root), Designated Port (segment root side), Blocked Port (backup).", "port_roles",
            mapOf("roles" to "3", "blocked_ports" to "disabled")),

        Frame("Convergence: when topology changes, STP re-computes in ~30 seconds (old). RSTP: 1 second (rapid convergence).", "convergence",
            mapOf("stp_time" to "30 seconds", "rstp_time" to "1 second")),

        Frame("BPDU Guard: port goes down if unexpected BPDUs received (prevents rogue switch). BPDU Filter: suppress on portfast ports.", "security",
            mapOf("guard" to "anti-rogue", "filter" to "clean edges")),

        Frame("STP enables high availability: redundant links for fault tolerance. Without STP, loops would crash network.", "summary",
            mapOf("availability" to "high", "cost" to "convergence_time"))
    )
}
