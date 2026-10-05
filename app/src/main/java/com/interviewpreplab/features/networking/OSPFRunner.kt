package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object OSPFRunner {
    fun run(): List<Frame> = listOf(
        Frame("OSPF (Open Shortest Path First): link-state routing protocol. Routers share network topology, compute shortest paths.", "intro",
            mapOf("type" to "link-state", "algorithm" to "Dijkstra")),

        Frame("Link-state: each router broadcasts LSA (Link-State Advertisements). All routers know full topology.", "link_state",
            mapOf("concept" to "full topology awareness", "mechanism" to "LSA flooding")),

        Frame("LSA contains: source router ID, destination router ID, metric (cost). Examples: serial link (cost 1), fast Ethernet (cost 10).", "lsa",
            mapOf("fields" to "3", "example_cost" to "10 Mbps → cost 10")),

        Frame("Flooding: router sends LSA to all neighbors. Neighbors forward to their neighbors (except sender). Eventually all routers get it.", "flooding",
            mapOf("scope" to "entire AS", "mechanism" to "broadcast")),

        Frame("SPF (Shortest Path First) tree: each router runs Dijkstra using collected LSAs. Builds tree of lowest-cost paths.", "spf",
            mapOf("algorithm" to "Dijkstra", "output" to "SPF tree")),

        Frame("Routing table built from SPF tree. Multiple equal-cost paths → load balance (ECMP).", "routing_table",
            mapOf("source" to "SPF tree", "multipath" to "true")),

        Frame("Hello packets: keepalive every 10 seconds. Detect dead neighbors in 40 seconds (4 missed hellos).", "hello_packets",
            mapOf("interval" to "10 sec", "timeout" to "40 sec")),

        Frame("OSPF areas: divide AS into areas (backbone 0.0.0.0). Area routers flood LSAs within area, ABRs (Area Border Routers) connect areas.", "areas",
            mapOf("backbone" to "0.0.0.0", "scaling" to "large networks")),

        Frame("Advantages: fast convergence (seconds), multipath support, load balancing. Disadvantage: higher CPU/memory than distance-vector.", "summary",
            mapOf("convergence" to "fast", "complexity" to "high"))
    )
}
