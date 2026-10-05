package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryPointer
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing use-after-free vulnerabilities.
 * Visualizes: allocation, deallocation, dangling pointers, memory reuse, corruption.
 */
object UseAfterFreeRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Valid memory allocation
        frames.add(Frame(
            narr = "ptr = malloc(100) allocates memory on heap. ptr points to valid memory.",
            phase = "allocation",
            stats = mapOf("allocated" to "100 bytes", "valid" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "[valid]", "malloc(100)", setOf("heap", "active"))
                ),
                legend = "Valid heap allocation"
            )
        ))

        // Frame 2: Using allocated memory
        frames.add(Frame(
            narr = "*ptr = 42 writes to allocated memory. Safe and correct.",
            phase = "valid_use",
            stats = mapOf("operation" to "write", "safety" to "safe"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "42", "allocated data", setOf("heap", "active"))
                ),
                legend = "Using allocated memory safely"
            )
        ))

        // Frame 3: Freeing memory
        frames.add(Frame(
            narr = "free(ptr) deallocates memory. Heap manager marks it as available.",
            phase = "deallocation",
            stats = mapOf("freed" to "yes", "ptr_valid" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr (dangling)", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "[freed]", "now available", setOf("heap", "active"))
                ),
                legend = "After free() - memory is deallocated"
            )
        ))

        // Frame 4: Dangling pointer
        frames.add(Frame(
            narr = "ptr still contains address 0x55558000, but memory is freed. DANGLING pointer!",
            phase = "dangling_pointer",
            stats = mapOf("pointer_contains" to "0x55558000", "memory_state" to "freed"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr (dangling)", setOf("stack", "pointer", "active"))
                ),
                legend = "Dangling pointer still holds freed address"
            )
        ))

        // Frame 5: Use-after-free access
        frames.add(Frame(
            narr = "x = *ptr reads from freed memory. Undefined behavior - might read garbage!",
            phase = "uaf_read",
            stats = mapOf("operation" to "dereference", "memory_state" to "freed", "undefined" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "???", "freed memory (garbage)", setOf("heap", "active"))
                ),
                legend = "Use-after-free: reading from freed memory"
            )
        ))

        // Frame 6: Heap reallocation
        frames.add(Frame(
            narr = "Heap manager reuses freed space. ptr2 = malloc(50) gets same address 0x55558000.",
            phase = "reallocation",
            stats = mapOf("reused_address" to "0x55558000", "new_owner" to "ptr2"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr (freed)", setOf("stack", "pointer")),
                    MemoryCell("0x7FFF0014", "0x55558000", "int* ptr2 (allocated)", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "[ptr2 data]", "new allocation", setOf("heap", "active"))
                ),
                legend = "Heap reuses freed memory for new allocation"
            )
        ))

        // Frame 7: Unexpected data modification
        frames.add(Frame(
            narr = "*ptr = 100 writes to same address, but now it's ptr2's data! Corruption!",
            phase = "corruption",
            stats = mapOf("victim" to "ptr2", "corrupted" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr (freed)", setOf("stack", "pointer")),
                    MemoryCell("0x7FFF0014", "0x55558000", "int* ptr2", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "100", "ptr2 data CORRUPTED", setOf("heap", "active"))
                ),
                legend = "Use-after-free corrupts reused memory"
            )
        ))

        // Frame 8: Information disclosure
        frames.add(Frame(
            narr = "x = *ptr reads from freed memory now owned by ptr2. Info leak!",
            phase = "info_leak",
            stats = mapOf("leaked_data" to "ptr2_contents", "confidentiality" to "broken"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr (freed)", setOf("stack", "pointer", "active")),
                    MemoryCell("0x7FFF0014", "0x55558000", "int* ptr2", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "0x12345678", "leaked secret", setOf("heap"))
                ),
                legend = "Use-after-free causes information disclosure"
            )
        ))

        // Frame 9: Control flow hijack
        frames.add(Frame(
            narr = "If freed memory held function pointer, use-after-free can redirect control flow!",
            phase = "control_hijack",
            stats = mapOf("impact" to "code_execution", "severity" to "critical"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "0xDEADBEEF", "function pointer (hijacked)", setOf("heap", "active"))
                ),
                legend = "Use-after-free on function pointer"
            )
        ))

        // Frame 10: Double free vulnerability
        frames.add(Frame(
            narr = "Worse: free(ptr) twice! Heap corruption - memory manager's metadata corrupted.",
            phase = "double_free",
            stats = mapOf("freed_count" to "2", "heap_state" to "corrupted"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr", setOf("stack", "pointer", "active")),
                    MemoryCell("0x55558000", "[freed twice!]", "heap corruption", setOf("heap"))
                ),
                legend = "Double free corrupts heap management"
            )
        ))

        // Frame 11: Prevention - set to NULL
        frames.add(Frame(
            narr = "After free(ptr), set ptr = NULL. Prevents accidental dereference!",
            phase = "null_after_free",
            stats = mapOf("defense" to "null_assignment", "effectiveness" to "good"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "ptr = NULL (after free)", setOf("stack", "active"))
                ),
                legend = "Setting to NULL after free prevents use-after-free"
            )
        ))

        // Frame 12: Prevention - reduce scope
        frames.add(Frame(
            narr = "Scope: free() as soon as done. Minimize time pointer is valid.",
            phase = "scope_reduction",
            stats = mapOf("strategy" to "early_free", "risk" to "reduced"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "ptr (limited scope)", setOf("stack", "pointer"))
                ),
                legend = "Limiting pointer scope reduces risk"
            )
        ))

        // Frame 13: Memory tagging (Arm MTE)
        frames.add(Frame(
            narr = "Arm MTE: hardware tags memory. Use-after-free detected at dereference!",
            phase = "mte",
            stats = mapOf("defense" to "mte", "detection" to "hardware"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[tag mismatch]", "MTE detects freed memory", setOf("heap", "active"))
                ),
                legend = "Arm MTE: hardware memory tagging"
            )
        ))

        // Frame 14: Use-after-free vs null pointer
        frames.add(Frame(
            narr = "NULL crash is obvious (segfault). Use-after-free is silent - data corrupts first!",
            phase = "comparison",
            stats = mapOf("null_obvious" to "yes", "uaf_sneaky" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "CRASH!", "NULL dereference", setOf("data")),
                    MemoryCell("0x55558000", "silent", "use-after-free", setOf("heap", "active"))
                ),
                legend = "Use-after-free is harder to detect than NULL"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Use-after-free: free() then deref = corruption/leak/hijack. Set NULL, limit scope, use MTE.",
            phase = "summary",
            stats = mapOf("defenses" to "3", "severity" to "critical"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "ptr = NULL", setOf("stack")),
                    MemoryCell("0x55558000", "[freed]", "heap", setOf("heap", "active"))
                ),
                legend = "Use-after-free prevention and detection"
            )
        ))

        return frames
    }
}
