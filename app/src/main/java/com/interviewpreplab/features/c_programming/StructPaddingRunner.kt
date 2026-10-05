package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing struct padding and memory alignment.
 * Visualizes: field layout, alignment boundaries, padding insertion, sizeof calculations.
 */
object StructPaddingRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Naive struct layout
        frames.add(Frame(
            narr = "If we could pack perfectly: char(1) + int(4) + char(1) = 6 bytes. Seems right?",
            phase = "naive_packing",
            stats = mapOf("fields" to "3", "naive_size" to "6 bytes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "a", "char a (1 byte)", setOf("stack")),
                    MemoryCell("0x7FFF0001", "int", "int b (4 bytes)", setOf("stack")),
                    MemoryCell("0x7FFF0005", "c", "char c (1 byte)", setOf("stack", "active"))
                ),
                legend = "Naive packing: 6 bytes total"
            )
        ))

        // Frame 2: Alignment requirement
        frames.add(Frame(
            narr = "CPU efficiency: int must start at 4-byte boundary. char can be anywhere.",
            phase = "alignment_rule",
            stats = mapOf("int_alignment" to "4 bytes", "char_alignment" to "1 byte"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "BOUNDARY", "4-byte aligned", setOf("stack", "active"))
                ),
                legend = "Alignment requirements for different types"
            )
        ))

        // Frame 3: Misaligned int
        frames.add(Frame(
            narr = "MISALIGNED: int b at offset 1. CPU requires 4-byte alignment. Inefficient!",
            phase = "misaligned",
            stats = mapOf("alignment" to "bad", "efficiency" to "low"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "a", "char a", setOf("stack")),
                    MemoryCell("0x7FFF0001", "!!!!", "int b (misaligned!)", setOf("stack", "active"))
                ),
                legend = "Misaligned int wastes CPU cycles"
            )
        ))

        // Frame 4: Alignment fix - padding inserted
        frames.add(Frame(
            narr = "Compiler inserts padding: char a (offset 0), then 3 padding bytes, then int b (offset 4).",
            phase = "aligned_layout",
            stats = mapOf("offset_a" to "0", "offset_b" to "4", "padding" to "3 bytes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "a", "char a (offset 0)", setOf("stack")),
                    MemoryCell("0x7FFF0001", "PAD", "padding (3 bytes)", setOf("data")),
                    MemoryCell("0x7FFF0004", "bbbb", "int b (offset 4)", setOf("stack", "active")),
                    MemoryCell("0x7FFF0008", "c", "char c (offset 8)", setOf("stack"))
                ),
                legend = "Compiler pads to align int"
            )
        ))

        // Frame 5: Full struct with padding
        frames.add(Frame(
            narr = "Complete struct with padding. Total: 1 + 3(pad) + 4 + 1 = 9 bytes.",
            phase = "complete_struct",
            stats = mapOf("actual_size" to "9 bytes", "fields_only" to "6 bytes", "wasted" to "3 bytes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "a", "char a", setOf("stack")),
                    MemoryCell("0x7FFF0001", "PAD", "padding", setOf("data")),
                    MemoryCell("0x7FFF0004", "bbbb", "int b", setOf("stack")),
                    MemoryCell("0x7FFF0008", "c", "char c", setOf("stack", "active"))
                ),
                legend = "Struct size: 9 bytes (3 wasted to padding)"
            )
        ))

        // Frame 6: Struct alignment
        frames.add(Frame(
            narr = "Struct alignment = largest field alignment. If biggest is int (4), struct aligns to 4.",
            phase = "struct_alignment",
            stats = mapOf("largest_field" to "int (4)", "struct_alignment" to "4 bytes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "struct_a", "aligned to 4-byte", setOf("stack", "active"))
                ),
                legend = "Struct alignment rule"
            )
        ))

        // Frame 7: Struct array padding
        frames.add(Frame(
            narr = "In array: next struct starts at next 4-byte boundary. Each struct takes 9 bytes.",
            phase = "array_layout",
            stats = mapOf("array_size" to "3 structs", "per_struct" to "9 bytes", "gap" to "padding"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "struct[0]", "9 bytes", setOf("stack")),
                    MemoryCell("0x7FFF0009", "PAD", "1 byte padding", setOf("data")),
                    MemoryCell("0x7FFF000A", "struct[1]", "9 bytes", setOf("stack")),
                    MemoryCell("0x7FFF0013", "PAD", "1 byte padding", setOf("data", "active"))
                ),
                legend = "Struct array with padding between elements"
            )
        ))

        // Frame 8: Field reordering optimization
        frames.add(Frame(
            narr = "Reorder: int b first (offset 0), then char a, then char c. Saves padding!",
            phase = "reordered",
            stats = mapOf("original_size" to "9", "reordered_size" to "6"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "bbbb", "int b (offset 0)", setOf("stack")),
                    MemoryCell("0x7FFF0004", "a", "char a (offset 4)", setOf("stack")),
                    MemoryCell("0x7FFF0005", "c", "char c (offset 5)", setOf("stack", "active"))
                ),
                legend = "Reordered struct size: 6 bytes (no wasted padding)"
            )
        ))

        // Frame 9: Pack pragma
        frames.add(Frame(
            narr = "#pragma pack(1): tell compiler NO padding. Be careful - misaligned!",
            phase = "pack_pragma",
            stats = mapOf("padding" to "none", "alignment" to "broken"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "a", "char a", setOf("stack")),
                    MemoryCell("0x7FFF0001", "bbbb", "int b (misaligned!)", setOf("stack", "active")),
                    MemoryCell("0x7FFF0005", "c", "char c", setOf("stack"))
                ),
                legend = "pragma pack(1) forces no padding"
            )
        ))

        // Frame 10: Double misalignment risk
        frames.add(Frame(
            narr = "Misalignment from pragma pack costs CPU performance. Only use if necessary!",
            phase = "perf_risk",
            stats = mapOf("performance" to "degraded", "alignment_faults" to "possible"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0001", "misaligned_int", "CPU penalty", setOf("data", "active"))
                ),
                legend = "Misalignment performance penalty"
            )
        ))

        // Frame 11: Calculating sizeof
        frames.add(Frame(
            narr = "sizeof(struct) = offset of last field + size of last field + trailing padding.",
            phase = "sizeof_calc",
            stats = mapOf("formula" to "last_offset + last_size + trailing_pad"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "a", "offset 0, size 1", setOf("stack")),
                    MemoryCell("0x7FFF0004", "b", "offset 4, size 4", setOf("stack")),
                    MemoryCell("0x7FFF0008", "c", "offset 8, size 1 + 3 pad", setOf("stack", "active"))
                ),
                legend = "sizeof = 9 bytes"
            )
        ))

        // Frame 12: Empty struct
        frames.add(Frame(
            narr = "sizeof(empty struct) = ? GCC: 0. MSVC: 1. Surprising!",
            phase = "empty_struct",
            stats = mapOf("structure" to "empty", "sizeof_gcc" to "0", "sizeof_msvc" to "1"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "???", "empty struct size", setOf("data", "active"))
                ),
                legend = "Empty struct size is compiler-dependent"
            )
        ))

        // Frame 13: Bitfield packing
        frames.add(Frame(
            narr = "Bitfields: pack multiple bools into single int. struct { int a:1; int b:1; } uses 4 bytes.",
            phase = "bitfield",
            stats = mapOf("packing" to "bits", "size" to "4 bytes (not 2)"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "bit0 bit1", "2 bits in 4-byte int", setOf("stack", "active"))
                ),
                legend = "Bitfields pack flags efficiently"
            )
        ))

        // Frame 14: Network packet struct
        frames.add(Frame(
            narr = "Network packets: use pragma pack(1) for wire format. Accept misalignment for correctness.",
            phase = "network_format",
            stats = mapOf("use_case" to "wire_format", "pack" to "required"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "packed", "network header (no padding)", setOf("data", "active"))
                ),
                legend = "Network packets need tight packing"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Padding: CPU aligns for efficiency. Reorder fields to reduce padding. Use pragma pack only if necessary.",
            phase = "summary",
            stats = mapOf("lessons" to "3"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "a", "char", setOf("stack")),
                    MemoryCell("0x7FFF0001", "PAD", "wasted space", setOf("data")),
                    MemoryCell("0x7FFF0004", "b", "int", setOf("stack", "active"))
                ),
                legend = "Struct padding and alignment"
            )
        ))

        return frames
    }
}
