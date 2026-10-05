package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.HeaderField
import com.interviewpreplab.core.model.PacketFlowScene
import com.interviewpreplab.core.model.PacketHeaderScene

object EthernetRunner {
    fun run(): List<Frame> = listOf(
        Frame("Ethernet is Layer 2 protocol. Each frame carries 48-bit MAC addresses.", "intro",
            mapOf("layer" to "2", "destination" to "MAC"),
            PacketHeaderScene("Ethernet Frame", listOf(
                HeaderField("Dest MAC", 0, 48, 0, "FF:FF:FF:FF:FF:FF", "Broadcast"),
                HeaderField("Src MAC", 48, 48, 6, "AA:BB:CC:DD:EE:01", "Sender"),
                HeaderField("Type", 96, 16, 12, "0x0800", "IPv4"),
                HeaderField("Payload", 112, 368, 14, "[46-1500 bytes]", "Data"),
                HeaderField("FCS", 480, 32, 60, "0x12345678", "Checksum")
            ), 64, description = "Ethernet II Frame Structure")),

        Frame("MAC address: 48 bits = 6 bytes in hex. Example: AA:BB:CC:DD:EE:01", "mac_address",
            mapOf("bits" to "48", "bytes" to "6"),
            PacketFlowScene(
                listOf(FlowNode("src", "Source"), FlowNode("dst", "Destination")),
                listOf(FlowPacket("src", "dst", "Ethernet", "AA:BB:CC:DD:EE:01 → FF:FF:FF:FF:FF:FF"))
            )),

        Frame("Type field indicates protocol: 0x0800=IPv4, 0x0806=ARP, 0x86DD=IPv6", "type_field",
            mapOf("ipv4" to "0x0800", "arp" to "0x0806"),
            PacketHeaderScene("Type Field", listOf(
                HeaderField("IPv4", 0, 16, 0, "0x0800", "IP protocol"),
                HeaderField("ARP", 0, 16, 0, "0x0806", "Address resolution"),
                HeaderField("IPv6", 0, 16, 0, "0x86DD", "IP version 6")
            ), 2)),

        Frame("Payload: 46-1500 bytes. Minimum 46 bytes (padding if needed).", "payload",
            mapOf("min_bytes" to "46", "max_bytes" to "1500"),
            PacketHeaderScene("Ethernet Payload", listOf(
                HeaderField("Data", 0, 368, 0, "[46 to 1500 bytes]", "Encapsulated protocol")
            ))),

        Frame("FCS (Frame Check Sequence): 32-bit CRC for error detection.", "fcs",
            mapOf("bits" to "32", "purpose" to "error detection"),
            PacketHeaderScene("FCS Checksum", listOf(
                HeaderField("CRC32", 0, 32, 0, "0x12345678", "Detects bit errors")
            ))),

        Frame("Maximum frame size (MTU): 1518 bytes total (header + payload + FCS).", "mtu",
            mapOf("total" to "1518", "header" to "14", "payload" to "1500"),
            PacketFlowScene(
                listOf(FlowNode("src", "Host A"), FlowNode("dst", "Host B")),
                listOf(FlowPacket("src", "dst", "Ethernet", "1518-byte frame max"))
            )),

        Frame("Unicast: single destination. Broadcast: FF:FF:FF:FF:FF:FF. Multicast: special range.", "addressing",
            mapOf("types" to "3"),
            PacketFlowScene(
                listOf(FlowNode("src", "Sender"), FlowNode("ucast", "Unicast"), FlowNode("bcast", "All")),
                listOf(
                    FlowPacket("src", "ucast", "Ethernet", "Unicast (specific destination)"),
                    FlowPacket("src", "bcast", "Ethernet", "Broadcast (all hosts)")
                )
            )),

        Frame("Ethernet operates at Layer 2 (Data Link). Provides hop-to-hop delivery.", "layer2",
            mapOf("layer" to "2 (Data Link)", "scope" to "LAN"),
            PacketFlowScene(
                listOf(FlowNode("h1", "Host1"), FlowNode("sw", "Switch"), FlowNode("h2", "Host2")),
                listOf(FlowPacket("h1", "sw", "Ethernet", "Frames via switch"), FlowPacket("sw", "h2", "Ethernet", ""))
            )),

        Frame("Ethernet frame: 14-byte header + 46-1500 byte payload + 4-byte FCS = 64-1518 bytes total.", "summary",
            mapOf("header" to "14", "payload" to "1500", "total" to "1518"),
            PacketHeaderScene("Ethernet Frame (Complete)", listOf(
                HeaderField("Dest MAC", 0, 48, 0, "FF:FF:FF:FF:FF:FF", "Broadcast"),
                HeaderField("Src MAC", 48, 48, 6, "AA:BB:CC:DD:EE:01", "Sender"),
                HeaderField("Type", 96, 16, 12, "0x0800", "IPv4")
            ), 14))
    )
}
