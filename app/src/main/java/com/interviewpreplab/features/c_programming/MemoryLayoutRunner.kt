package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing C memory layout.
 * Visualizes: Stack, Heap, Data Segment, Code Segment with realistic addresses.
 */
object MemoryLayoutRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Overview
        frames.add(Frame(
            narr = "Memory in C is organized into 4 main segments. Let's explore each one.",
            phase = "overview",
            stats = mapOf(),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "TEXT", "Code Segment", setOf("data")),
                    MemoryCell("0x00001000", "globals", "Data Segment", setOf("data")),
                    MemoryCell("0x55555000", "[heap]", "Heap Start", setOf("heap")),
                    MemoryCell("0x7FFF0000", "locals", "Stack Start", setOf("stack"))
                ),
                legend = "Memory Layout Overview"
            )
        ))

        // Frame 2: Code Segment (Read-Only)
        frames.add(Frame(
            narr = "CODE SEGMENT: Contains executable machine instructions. Read-only, cannot be modified.",
            phase = "code_segment",
            stats = mapOf("segment" to "Code"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "0x90", "start:", setOf("data")),
                    MemoryCell("0x00000001", "0x48", "mov %rax...", setOf("data")),
                    MemoryCell("0x00000002", "0x89", "mov %rdi...", setOf("data")),
                    MemoryCell("0x00000003", "0xE8", "call printf", setOf("data"))
                ),
                legend = "Code Segment (Instructions)"
            )
        ))

        // Frame 3: Data Segment (Initialized Globals)
        frames.add(Frame(
            narr = "DATA SEGMENT: Initialized global and static variables. Values are set at program start.",
            phase = "data_segment",
            stats = mapOf("segment" to "Data", "count" to "3"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x10000000", "42", "int global_x", setOf("data")),
                    MemoryCell("0x10000004", "3.14", "float pi", setOf("data")),
                    MemoryCell("0x10000008", "hello", "char* msg", setOf("data"))
                ),
                legend = "Data Segment (Global Variables)"
            )
        ))

        // Frame 4: BSS Segment (Uninitialized Globals)
        frames.add(Frame(
            narr = "BSS SEGMENT: Uninitialized global variables. Automatically zeroed out at startup.",
            phase = "bss_segment",
            stats = mapOf("segment" to "BSS", "count" to "2"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x20000000", "0", "int arr[100]", setOf("data")),
                    MemoryCell("0x20000100", "0", "char buffer[256]", setOf("data"))
                ),
                legend = "BSS Segment (Uninitialized Globals)"
            )
        ))

        // Frame 5: Heap (Dynamic Allocation)
        frames.add(Frame(
            narr = "HEAP: Used for dynamic memory allocation. Grows upward. Managed with malloc/free.",
            phase = "heap",
            stats = mapOf("segment" to "Heap", "allocated" to "2"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[32 bytes]", "ptr1=malloc(32)", setOf("heap")),
                    MemoryCell("0x55558020", "[free]", "unallocated", setOf("heap")),
                    MemoryCell("0x55558100", "[128 bytes]", "ptr2=malloc(128)", setOf("heap"))
                ),
                legend = "Heap Segment (Dynamic Memory)"
            )
        ))

        // Frame 6: Stack (Local Variables)
        frames.add(Frame(
            narr = "STACK: Local variables and function parameters. Grows downward. Auto-freed when function returns.",
            phase = "stack",
            stats = mapOf("segment" to "Stack", "locals" to "3"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x", setOf("stack")),
                    MemoryCell("0x7FFF0014", "3.14", "float y", setOf("stack")),
                    MemoryCell("0x7FFF0018", "0x55558000", "int* ptr", setOf("stack"))
                ),
                legend = "Stack Segment (Local Variables)"
            )
        ))

        // Frame 7: Stack vs Heap Comparison
        frames.add(Frame(
            narr = "STACK: Limited, fast, auto-freed. HEAP: Large, slower, manual management.",
            phase = "comparison",
            stats = mapOf("stack_speed" to "Fast", "heap_speed" to "Slow"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "auto-freed", "Stack", setOf("stack")),
                    MemoryCell("0x55558000", "manual-free", "Heap", setOf("heap"))
                ),
                legend = "Stack vs Heap"
            )
        ))

        // Frame 8: Full Memory Layout
        frames.add(Frame(
            narr = "Complete memory layout: Code at bottom, Globals/BSS in middle, Heap and Stack at top.",
            phase = "complete",
            stats = mapOf("total_segments" to "4"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "code", "Code Segment", setOf("data")),
                    MemoryCell("0x10000000", "globals", "Data Segment", setOf("data")),
                    MemoryCell("0x20000000", "uninitialized", "BSS Segment", setOf("data")),
                    MemoryCell("0x55558000", "malloc", "Heap (grows ↑)", setOf("heap")),
                    MemoryCell("0x7FFF0000", "locals", "Stack (grows ↓)", setOf("stack"))
                ),
                legend = "Complete Memory Layout"
            )
        ))

        return frames
    }
}
