package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing endianness (byte order).
 * Visualizes: big-endian vs little-endian, network byte order, multi-byte values.
 */
object EndiannessRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Multi-byte value storage
        frames.add(Frame(
            narr = "32-bit value 0x12345678 spans 4 bytes. Order matters: which byte comes first?",
            phase = "intro",
            stats = mapOf("value" to "0x12345678", "bytes" to "4"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x12345678", "4-byte integer", setOf("stack", "active"))
                ),
                legend = "Multi-byte value storage"
            )
        ))

        // Frame 2: Big-endian definition
        frames.add(Frame(
            narr = "BIG-ENDIAN: Most significant byte FIRST. 0x12 at low address.",
            phase = "big_endian_intro",
            stats = mapOf("byte_order" to "MSB first", "example" to "Motorola"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x12", "byte 0 (MSB)", setOf("stack")),
                    MemoryCell("0x7FFF0001", "0x34", "byte 1", setOf("stack")),
                    MemoryCell("0x7FFF0002", "0x56", "byte 2", setOf("stack")),
                    MemoryCell("0x7FFF0003", "0x78", "byte 3 (LSB)", setOf("stack", "active"))
                ),
                legend = "Big-endian (MSB first)"
            )
        ))

        // Frame 3: Little-endian definition
        frames.add(Frame(
            narr = "LITTLE-ENDIAN: Least significant byte FIRST. 0x78 at low address.",
            phase = "little_endian_intro",
            stats = mapOf("byte_order" to "LSB first", "example" to "Intel x86"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x78", "byte 0 (LSB)", setOf("stack")),
                    MemoryCell("0x7FFF0001", "0x56", "byte 1", setOf("stack")),
                    MemoryCell("0x7FFF0002", "0x34", "byte 2", setOf("stack")),
                    MemoryCell("0x7FFF0003", "0x12", "byte 3 (MSB)", setOf("stack", "active"))
                ),
                legend = "Little-endian (LSB first)"
            )
        ))

        // Frame 4: Comparison
        frames.add(Frame(
            narr = "Same value 0x12345678, different memory layout. Huge difference!",
            phase = "comparison",
            stats = mapOf("value_same" to "yes", "layout_same" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x12", "Big-Endian", setOf("stack")),
                    MemoryCell("0x7FFF0010", "0x78", "Little-Endian", setOf("stack", "active"))
                ),
                legend = "Same value, different byte orders"
            )
        ))

        // Frame 5: Memory access patterns
        frames.add(Frame(
            narr = "Reading byte-by-byte: Big-endian reads MSB first (natural). Little-endian reads LSB first (reversed).",
            phase = "byte_reading",
            stats = mapOf("read_order" to "different"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x12", "Big: read first", setOf("stack")),
                    MemoryCell("0x7FFF0000", "0x78", "Little: read first", setOf("data", "active"))
                ),
                legend = "Reading byte-by-byte reveals endianness"
            )
        ))

        // Frame 6: Casting pointers pitfall
        frames.add(Frame(
            narr = "Casting int* to char*: first byte differs by endianness!",
            phase = "pointer_cast",
            stats = mapOf("pitfall" to "yes", "portability" to "broken"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x12345678", "int value", setOf("stack")),
                    MemoryCell("0x7FFF0000", "0x12 or 0x78?", "first char depends on endian", setOf("stack", "active"))
                ),
                legend = "Pointer casting reveals endianness"
            )
        ))

        // Frame 7: Network byte order
        frames.add(Frame(
            narr = "Network byte order (Internet Standard): BIG-ENDIAN. TCP/IP uses big-endian.",
            phase = "network_order",
            stats = mapOf("standard" to "big-endian", "domain" to "networking"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "0x12", "network order (MSB first)", setOf("data", "active"))
                ),
                legend = "Network byte order is big-endian"
            )
        ))

        // Frame 8: Host vs Network conversion
        frames.add(Frame(
            narr = "htons(): convert host byte order to network (big-endian). htons(0x1234) = 0x1234 on big-endian, 0x3412 on little-endian.",
            phase = "conversion",
            stats = mapOf("function" to "htons", "purpose" to "byte_order_conversion"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x1234", "host (little)", setOf("stack")),
                    MemoryCell("0x7FFF0010", "0x3412", "network (big)", setOf("stack", "active"))
                ),
                legend = "htons() converts to network byte order"
            )
        ))

        // Frame 9: TCP port serialization
        frames.add(Frame(
            narr = "TCP port 80: serialized as 0x0050 in network format (big-endian).",
            phase = "port_example",
            stats = mapOf("port" to "80", "network_hex" to "0x0050"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "80", "host (decimal)", setOf("stack")),
                    MemoryCell("0x7FFF0010", "0x00 0x50", "network bytes", setOf("stack", "active"))
                ),
                legend = "TCP port in network byte order"
            )
        ))

        // Frame 10: Serialization bug
        frames.add(Frame(
            narr = "Forgetting htons(): sending host-order value as network. On little-endian, bytes reversed!",
            phase = "serialization_bug",
            stats = mapOf("bug" to "endianness", "portability" to "broken"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x1234", "host order", setOf("stack")),
                    MemoryCell("0x7FFF0010", "0x3412", "WRONG (should be 0x1234)", setOf("stack", "active"))
                ),
                legend = "Endianness bug: forgot htons()"
            )
        ))

        // Frame 11: Deserialization
        frames.add(Frame(
            narr = "ntohs(): convert network byte order back to host. ntohs(0x0050) = 80 on little-endian.",
            phase = "deserialization",
            stats = mapOf("function" to "ntohs", "reverse_operation" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x00 0x50", "network bytes", setOf("stack")),
                    MemoryCell("0x7FFF0010", "80", "host (decimal)", setOf("stack", "active"))
                ),
                legend = "ntohs() converts from network byte order"
            )
        ))

        // Frame 12: Struct serialization
        frames.add(Frame(
            narr = "Serializing struct with ints: MUST convert each int field with htons()!",
            phase = "struct_serialization",
            stats = mapOf("fields" to "multiple", "conversion_needed" to "per_field"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "port_htons", "converted", setOf("stack")),
                    MemoryCell("0x7FFF0002", "addr_htonl", "converted", setOf("stack", "active"))
                ),
                legend = "Each int field needs byte order conversion"
            )
        ))

        // Frame 13: File format portability
        frames.add(Frame(
            narr = "File format with binary ints: little-endian machine reads big-endian file → garbage!",
            phase = "file_format_issue",
            stats = mapOf("scenario" to "file_io", "portability" to "broken"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x78563412", "file (big-endian)", setOf("data")),
                    MemoryCell("0x7FFF0000", "0x12345678", "machine (little)", setOf("stack", "active"))
                ),
                legend = "Endianness mismatch in file I/O"
            )
        ))

        // Frame 14: Middle-endian (rare)
        frames.add(Frame(
            narr = "Some rare architectures: PDPs, PowerPC variants use mixed byte ordering. Avoid!",
            phase = "middle_endian",
            stats = mapOf("frequency" to "rare", "recommendation" to "avoid"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "mixed", "non-standard byte order", setOf("data", "active"))
                ),
                legend = "Middle-endian: rare and confusing"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Endianness: Big-endian (network), little-endian (x86). Always use htons/ntohs for network code!",
            phase = "summary",
            stats = mapOf("rule" to "always_convert"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "0x12", "Big-Endian", setOf("stack")),
                    MemoryCell("0x7FFF0010", "0x78", "Little-Endian", setOf("stack", "active"))
                ),
                legend = "Endianness awareness prevents bugs"
            )
        ))

        return frames
    }
}
