package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene
import com.interviewpreplab.core.model.TCPFlags
import com.interviewpreplab.core.model.TCPStates
import com.interviewpreplab.core.model.TimelineStep

/**
 * Generates frames showing TCP 3-way handshake.
 * Visualizes: SYN, SYN-ACK, ACK, connection establishment, sequence numbers.
 */
object TCPHandshakeRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: TCP connection goal
        frames.add(Frame(
            narr = "TCP establishes reliable connections before data transfer. Goal: synchronize sequence numbers and create reliable path.",
            phase = "tcp_goal",
            stats = mapOf("reliability" to "required", "connection_oriented" to "yes"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client", "host", 0.25f),
                    FlowNode("server", "Server", "host", 0.75f)
                ),
                packets = emptyList(),
                description = "TCP requires connection establishment"
            )
        ))

        // Frame 2: Initial state
        frames.add(Frame(
            narr = "Server is LISTEN state, waiting for connection. Client is CLOSED state, not connected.",
            phase = "initial_state",
            stats = mapOf("server_state" to "LISTEN", "client_state" to "CLOSED"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client (CLOSED)", "host", 0.25f),
                    FlowNode("server", "Server (LISTEN)", "host", 0.75f)
                ),
                packets = emptyList(),
                timeline = listOf(
                    TimelineStep(0, "Server listening on port 80")
                ),
                description = "Initial states before connection"
            )
        ))

        // Frame 3: SYN packet
        frames.add(Frame(
            narr = "Client sends SYN packet: 'I want to connect. My sequence number is 1000.' Client enters SYN_SENT state.",
            phase = "syn_sent",
            stats = mapOf("flag" to "SYN", "seq" to "1000", "ack" to "0"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client (SYN_SENT)", "host", 0.25f),
                    FlowNode("server", "Server (LISTEN)", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "client",
                        to = "server",
                        protocol = "TCP",
                        details = "SYN, seq=1000, ack=0, port 12345→80",
                        timestamp = 0,
                        flags = setOf(TCPFlags.SYN),
                        size = 40
                    )
                ),
                timeline = listOf(
                    TimelineStep(0, "Client sends SYN")
                ),
                description = "SYN packet initiates connection"
            )
        ))

        // Frame 4: SYN-ACK packet
        frames.add(Frame(
            narr = "Server receives SYN. Sends SYN-ACK: 'I got your SYN (ack=1001). My sequence is 2000.' Server enters SYN_RECEIVED.",
            phase = "syn_ack_sent",
            stats = mapOf("flag" to "SYN,ACK", "seq" to "2000", "ack" to "1001"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client (SYN_SENT)", "host", 0.25f),
                    FlowNode("server", "Server (SYN_RECEIVED)", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "client",
                        to = "server",
                        protocol = "TCP",
                        details = "SYN, seq=1000",
                        timestamp = 0,
                        flags = setOf(TCPFlags.SYN)
                    ),
                    FlowPacket(
                        from = "server",
                        to = "client",
                        protocol = "TCP",
                        details = "SYN-ACK, seq=2000, ack=1001",
                        direction = -1,
                        timestamp = 1,
                        flags = setOf(TCPFlags.SYN, TCPFlags.ACK),
                        size = 40
                    )
                ),
                timeline = listOf(
                    TimelineStep(0, "Client sends SYN"),
                    TimelineStep(1, "Server sends SYN-ACK")
                ),
                description = "Server responds with SYN-ACK"
            )
        ))

        // Frame 5: ACK packet
        frames.add(Frame(
            narr = "Client receives SYN-ACK. Sends ACK: 'I got your SYN (ack=2001).' Client enters ESTABLISHED state.",
            phase = "ack_sent",
            stats = mapOf("flag" to "ACK", "seq" to "1001", "ack" to "2001"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client (ESTABLISHED)", "host", 0.25f),
                    FlowNode("server", "Server (SYN_RECEIVED)", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "client",
                        to = "server",
                        protocol = "TCP",
                        details = "ACK, seq=1001, ack=2001",
                        timestamp = 2,
                        flags = setOf(TCPFlags.ACK),
                        size = 40
                    )
                ),
                timeline = listOf(
                    TimelineStep(0, "Client sends SYN"),
                    TimelineStep(1, "Server sends SYN-ACK"),
                    TimelineStep(2, "Client sends ACK")
                ),
                description = "Client confirms with ACK"
            )
        ))

        // Frame 6: Both established
        frames.add(Frame(
            narr = "Server receives ACK and enters ESTABLISHED state. Both sides synchronized! Connection ready for data transfer.",
            phase = "both_established",
            stats = mapOf("client_state" to "ESTABLISHED", "server_state" to "ESTABLISHED"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client (ESTABLISHED)", "host", 0.25f),
                    FlowNode("server", "Server (ESTABLISHED)", "host", 0.75f)
                ),
                packets = emptyList(),
                timeline = listOf(
                    TimelineStep(0, "SYN sent"),
                    TimelineStep(1, "SYN-ACK received"),
                    TimelineStep(2, "ACK sent"),
                    TimelineStep(3, "Connection ESTABLISHED")
                ),
                description = "Both sides are connected and ready"
            )
        ))

        // Frame 7: Sequence number synchronization
        frames.add(Frame(
            narr = "Both sides know each other's sequence numbers. Client will send data with seq=1001, Server expects ack=1001.",
            phase = "seq_sync",
            stats = mapOf("client_seq" to "1001", "server_seq" to "2001"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client\nseq: 1001, expect ack: 2001", "host", 0.25f),
                    FlowNode("server", "Server\nseq: 2001, expect ack: 1001", "host", 0.75f)
                ),
                packets = emptyList(),
                description = "Sequence numbers synchronized"
            )
        ))

        // Frame 8: Data transfer
        frames.add(Frame(
            narr = "Client sends data: 'GET / HTTP/1.1' with seq=1001 (increments by payload size). Server acknowledges.",
            phase = "data_transfer",
            stats = mapOf("direction" to "client→server", "payload_size" to "16 bytes"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client", "host", 0.25f),
                    FlowNode("server", "Server", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "client",
                        to = "server",
                        protocol = "TCP",
                        details = "Data: 'GET / HTTP/1.1', seq=1001, ack=2001",
                        timestamp = 3,
                        flags = setOf(TCPFlags.ACK),
                        size = 56
                    )
                ),
                description = "Data transfer after handshake"
            )
        ))

        // Frame 9: Window size (flow control)
        frames.add(Frame(
            narr = "Each packet includes WINDOW SIZE: how much data receiver can buffer. Example: window=65536 bytes.",
            phase = "window_size",
            stats = mapOf("window" to "65536", "unit" to "bytes"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client\nwindow: 65536", "host", 0.25f),
                    FlowNode("server", "Server\nwindow: 32768", "host", 0.75f)
                ),
                packets = emptyList(),
                description = "Window size enables flow control"
            )
        ))

        // Frame 10: Maximum Segment Size (MSS)
        frames.add(Frame(
            narr = "During handshake, SYN includes MSS (Maximum Segment Size). Example: 1460 bytes for Ethernet.",
            phase = "mss",
            stats = mapOf("mss" to "1460", "calculation" to "1500 - 20 (IP) - 20 (TCP)"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client (MSS: 1460)", "host", 0.25f),
                    FlowNode("server", "Server (MSS: 1460)", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "client",
                        to = "server",
                        protocol = "TCP",
                        details = "SYN, MSS=1460",
                        timestamp = 0,
                        flags = setOf(TCPFlags.SYN)
                    )
                ),
                description = "MSS negotiation during SYN"
            )
        ))

        // Frame 11: TCP options
        frames.add(Frame(
            narr = "TCP options in SYN: MSS, window scaling, SACK, timestamps. Enable advanced features.",
            phase = "tcp_options",
            stats = mapOf("options_count" to "4"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client", "host", 0.25f),
                    FlowNode("server", "Server", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "client",
                        to = "server",
                        protocol = "TCP",
                        details = "SYN + Options (MSS, WSS, SACK, TS)",
                        timestamp = 0,
                        flags = setOf(TCPFlags.SYN)
                    )
                ),
                description = "TCP options enhance performance"
            )
        ))

        // Frame 12: Connection state diagram
        frames.add(Frame(
            narr = "TCP state transitions: LISTEN → SYN_RECEIVED → ESTABLISHED. Reverse for closure.",
            phase = "state_diagram",
            stats = mapOf("states" to "3", "transition_count" to "2"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("listen", "LISTEN", "host", 0.2f),
                    FlowNode("syn_recv", "SYN_RECEIVED", "host", 0.5f),
                    FlowNode("estab", "ESTABLISHED", "host", 0.8f)
                ),
                packets = emptyList(),
                timeline = listOf(
                    TimelineStep(0, "SYN received → SYN_RECEIVED"),
                    TimelineStep(1, "ACK received → ESTABLISHED")
                ),
                description = "TCP connection state transitions"
            )
        ))

        // Frame 13: Simultaneous open (rare)
        frames.add(Frame(
            narr = "RARE: Both sides send SYN simultaneously. Each side receives SYN before SYN-ACK. Both enter ESTABLISHED.",
            phase = "simultaneous_open",
            stats = mapOf("occurrence" to "rare", "handled" to "yes"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("host1", "Host 1", "host", 0.25f),
                    FlowNode("host2", "Host 2", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "host1",
                        to = "host2",
                        protocol = "TCP",
                        details = "SYN",
                        timestamp = 0,
                        flags = setOf(TCPFlags.SYN)
                    ),
                    FlowPacket(
                        from = "host2",
                        to = "host1",
                        protocol = "TCP",
                        details = "SYN",
                        direction = -1,
                        timestamp = 0,
                        flags = setOf(TCPFlags.SYN)
                    )
                ),
                description = "Simultaneous open (edge case)"
            )
        ))

        // Frame 14: SYN flood attack
        frames.add(Frame(
            narr = "SECURITY: Attacker sends thousands of SYN packets with spoofed IPs. Server allocates resources for each. Denial of service!",
            phase = "syn_flood",
            stats = mapOf("vulnerability" to "yes", "attack_type" to "DoS"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("attacker", "Attacker", "host", 0.25f),
                    FlowNode("server", "Server", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "attacker",
                        to = "server",
                        protocol = "TCP",
                        details = "SYN (spoofed IP)",
                        timestamp = 0,
                        flags = setOf(TCPFlags.SYN)
                    ),
                    FlowPacket(
                        from = "attacker",
                        to = "server",
                        protocol = "TCP",
                        details = "SYN (spoofed IP)",
                        timestamp = 1,
                        flags = setOf(TCPFlags.SYN)
                    )
                ),
                description = "SYN flood attack exhausts resources"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Summary: 3-way handshake (SYN, SYN-ACK, ACK) synchronizes sequences and establishes connection before data transfer.",
            phase = "summary",
            stats = mapOf("key_steps" to "3", "time_estimate" to "~1-2 RTT"),
            scene = PacketFlowScene(
                nodes = listOf(
                    FlowNode("client", "Client", "host", 0.25f),
                    FlowNode("server", "Server", "host", 0.75f)
                ),
                packets = listOf(
                    FlowPacket(
                        from = "client",
                        to = "server",
                        protocol = "TCP",
                        details = "SYN",
                        timestamp = 0,
                        flags = setOf(TCPFlags.SYN)
                    ),
                    FlowPacket(
                        from = "server",
                        to = "client",
                        protocol = "TCP",
                        details = "SYN-ACK",
                        direction = -1,
                        timestamp = 1,
                        flags = setOf(TCPFlags.SYN, TCPFlags.ACK)
                    ),
                    FlowPacket(
                        from = "client",
                        to = "server",
                        protocol = "TCP",
                        details = "ACK",
                        timestamp = 2,
                        flags = setOf(TCPFlags.ACK)
                    )
                ),
                description = "TCP 3-way handshake overview"
            )
        ))

        return frames
    }
}
