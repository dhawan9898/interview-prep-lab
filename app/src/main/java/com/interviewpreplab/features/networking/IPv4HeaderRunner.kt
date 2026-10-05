package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.HeaderField
import com.interviewpreplab.core.model.PacketHeaderScene

object IPv4HeaderRunner {
    fun run(): List<Frame> = listOf(
        Frame("IPv4 header: minimum 20 bytes. Provides routing and fragmentation.", "intro",
            mapOf("size" to "20 bytes", "role" to "routing"),
            PacketHeaderScene("IPv4 Header", listOf(
                HeaderField("Version", 0, 4, 0, "4", "IPv4"),
                HeaderField("IHL", 4, 4, 0, "5", "Header length"),
                HeaderField("DSCP", 8, 6, 1, "0", "QoS"),
                HeaderField("Total Length", 16, 16, 2, "1234", "Entire packet"),
                HeaderField("TTL", 64, 8, 8, "64", "Hop limit"),
                HeaderField("Protocol", 72, 8, 9, "6", "TCP"),
                HeaderField("Source IP", 96, 32, 12, "192.168.1.1", "Sender"),
                HeaderField("Dest IP", 128, 32, 16, "8.8.8.8", "Recipient")
            ), 20)),

        Frame("Version (4 bits) = 4 for IPv4. IHL (4 bits) = header length in 32-bit words.", "header_start",
            mapOf("version" to "4", "ihl" to "5"),
            PacketHeaderScene("Version & IHL", listOf(
                HeaderField("Version", 0, 4, 0, "4", "IPv4"),
                HeaderField("IHL", 4, 4, 0, "5", "20-byte header")
            ))),

        Frame("TTL (Time To Live): starts at 64. Decrements at each router hop. Prevents loops.", "ttl",
            mapOf("initial" to "64", "decrements_per_hop" to "1"),
            PacketHeaderScene("TTL Field", listOf(
                HeaderField("TTL", 0, 8, 0, "64", "Router 1: 64"),
                HeaderField("TTL", 0, 8, 0, "63", "Router 2: 63"),
                HeaderField("TTL", 0, 8, 0, "0", "Timeout - dropped")
            ))),

        Frame("Protocol (8 bits): 6=TCP, 17=UDP, 1=ICMP, 41=IPv6. Tells what Layer 4 protocol.", "protocol",
            mapOf("tcp" to "6", "udp" to "17", "icmp" to "1"),
            PacketHeaderScene("Protocol Field", listOf(
                HeaderField("TCP", 0, 8, 0, "6", "Transmission Control"),
                HeaderField("UDP", 0, 8, 0, "17", "User Datagram"),
                HeaderField("ICMP", 0, 8, 0, "1", "Control Message")
            ))),

        Frame("Source IP (32 bits) = sender address. Dest IP (32 bits) = recipient address.", "addresses",
            mapOf("source" to "192.168.1.1", "dest" to "8.8.8.8"),
            PacketHeaderScene("IP Addresses", listOf(
                HeaderField("Source IP", 0, 32, 0, "192.168.1.1", "Sender"),
                HeaderField("Dest IP", 0, 32, 0, "8.8.8.8", "Google DNS")
            ))),

        Frame("Total Length: 20-byte header + payload size. Maximum 65,535 bytes.", "total_length",
            mapOf("header" to "20", "payload_max" to "65515", "total_max" to "65535"),
            PacketHeaderScene("Total Length", listOf(
                HeaderField("Total Length", 0, 16, 0, "1234", "20 header + 1214 data")
            ))),

        Frame("Fragmentation: if packet > MTU, split into fragments. Each gets same ID.", "fragmentation",
            mapOf("mtu" to "1500", "splits" to "multiple", "field" to "Fragment Offset"),
            PacketHeaderScene("Fragment Fields", listOf(
                HeaderField("Identification", 0, 16, 0, "12345", "Fragment group ID"),
                HeaderField("Flags", 0, 3, 0, "0", "DF=dont fragment"),
                HeaderField("Fragment Offset", 0, 13, 0, "0", "Byte position")
            ))),

        Frame("Checksum: covers header only (not payload). Recalculated at each hop (TTL changes).", "checksum",
            mapOf("scope" to "header only", "recalc" to "per hop"),
            PacketHeaderScene("Checksum", listOf(
                HeaderField("Checksum", 0, 16, 0, "0xABCD", "Header integrity")
            ))),

        Frame("IPv4 provides: routing (via dest IP), fragmentation, TTL loop prevention, QoS.", "summary",
            mapOf("features" to "4", "reliability" to "best-effort"),
            PacketHeaderScene("IPv4 Complete Header", listOf(
                HeaderField("Version", 0, 4, 0, "4", "IPv4"),
                HeaderField("IHL", 4, 4, 0, "5", "20 bytes"),
                HeaderField("Total Length", 16, 16, 2, "1234", "Entire packet"),
                HeaderField("TTL", 64, 8, 8, "64", "Hop limit"),
                HeaderField("Protocol", 72, 8, 9, "6", "TCP"),
                HeaderField("Checksum", 80, 16, 10, "0xABCD", "Integrity"),
                HeaderField("Source IP", 96, 32, 12, "192.168.1.1", "Sender"),
                HeaderField("Dest IP", 128, 32, 16, "8.8.8.8", "Recipient")
            ), 20))
    )
}
