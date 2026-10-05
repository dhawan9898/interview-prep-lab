package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object ICMPTracerouteRunner {
    fun run(): List<Frame> = listOf(
        Frame("ICMP (Internet Control Message Protocol): Layer 3 protocol for network diagnostics. Types: Echo, Unreachable, TTL Exceeded.", "intro",
            mapOf("layer" to "3", "types" to "3+")),

        Frame("Ping: sends ICMP Echo Request (type 8). Remote responds with Echo Reply (type 0). Measures round-trip time.", "ping",
            mapOf("request_type" to "8", "reply_type" to "0", "use" to "connectivity check")),

        Frame("Traceroute: sends packets with TTL=1, 2, 3... Each router decrements TTL. At TTL=0, sends ICMP Time Exceeded.", "traceroute_method",
            mapOf("ttl_sequence" to "1,2,3,...", "trigger" to "TTL=0")),

        Frame("Traceroute example: Host sends UDP to port 33434 with TTL=1. Router1 replies ICMP TTL Exceeded (gives Router1 IP).", "traceroute_step1",
            mapOf("ttl" to "1", "router" to "Router1", "icmp_type" to "11")),

        Frame("Then TTL=2: packet reaches Router2, TTL decrements to 0, Router2 replies (gives Router2 IP). Continues to destination.", "traceroute_step2",
            mapOf("ttl" to "2", "router" to "Router2")),

        Frame("Destination reached: TTL > 1 when arriving. Responds with ICMP Port Unreachable (port 33434 usually closed).", "traceroute_destination",
            mapOf("response" to "Port Unreachable", "signifies" to "destination reached")),

        Frame("Result: list of hop-by-hop IPs and latencies. Shows path packets take through the Internet.", "traceroute_output",
            mapOf("hops" to "multiple", "shows" to "routing path")),

        Frame("ICMP types: 0=Echo Reply, 8=Echo Request, 11=Time Exceeded, 3=Destination Unreachable, 5=Redirect.", "icmp_types",
            mapOf("echo_reply" to "0", "echo_request" to "8", "time_exceeded" to "11")),

        Frame("ICMP is not typically rate-limited in older networks, but modern routers limit ICMP to prevent DoS.", "rate_limiting",
            mapOf("purpose" to "prevent flooding", "modern" to "true"))
    )
}
