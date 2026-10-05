package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.HeaderField
import com.interviewpreplab.core.model.PacketFlowScene
import com.interviewpreplab.core.model.PacketHeaderScene

object SubnettingRunner {
    fun run(): List<Frame> = listOf(
        Frame("Subnetting: divides IP address space into subnets. Use CIDR (Classless Interdomain Routing).", "intro",
            mapOf("notation" to "CIDR", "purpose" to "divide networks")),

        Frame("CIDR notation: IP/prefix. /24 means 24 bits are network, 8 bits are host.", "cidr",
            mapOf("example" to "192.168.1.0/24", "network_bits" to "24", "host_bits" to "8")),

        Frame("Network address: last host bits = 0. Broadcast: last host bits = 1.", "addresses",
            mapOf("network" to "192.168.1.0", "broadcast" to "192.168.1.255", "usable" to "254")),

        Frame("/24 subnet: 256 addresses (2^8). Network + broadcast = 254 usable IPs for hosts.", "sizing",
            mapOf("total" to "256", "usable" to "254", "prefix" to "/24")),

        Frame("/25 = 128 addresses/subnet. /26 = 64. /30 = 4 (point-to-point links). /32 = 1 (host route).", "common_prefixes",
            mapOf("slash25" to "128", "slash26" to "64", "slash30" to "4")),

        Frame("Subnet mask: /24 = 255.255.255.0. /25 = 255.255.255.128. Shows network/host boundary.", "mask",
            mapOf("slash24_mask" to "255.255.255.0", "slash25_mask" to "255.255.255.128")),

        Frame("Host bits must match subnet: 192.168.1.0/24 includes 192.168.1.1 through 192.168.1.254.", "membership",
            mapOf("subnet" to "192.168.1.0/24", "first_host" to "192.168.1.1", "last_host" to "192.168.1.254")),

        Frame("Supernetting: combine /25 subnets into /24. Routing uses longest-prefix match (more on that next).", "supernetting",
            mapOf("combined" to "192.168.0.0/24", "source_subnets" to "2")),

        Frame("Subnetting enables efficient address allocation, easier routing, and multi-level hierarchy.", "summary",
            mapOf("benefits" to "3", "foundation" to "routing"))
    )
}
