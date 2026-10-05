package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.HeaderField
import com.interviewpreplab.core.model.PacketHeaderScene

object IPv6HeaderRunner {
    fun run(): List<Frame> = listOf(
        Frame("IPv6 header: 40 bytes fixed. Simpler than IPv4. Addresses are 128 bits.", "intro",
            mapOf("size" to "40 bytes", "address_bits" to "128"),
            PacketHeaderScene("IPv6 Header", listOf(
                HeaderField("Version", 0, 4, 0, "6", "IPv6"),
                HeaderField("Traffic Class", 4, 8, 0, "0", "QoS"),
                HeaderField("Flow Label", 12, 20, 1, "0", "Flow ID"),
                HeaderField("Payload Length", 32, 16, 4, "1234", "Bytes after header"),
                HeaderField("Next Header", 48, 8, 6, "6", "TCP (like Protocol in v4)"),
                HeaderField("Hop Limit", 56, 8, 7, "64", "Like TTL in v4"),
                HeaderField("Source IP", 64, 128, 8, "2001:db8::1", "Sender (128-bit)"),
                HeaderField("Dest IP", 192, 128, 24, "2001:db8::2", "Recipient (128-bit)")
            ), 40)),

        Frame("Version (4 bits) = 6 for IPv6. Traffic Class (8 bits) = QoS priority.", "header_start",
            mapOf("version" to "6", "traffic_class" to "0")),

        Frame("Flow Label (20 bits): identifies flows for QoS/real-time. IPv4 has no equivalent.", "flow_label",
            mapOf("bits" to "20", "purpose" to "QoS flow identification")),

        Frame("Payload Length: bytes after 40-byte header. Max 65,535 bytes (or use jumbo payload).", "payload_length",
            mapOf("header" to "40", "payload_max" to "65535")),

        Frame("Next Header (8 bits): 6=TCP, 17=UDP, 58=ICMPv6, 0=hop-by-hop option. IPv4 calls it Protocol.", "next_header",
            mapOf("tcp" to "6", "udp" to "17", "icmpv6" to "58")),

        Frame("Hop Limit (8 bits): same as IPv4 TTL. Decrements at each router. Prevents loops.", "hop_limit",
            mapOf("initial" to "64", "decrements_per_hop" to "1")),

        Frame("Source & Dest IP: 128 bits each (16 bytes). Written as hex colon-separated: 2001:db8::1", "addresses",
            mapOf("source" to "2001:db8::1", "dest" to "2001:db8::2", "bytes" to "16")),

        Frame("IPv6 advantages: no fragmentation by endpoint (Path MTU Discovery), no checksum (faster), fixed header size.", "advantages",
            mapOf("no_fragmentation" to "true", "no_checksum" to "true", "fixed_size" to "40")),

        Frame("IPv6 header structure: simpler than IPv4, supports extension headers via Next Header field.", "summary",
            mapOf("size" to "40 bytes", "features" to "5", "address_space" to "128-bit"))
    )
}
