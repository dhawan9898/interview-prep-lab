package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object LongestPrefixMatchRunner {
    fun run(): List<Frame> = listOf(
        Frame("Longest-prefix match: routing algorithm. If multiple routes match, use the one with longest prefix.", "intro",
            mapOf("algorithm" to "LPM", "goal" to "find best route")),

        Frame("Routing table example: 10.0.0.0/8 → Port1, 10.1.0.0/16 → Port2, 10.1.2.0/24 → Port3.", "table",
            mapOf("routes" to "3", "longest_prefix" to "/24")),

        Frame("Packet dest 10.1.2.5: matches /8 (yes), /16 (yes), /24 (yes). Use /24 (longest prefix).", "matching",
            mapOf("dest" to "10.1.2.5", "all_match" to "true", "chosen_prefix" to "/24")),

        Frame("Packet dest 10.2.0.0: matches /8 (yes), /16 (no), /24 (no). Use /8.", "no_exact_match",
            mapOf("dest" to "10.2.0.0", "matches" to "1", "chosen_prefix" to "/8")),

        Frame("Packet dest 192.168.0.0: matches no routes. Use default route 0.0.0.0/0 if exists.", "default_route",
            mapOf("matches" to "0", "fallback" to "default")),

        Frame("Implementation: radix tree (trie on bits) or binary search on sorted routes. Both O(prefix length).", "implementation",
            mapOf("data_structure" to "trie or binary search", "complexity" to "O(prefix_length)")),

        Frame("Hardware (TCAM): ternary CAM matches /0 to /32 in parallel in one cycle (expensive, fast).", "hardware",
            mapOf("device" to "TCAM", "time" to "O(1)", "cost" to "high")),

        Frame("Software (Linux kernel): uses trie (fib_trie) or hash table. Updated when route changes.", "software",
            mapOf("kernel" to "Linux", "structure" to "trie")),

        Frame("Longest-prefix match ensures hierarchical routing: /24 for local subnet, /16 for region, /8 for country.", "summary",
            mapOf("hierarchy" to "true", "efficiency" to "high"))
    )
}
