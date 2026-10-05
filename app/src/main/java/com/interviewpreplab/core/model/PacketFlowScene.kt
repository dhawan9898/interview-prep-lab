package com.interviewpreplab.core.model

/**
 * Represents a packet flow/sequence diagram for network protocol visualization.
 * Shows packets flowing between nodes over time (e.g., TCP handshake, ARP exchange).
 *
 * @param nodes Network endpoints (Client, Server, Router, etc.)
 * @param packets Messages flowing between nodes with protocol details
 * @param timeline Sequence of events showing packet progression
 * @param description Visual legend describing the diagram
 */
data class PacketFlowScene(
    val nodes: List<FlowNode>,
    val packets: List<FlowPacket>,
    val timeline: List<TimelineStep> = emptyList(),
    val description: String = "Packet Exchange"
) : Scene

/**
 * A network node in a packet flow diagram.
 *
 * @param id Unique identifier (e.g., "client", "server", "router-1")
 * @param label Display name (e.g., "Client", "Server", "Router 1")
 * @param type Node type for styling (e.g., "client", "server", "router", "host")
 * @param position Y position for vertical layout (0.0 to 1.0)
 */
data class FlowNode(
    val id: String,
    val label: String,
    val type: String = "host",
    val position: Float = 0.5f
)

/**
 * A packet flowing between two nodes.
 *
 * @param from Source node ID
 * @param to Destination node ID
 * @param protocol Protocol name (e.g., "TCP", "ARP", "ICMP", "DNS")
 * @param details Protocol-specific details (e.g., "SYN(seq=1000, ack=0)")
 * @param direction 1 = left-to-right, -1 = right-to-left
 * @param timestamp Order in sequence (determines Y position)
 * @param flags Optional flags (e.g., "SYN", "ACK", "FIN", "RST")
 * @param size Packet size in bytes (for annotation)
 */
data class FlowPacket(
    val from: String,
    val to: String,
    val protocol: String,
    val details: String,
    val direction: Int = 1,
    val timestamp: Int = 0,
    val flags: Set<String> = emptySet(),
    val size: Int = 0
)

/**
 * A step in the timeline showing what happens at a specific point.
 *
 * @param time Time or sequence number
 * @param description Human-readable explanation of this step
 * @param highlightedPacketIndex Which packet (if any) is being highlighted
 * @param state Machine state at this point (e.g., "ESTABLISHED")
 */
data class TimelineStep(
    val time: Int,
    val description: String,
    val highlightedPacketIndex: Int? = null,
    val state: String = ""
)

/**
 * Represents TCP connection states for handshake visualization.
 */
object TCPStates {
    const val LISTEN = "LISTEN"
    const val SYN_SENT = "SYN_SENT"
    const val SYN_RECEIVED = "SYN_RECEIVED"
    const val ESTABLISHED = "ESTABLISHED"
    const val FIN_WAIT_1 = "FIN_WAIT_1"
    const val CLOSE_WAIT = "CLOSE_WAIT"
    const val CLOSING = "CLOSING"
    const val TIME_WAIT = "TIME_WAIT"
    const val CLOSED = "CLOSED"
}

/**
 * Represents common TCP flags.
 */
object TCPFlags {
    const val SYN = "SYN"
    const val ACK = "ACK"
    const val FIN = "FIN"
    const val RST = "RST"
    const val PSH = "PSH"
    const val URG = "URG"
}

/**
 * Represents ARP message types.
 */
object ARPTypes {
    const val REQUEST = "REQUEST"
    const val REPLY = "REPLY"
    const val GRATUITOUS = "GRATUITOUS"
}
