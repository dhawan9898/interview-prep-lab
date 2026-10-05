package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.ARPTypes
import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene
import com.interviewpreplab.core.model.TimelineStep

/**
 * Generates frames showing ARP (Address Resolution Protocol).
 * Visualizes: ARP request, ARP reply, MAC address discovery, cache learning.
 */
object ARPRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Problem - IP to MAC mapping
        frames.add(Frame(
            narr = "Host A needs to reach Host B at IP 192.168.1.50. But what is the MAC address?",
            phase = "arp_problem",
            stats = mapOf("known" to "IP address", "needed" to "MAC address"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "Host A (192.168.1.10)", "host", 0.25f),
                    FlowNode("hostB", "Host B (192.168.1.50)", "host", 0.75f)
                ),
                packets = emptyList(),
                description = "Host A needs Host B's MAC address"
            )
        ))

        // Frame 2: ARP request broadcast
        frames.add(Frame(
            narr = "Host A sends ARP REQUEST: 'Who has IP 192.168.1.50?' to broadcast MAC FF:FF:FF:FF:FF:FF.",
            phase = "arp_request",
            stats = mapOf("destination" to "broadcast", "operation" to "who-has"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "Host A", "host", 0.25f),
                    FlowNode("hostB", "Host B", "host", 0.75f),
                    FlowNode("others", "Other Hosts", "host", 0.5f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "hostA",
                        to = "broadcast",
                        protocol = "ARP",
                        details = "Who has 192.168.1.50?",
                        timestamp = 0,
                        flags = setOf("REQUEST")
                    )
                ),
                description = "ARP request broadcasted to all hosts"
            )
        ))

        // Frame 3: Other hosts ignore
        frames.add(Frame(
            narr = "Other hosts check: 'Is the target IP ours?' No, so they ignore the request.",
            phase = "arp_ignored",
            stats = mapOf("responding" to "0", "ignored_by" to "other hosts"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "Host A", "host", 0.25f),
                    FlowNode("hostB", "Host B", "host", 0.75f),
                    FlowNode("others", "Other Hosts (ignore)", "host", 0.5f)
                ),
                packets = emptyList(),
                description = "Non-target hosts ignore ARP request"
            )
        ))

        // Frame 4: Target host sends reply
        frames.add(Frame(
            narr = "Host B recognizes its IP and sends ARP REPLY: 'I have 192.168.1.50, my MAC is AA:BB:CC:DD:EE:02'.",
            phase = "arp_reply",
            stats = mapOf("responder" to "Host B", "operation" to "is-at"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "Host A", "host", 0.25f),
                    FlowNode("hostB", "Host B (responding)", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "hostB",
                        to = "hostA",
                        protocol = "ARP",
                        details = "I have 192.168.1.50, MAC = AA:BB:CC:DD:EE:02",
                        direction = -1,
                        timestamp = 1,
                        flags = setOf("REPLY")
                    )
                ),
                description = "ARP reply sent back to requester"
            )
        ))

        // Frame 5: ARP message format
        frames.add(Frame(
            narr = "ARP message: Hardware type, Protocol type, Hardware length (6), Protocol length (4), Operation (1=request, 2=reply).",
            phase = "arp_format",
            stats = mapOf("hw_addr_len" to "6 bytes", "proto_addr_len" to "4 bytes"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("source", "Sender", "host", 0.25f),
                    FlowNode("target", "Target", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "source",
                        to = "target",
                        protocol = "ARP",
                        details = "Sender MAC: AA:BB:CC:DD:EE:01, Sender IP: 192.168.1.10",
                        timestamp = 0
                    )
                ),
                description = "ARP message structure"
            )
        ))

        // Frame 6: Unicast reply optimization
        frames.add(Frame(
            narr = "ARP reply is sent UNICAST (directed) to Host A, not broadcast, saving bandwidth.",
            phase = "unicast_reply",
            stats = mapOf("destination" to "unicast", "bandwidth_saved" to "yes"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "Host A (unicast)", "host", 0.25f),
                    FlowNode("hostB", "Host B (sender)", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "hostB",
                        to = "hostA",
                        protocol = "ARP",
                        details = "AA:BB:CC:DD:EE:02",
                        direction = -1,
                        timestamp = 1,
                        flags = setOf("REPLY", "UNICAST")
                    )
                ),
                description = "Unicast ARP reply conserves bandwidth"
            )
        ))

        // Frame 7: ARP cache learning
        frames.add(Frame(
            narr = "Host A receives reply and learns: IP 192.168.1.50 maps to MAC AA:BB:CC:DD:EE:02. Cached!",
            phase = "arp_cache",
            stats = mapOf("cached_entries" to "1", "ttl" to "4 hours"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "Host A (learned)", "host", 0.25f),
                    FlowNode("hostB", "Host B", "host", 0.75f)
                ),
                packets = emptyList(),
                timeline = listOf(
                    TimelineStep(0, "ARP request sent"),
                    TimelineStep(1, "ARP reply received"),
                    TimelineStep(2, "Entry cached: 192.168.1.50 -> AA:BB:CC:DD:EE:02")
                ),
                description = "ARP cache stores IP-to-MAC mappings"
            )
        ))

        // Frame 8: Subsequent traffic uses cache
        frames.add(Frame(
            narr = "Next time Host A needs to send data to 192.168.1.50, it uses cached MAC address. No ARP request needed!",
            phase = "cache_hit",
            stats = mapOf("arp_requests" to "0", "efficiency" to "high"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "Host A", "host", 0.25f),
                    FlowNode("hostB", "Host B", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "hostA",
                        to = "hostB",
                        protocol = "IP",
                        details = "Dest MAC from cache: AA:BB:CC:DD:EE:02",
                        timestamp = 3
                    )
                ),
                description = "Cached entry avoids repeated ARP requests"
            )
        ))

        // Frame 9: ARP cache timeout
        frames.add(Frame(
            narr = "After 4 hours (typical), ARP cache entry expires. Next packet triggers new ARP request.",
            phase = "cache_timeout",
            stats = mapOf("ttl_seconds" to "14400", "expired" to "yes"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "Host A", "host", 0.25f),
                    FlowNode("hostB", "Host B", "host", 0.75f)
                ),
                packets = emptyList(),
                timeline = listOf(
                    TimelineStep(0, "Entry cached"),
                    TimelineStep(14400, "TTL expired"),
                    TimelineStep(14401, "New ARP request needed")
                ),
                description = "ARP cache entries expire and require refresh"
            )
        ))

        // Frame 10: Gratuitous ARP
        frames.add(Frame(
            narr = "Gratuitous ARP: Host announces itself without being asked. 'I have 192.168.1.50, MAC is AA:BB:CC:DD:EE:02'.",
            phase = "gratuitous",
            stats = mapOf("operation" to "announcement", "unsolicited" to "yes"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "All Hosts", "host", 0.5f),
                    FlowNode("sender", "Announcing Host", "host", 0.5f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "sender",
                        to = "broadcast",
                        protocol = "ARP",
                        details = "I have 192.168.1.50",
                        timestamp = 0,
                        flags = setOf("GRATUITOUS")
                    )
                ),
                description = "Gratuitous ARP unsolicited announcement"
            )
        ))

        // Frame 11: Gratuitous ARP uses
        frames.add(Frame(
            narr = "Uses: IP failover (VIP moving to new hardware), duplicate IP detection, ARP cache update, link layer redundancy.",
            phase = "gratuitous_uses",
            stats = mapOf("use_cases" to "4"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("old_hw", "Old Hardware", "host", 0.25f),
                    FlowNode("new_hw", "New Hardware", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "new_hw",
                        to = "broadcast",
                        protocol = "ARP",
                        details = "I now have 10.0.0.1 (failover)",
                        timestamp = 0,
                        flags = setOf("GRATUITOUS")
                    )
                ),
                description = "Gratuitous ARP enables high availability"
            )
        ))

        // Frame 12: ARP spoofing
        frames.add(Frame(
            narr = "SECURITY: Attacker sends ARP reply claiming to be 192.168.1.1 (gateway). Unsuspecting hosts learn wrong MAC!",
            phase = "arp_spoofing",
            stats = mapOf("vulnerability" to "yes", "impact" to "MITM"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("attacker", "Attacker", "host", 0.5f),
                    FlowNode("victims", "Victim Hosts", "host", 0.75f),
                    FlowNode("gateway", "Real Gateway", "host", 0.25f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "attacker",
                        to = "broadcast",
                        protocol = "ARP",
                        details = "192.168.1.1 is at AA:AA:AA:AA:AA:AA",
                        timestamp = 0,
                        flags = setOf("SPOOFED")
                    )
                ),
                description = "ARP spoofing enables man-in-the-middle attacks"
            )
        ))

        // Frame 13: ARP attack prevention
        frames.add(Frame(
            narr = "Defense: Static ARP entries, ARP inspection, gratuitous ARP validation, DHCP snooping.",
            phase = "arp_defense",
            stats = mapOf("defenses" to "4"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("host", "Protected Host", "host", 0.5f),
                    FlowNode("switch", "L3 Switch", "host", 0.75f)
                ),
                packets = emptyList(),
                description = "ARP protections against spoofing"
            )
        ))

        // Frame 14: Multi-segment network
        frames.add(Frame(
            narr = "ARP request: broadcast only on LOCAL segment (same VLAN/subnet). Does NOT reach other subnets!",
            phase = "arp_scope",
            stats = mapOf("scope" to "local", "cross_subnet" to "router required"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("seg1_host1", "Seg1 Host1", "host", 0.2f),
                    FlowNode("seg1_host2", "Seg1 Host2", "host", 0.35f),
                    FlowNode("router", "Router", "router", 0.5f),
                    FlowNode("seg2_host1", "Seg2 Host1", "host", 0.65f),
                    FlowNode("seg2_host2", "Seg2 Host2", "host", 0.8f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "seg1_host1",
                        to = "broadcast",
                        protocol = "ARP",
                        details = "Who has 192.168.1.50? (Segment 1 only)",
                        timestamp = 0
                    )
                ),
                description = "ARP broadcasts are segment-local"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Summary: ARP maps IP addresses to MAC addresses. Broadcast request, unicast reply, cached for efficiency.",
            phase = "summary",
            stats = mapOf("key_concepts" to "5"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("hostA", "Host A", "host", 0.25f),
                    FlowNode("hostB", "Host B", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "hostA",
                        to = "broadcast",
                        protocol = "ARP",
                        details = "Who has 192.168.1.50?",
                        timestamp = 0,
                        flags = setOf("REQUEST")
                    ),
                    FlowPacket(
                        from = "hostB",
                        to = "hostA",
                        protocol = "ARP",
                        details = "AA:BB:CC:DD:EE:02",
                        direction = -1,
                        timestamp = 1,
                        flags = setOf("REPLY")
                    )
                ),
                description = "ARP protocol overview"
            )
        ))

        return frames
    }
}
