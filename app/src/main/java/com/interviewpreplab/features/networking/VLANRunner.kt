package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.HeaderField
import com.interviewpreplab.core.model.PacketHeaderScene

object VLANRunner {
    fun run(): List<Frame> = listOf(
        Frame("VLAN (Virtual LAN): partitions single physical switch into multiple logical networks. 802.1Q tag in frame.", "intro",
            mapOf("concept" to "virtual networks", "standard" to "802.1Q")),

        Frame("Without VLAN: all ports on switch see all traffic (flood domain = broadcast domain). Security/efficiency problem.", "problem",
            mapOf("visibility" to "all_to_all", "broadcast_domain" to "single")),

        Frame("With VLAN: ports assigned to VLAN IDs (1-4094). Frames tagged with VLAN ID. Switch only forwards within same VLAN.", "solution",
            mapOf("tag_range" to "1-4094", "isolation" to "per_vlan")),

        Frame("802.1Q tag: 4-byte insertion after source MAC. Contains VLAN ID (12 bits, up to 4094 VLANs) + priority (3 bits).", "tag_format",
            mapOf("total_bytes" to "4", "vlan_id_bits" to "12", "priority_bits" to "3")),

        Frame("Access port: untagged. Belongs to single VLAN. Frame arrives untagged → switch adds tag (implicit). Leaves switch untagged.", "access_port",
            mapOf("type" to "end device", "tagging" to "implicit")),

        Frame("Trunk port: tagged. Carries multiple VLANs. Frames tagged with respective VLAN IDs. Trunk link between switches.", "trunk_port",
            mapOf("type" to "switch-to-switch", "tagging" to "explicit")),

        Frame("Broadcast isolation: broadcast in VLAN 10 doesn't reach VLAN 20. Reduces congestion. Need routing (Layer 3) to cross VLANs.", "isolation",
            mapOf("benefit" to "broadcast containment", "cross_vlan" to "needs routing")),

        Frame("Voice VLAN: separate VLAN for phones (e.g., VLAN 100). Access port + voice VLAN on same physical port (dual VLAN).", "voice_vlan",
            mapOf("use" to "IP phones", "typical" to "separate voice network")),

        Frame("VLAN enables multi-tenant networks, separates departments, improves security/performance. Common in enterprise.", "summary",
            mapOf("flexibility" to "high", "scale" to "enterprise networks"))
    )
}
