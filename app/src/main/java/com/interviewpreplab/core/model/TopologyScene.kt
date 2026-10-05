package com.interviewpreplab.core.model

/**
 * Represents a network topology diagram showing nodes, links, and routing state.
 * Used to visualize LANs, routing topologies, switching paths, etc.
 *
 * @param nodes Network nodes (routers, switches, hosts, etc.)
 * @param links Connections between nodes
 * @param protocols Active routing/switching protocols (OSPF, BGP, STP, etc.)
 * @param highlightedPath Path currently being explained (for routing examples)
 * @param description Visual legend describing the topology
 */
data class TopologyScene(
    val nodes: List<TopoNode>,
    val links: List<TopoLink>,
    val protocols: List<String> = emptyList(),
    val highlightedPath: List<String> = emptyList(),
    val description: String = "Network Topology"
) : Scene

/**
 * A node in a network topology (router, switch, host, etc.).
 *
 * @param id Unique identifier (e.g., "R1", "S1", "host-1")
 * @param label Display name (e.g., "Router 1", "Switch 1")
 * @param type Node type for styling ("router", "switch", "host", "firewall", "gateway")
 * @param ip Primary IP address if applicable
 * @param interfaces Map of interface names to IP addresses (e.g., "eth0" -> "10.0.0.1")
 * @param x X position (0.0 to 1.0) for layout
 * @param y Y position (0.0 to 1.0) for layout
 * @param highlighted Whether this node is currently active/selected
 * @param asNumber Autonomous System number (for BGP routers)
 * @param routingProtocols Protocols this node runs (e.g., "OSPF", "BGP")
 */
data class TopoNode(
    val id: String,
    val label: String,
    val type: String = "host",
    val ip: String? = null,
    val interfaces: Map<String, String> = emptyMap(),
    val x: Float = 0.5f,
    val y: Float = 0.5f,
    val highlighted: Boolean = false,
    val asNumber: Int? = null,
    val routingProtocols: List<String> = emptyList()
)

/**
 * A link between two nodes in a network topology.
 *
 * @param from Source node ID
 * @param to Destination node ID
 * @param bandwidth Link speed (e.g., "1Gbps", "100Mbps", "10Mbps")
 * @param status Link status ("up", "down", "blocked")
 * @param protocol Protocol running on this link (e.g., "OSPF", "BGP", "STP")
 * @param cost OSPF cost/metric if applicable
 * @param highlighted Whether this link is active in highlighted path
 * @param label Optional label for the link
 * @param vlan VLAN ID if this is a tagged trunk
 * @param stp_state STP port state ("forwarding", "blocking", "listening", "learning")
 */
data class TopoLink(
    val from: String,
    val to: String,
    val bandwidth: String? = null,
    val status: String = "up",
    val protocol: String? = null,
    val cost: Int? = null,
    val highlighted: Boolean = false,
    val label: String = "",
    val vlan: Int? = null,
    val stp_state: String? = null
)

/**
 * Node types for styling and behavior.
 */
object TopoNodeTypes {
    const val ROUTER = "router"
    const val SWITCH = "switch"
    const val HOST = "host"
    const val FIREWALL = "firewall"
    const val GATEWAY = "gateway"
    const val SERVER = "server"
    const val WORKSTATION = "workstation"
}

/**
 * Link status values.
 */
object LinkStatus {
    const val UP = "up"
    const val DOWN = "down"
    const val BLOCKED = "blocked"  // STP blocking
    const val WAITING = "waiting"  // Packet in transit
}

/**
 * STP (Spanning Tree Protocol) port states.
 */
object STPStates {
    const val DISABLED = "disabled"
    const val BLOCKING = "blocking"
    const val LISTENING = "listening"
    const val LEARNING = "learning"
    const val FORWARDING = "forwarding"
}

/**
 * STP port roles.
 */
object STPRoles {
    const val ROOT = "root"          // Port on root bridge
    const val DESIGNATED = "designated" // Designated port on segment
    const val ALTERNATE = "alternate"  // Blocked alternate path
    const val BACKUP = "backup"      // Backup to designated port
}

/**
 * Common routing protocols.
 */
object RoutingProtocols {
    const val OSPF = "OSPF"
    const val BGP = "BGP"
    const val RIP = "RIP"
    const val EIGRP = "EIGRP"
    const val ISIS = "IS-IS"
    const val STATIC = "Static"
}

/**
 * Switching/L2 protocols.
 */
object SwitchingProtocols {
    const val STP = "STP"
    const val RSTP = "RSTP"
    const val MSTP = "MSTP"
    const val LACP = "LACP"
    const val LLDP = "LLDP"
}

/**
 * Common port states in L2/L3 networks.
 */
object PortStates {
    const val UP = "up"
    const val DOWN = "down"
    const val DISABLED = "disabled"
    const val SUSPENDED = "suspended"
}
