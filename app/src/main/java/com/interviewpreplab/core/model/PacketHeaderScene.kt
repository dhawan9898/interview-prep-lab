package com.interviewpreplab.core.model

/**
 * Represents a protocol header with individual fields for bit-level visualization.
 * Used to show structure of network protocol headers (Ethernet, IPv4, IPv6, TCP, UDP, etc.).
 *
 * @param protocol Protocol name (e.g., "IPv4", "Ethernet", "TCP", "DNS")
 * @param fields Individual protocol fields with bit positions and values
 * @param totalSize Total header size in bytes
 * @param highlights Map of field names to their meanings
 * @param description Visual legend describing the header
 */
data class PacketHeaderScene(
    val protocol: String,
    val fields: List<HeaderField>,
    val totalSize: Int = 20,
    val highlights: Map<String, String> = emptyMap(),
    val description: String = "Protocol Header"
) : Scene

/**
 * A single field within a protocol header.
 *
 * @param name Field name (e.g., "Version", "IHL", "TTL", "Source IP")
 * @param bitOffset Starting bit position (0-based from start of header)
 * @param bitLength Number of bits this field occupies
 * @param byteOffset Starting byte position (for reference)
 * @param value Decimal or hex value of this field
 * @param meaning Human-readable explanation (e.g., "IPv4", "64 hops")
 * @param role Field role for styling (e.g., "address", "flag", "counter")
 * @param highlighted Whether this field is currently active/selected
 */
data class HeaderField(
    val name: String,
    val bitOffset: Int,
    val bitLength: Int,
    val byteOffset: Int = bitOffset / 8,
    val value: String,
    val meaning: String = "",
    val role: String = "data",
    val highlighted: Boolean = false
)

/**
 * Helper object for common Ethernet header field offsets.
 */
object EthernetFields {
    const val DEST_MAC_BITS = 48
    const val SRC_MAC_BITS = 48
    const val TYPE_BITS = 16
    const val PAYLOAD_MIN_BITS = 46 * 8
    const val FCS_BITS = 32
    const val HEADER_SIZE = 14 // bytes
}

/**
 * Helper object for common IPv4 header field offsets.
 */
object IPv4Fields {
    const val VERSION_BITS = 4
    const val IHL_BITS = 4
    const val DSCP_BITS = 6
    const val ECN_BITS = 2
    const val TOTAL_LENGTH_BITS = 16
    const val IDENTIFICATION_BITS = 16
    const val FLAGS_BITS = 3
    const val FRAGMENT_OFFSET_BITS = 13
    const val TTL_BITS = 8
    const val PROTOCOL_BITS = 8
    const val CHECKSUM_BITS = 16
    const val SRC_IP_BITS = 32
    const val DEST_IP_BITS = 32
    const val HEADER_MIN_SIZE = 20 // bytes
}

/**
 * Helper object for common TCP header field offsets.
 */
object TCPFields {
    const val SRC_PORT_BITS = 16
    const val DEST_PORT_BITS = 16
    const val SEQUENCE_BITS = 32
    const val ACKNOWLEDGEMENT_BITS = 32
    const val DATA_OFFSET_BITS = 4
    const val RESERVED_BITS = 6
    const val FLAGS_BITS = 6
    const val WINDOW_SIZE_BITS = 16
    const val CHECKSUM_BITS = 16
    const val URG_POINTER_BITS = 16
    const val HEADER_MIN_SIZE = 20 // bytes
}

/**
 * TCP flag definitions.
 */
object TCPFlagBits {
    const val URG = 0x20
    const val ACK = 0x10
    const val PSH = 0x08
    const val RST = 0x04
    const val SYN = 0x02
    const val FIN = 0x01
}

/**
 * Helper object for common UDP header field offsets.
 */
object UDPFields {
    const val SRC_PORT_BITS = 16
    const val DEST_PORT_BITS = 16
    const val LENGTH_BITS = 16
    const val CHECKSUM_BITS = 16
    const val HEADER_SIZE = 8 // bytes
}

/**
 * Helper object for common IPv6 header field offsets.
 */
object IPv6Fields {
    const val VERSION_BITS = 4
    const val TRAFFIC_CLASS_BITS = 8
    const val FLOW_LABEL_BITS = 20
    const val PAYLOAD_LENGTH_BITS = 16
    const val NEXT_HEADER_BITS = 8
    const val HOP_LIMIT_BITS = 8
    const val SRC_IP_BITS = 128
    const val DEST_IP_BITS = 128
    const val HEADER_SIZE = 40 // bytes
}

/**
 * IP protocol numbers.
 */
object IPProtocols {
    const val ICMP = 1
    const val TCP = 6
    const val UDP = 17
    const val IGMP = 2
    const val GRE = 47
}
