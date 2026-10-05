package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object NATRunner {
    private fun baseFrames(): List<Frame> = listOf(
        Frame("NAT (Network Address Translation): router translates private IPs to public IP. Enables LAN without public IPs.", "intro",
            mapOf("private_ips" to "many", "public_ips" to "1")),

        Frame("Private IP ranges: 10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16. Not routable on Internet.", "private_ranges",
            mapOf("range1" to "10.0.0.0/8", "range2" to "172.16.0.0/12", "range3" to "192.168.0.0/16")),

        Frame("Outgoing: Host 192.168.1.100 sends to 8.8.8.8. NAT router replaces source IP with public IP 203.0.113.5.", "outgoing",
            mapOf("private_src" to "192.168.1.100", "public_src" to "203.0.113.5")),

        Frame("NAT maintains translation table: (private_ip:port, public_ip:port). Returns traffic: maps public dest back to private.", "translation_table",
            mapOf("entry" to "(192.168.1.100:5000, 203.0.113.5:12345)", "purpose" to "bidirectional")),

        Frame("Incoming traffic to public IP 203.0.113.5:12345 → look up table → forward to 192.168.1.100:5000.", "incoming",
            mapOf("public_dest" to "203.0.113.5:12345", "private_dest" to "192.168.1.100:5000")),

        Frame("NAT types: Symmetric (all hosts same mapping) vs Port-Preserving (try to keep same port).", "nat_types",
            mapOf("types" to "2", "impact" to "P2P applications")),

        Frame("Problem: inbound connections to private hosts blocked (no incoming translation entry). Workaround: port forwarding.", "inbound_problem",
            mapOf("issue" to "inbound blocked", "solution" to "port forwarding")),

        Frame("Port forwarding: admin manually maps public port to private IP:port. Example: public 8080 → 192.168.1.100:80 (web server).", "port_forwarding",
            mapOf("public_port" to "8080", "private" to "192.168.1.100:80")),

        Frame("NAT enables millions of devices on private networks to share limited public IPv4 space. Bridge until IPv6 adoption.", "summary",
            mapOf("scaling" to "high", "duration" to "decades"))
    )

    private val nodes = listOf(
        FlowNode("host", "192.168.1.100", "host"),
        FlowNode("nat", "NAT router", "router"),
        FlowNode("server", "8.8.8.8", "server")
    )

    fun run(): List<Frame> = baseFrames().withFlow(
        nodes = nodes,
        stages = mapOf(
            2 to stage(FlowStep("host", "nat", "TCP", "src 192.168.1.100:5000"), FlowStep("nat", "server", "TCP", "src 203.0.113.5:12345")),
            4 to stage(FlowStep("server", "nat", "TCP", "dst 203.0.113.5:12345"), FlowStep("nat", "host", "TCP", "dst 192.168.1.100:5000"))
        ),
        description = "NAT translation"
    )
}
