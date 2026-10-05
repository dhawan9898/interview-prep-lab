package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object BGPRunner {
    fun run(): List<Frame> = listOf(
        Frame("BGP (Border Gateway Protocol): exterior routing protocol. Connects autonomous systems (ASes) on the Internet.", "intro",
            mapOf("scope" to "interdomain", "asn" to "AS numbers")),

        Frame("AS (Autonomous System): network under single administration. Has unique AS Number (ASN). Examples: AS64512 (Google), AS16509 (AWS).", "as",
            mapOf("examples" to "Google, AWS", "identifier" to "ASN")),

        Frame("BGP routers (eBGP): peer across AS boundaries. iBGP: peers within same AS. Propagate routes = reachability info.", "bgp_types",
            mapOf("ebgp" to "external", "ibgp" to "internal")),

        Frame("BGP advertisement: announces prefix + path of ASes. Example: 'I can reach 8.8.0.0/16 via AS1 → AS2'.", "advertisement",
            mapOf("includes" to "prefix, AS path", "example" to "AS64512 → AS64513 → AS16509")),

        Frame("Best path selection: prefer shortest AS path. Tie-break: local preference, origin code, MED (multi-exit discriminator).", "best_path",
            mapOf("primary" to "AS path length", "tiebreakers" to "3+")),

        Frame("Route filtering: policies control which routes advertise/accept. Prevent undesired traffic, ensure compliance.", "filtering",
            mapOf("tool" to "prefix lists, AS path filters", "purpose" to "traffic engineering")),

        Frame("BGP convergence: slow (minutes). Due to policy loop detection, conservative advertisement rules.", "convergence",
            mapOf("time" to "slow (minutes)", "reason" to "policy checks")),

        Frame("Communities: attach tags to routes for policy coordination. Example: 'customer routes' vs 'peer routes' -> different treatment.", "communities",
            mapOf("use" to "route tagging, policy")),

        Frame("BGP powers Internet routing. ASes peer bilaterally/multilaterally. No central authority—fully distributed.", "summary",
            mapOf("scale" to "global Internet", "autonomy" to "fully distributed"))
    )
}
