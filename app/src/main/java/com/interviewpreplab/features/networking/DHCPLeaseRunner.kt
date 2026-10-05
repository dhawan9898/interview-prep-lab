package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object DHCPLeaseRunner {
    private fun baseFrames(): List<Frame> = listOf(
        Frame("DHCP (Dynamic Host Configuration Protocol): assigns IP addresses, gateways, DNS servers to hosts automatically.", "intro",
            mapOf("layer" to "application/transport", "goal" to "dynamic addressing")),

        Frame("DHCP Discover (broadcast): new host broadcasts 'Anyone offering IP addresses?' on 255.255.255.255:67.", "step1_discover",
            mapOf("packet_type" to "DISCOVER", "destination" to "broadcast")),

        Frame("DHCP Offer: DHCP server replies (unicast or broadcast) with offered IP, lease time, gateway, DNS servers.", "step2_offer",
            mapOf("packet_type" to "OFFER", "includes" to "IP, lease_time, gateway, DNS")),

        Frame("DHCP Request: client selects an offer, broadcasts 'I accept this IP'. Other servers retract their offers.", "step3_request",
            mapOf("packet_type" to "REQUEST", "scope" to "broadcast")),

        Frame("DHCP Ack (Acknowledge): server confirms 'Your IP is 192.168.1.100, lease expires in 24 hours'.", "step4_ack",
            mapOf("packet_type" to "ACK", "confirms" to "IP assignment")),

        Frame("Lease time: typically 24 hours. Client can renew at 50% lease time (12 hours). After lease expires, IP revoked.", "lease",
            mapOf("default_duration" to "24 hours", "renewal" to "50% through")),

        Frame("DHCP Decline: if client detects IP conflict (ARP reply), sends Decline. Server marks IP as no longer offered.", "decline",
            mapOf("trigger" to "IP conflict", "action" to "server retracts IP")),

        Frame("DHCP Release: client explicitly releases IP before lease expires. 'I'm done with this IP.'", "release",
            mapOf("voluntary" to "true", "benefit" to "early reuse")),

        Frame("DHCP enables plug-and-play networking. No manual IP assignment. Essential for mobile devices, hotels, coffee shops.", "summary",
            mapOf("convenience" to "high", "scale" to "millions of devices"))
    )

    private val nodes = listOf(
        FlowNode("client", "Client", "host"),
        FlowNode("server", "DHCP Server", "server")
    )

    fun run(): List<Frame> = baseFrames().withFlow(
        nodes = nodes,
        stages = mapOf(
            1 to stage(FlowStep("client", "server", "DHCP", "DISCOVER (broadcast)")),
            2 to stage(FlowStep("server", "client", "DHCP", "OFFER 192.168.1.100")),
            3 to stage(FlowStep("client", "server", "DHCP", "REQUEST 192.168.1.100")),
            4 to stage(FlowStep("server", "client", "DHCP", "ACK lease 24h")),
            7 to stage(FlowStep("client", "server", "DHCP", "RELEASE"))
        ),
        description = "DHCP DORA exchange"
    )
}
