package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing NULL pointers and segmentation faults.
 * Visualizes: pointer initialization, NULL checks, crash scenarios.
 */
object NullPointersRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Uninitialized pointer
        frames.add(Frame(
            narr = "An uninitialized pointer contains garbage (random address). Very dangerous!",
            phase = "uninitialized",
            stats = mapOf("ptr_value" to "garbage", "safe" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0xDEADBEEF", "int* ptr (uninitialized)", setOf("stack", "active"))
                ),
                legend = "Uninitialized pointer contains garbage"
            )
        ))

        // Frame 2: NULL pointer definition
        frames.add(Frame(
            narr = "NULL is a special value (address 0x00000000). Means 'pointer points nowhere'.",
            phase = "null_definition",
            stats = mapOf("null_value" to "0x00000000", "meaning" to "invalid"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "RESERVED", "[kernel space]", setOf("data")),
                    MemoryCell("0x7FFF0010", "0x00000000", "int* ptr = NULL", setOf("stack", "active"))
                ),
                legend = "NULL pointer = address 0x00000000"
            )
        ))

        // Frame 3: Pointer initialization with NULL
        frames.add(Frame(
            narr = "Best practice: initialize pointers to NULL. Safe until you assign a valid address.",
            phase = "null_init",
            stats = mapOf("initialized" to "yes", "valid" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "int* ptr = NULL", setOf("stack"))
                ),
                legend = "Initializing pointer to NULL"
            )
        ))

        // Frame 4: Valid pointer after malloc
        frames.add(Frame(
            narr = "After malloc(), pointer holds valid address (e.g., 0x55558000 on heap).",
            phase = "valid_allocation",
            stats = mapOf("ptr_valid" to "yes", "allocated" to "100 bytes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr = malloc(100)", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "[allocated]", "heap memory", setOf("heap", "active"))
                ),
                legend = "Valid pointer after malloc()"
            )
        ))

        // Frame 5: Dereferencing valid pointer
        frames.add(Frame(
            narr = "Dereferencing valid pointer works. *ptr reads the value at that address.",
            phase = "valid_dereference",
            stats = mapOf("dereference" to "safe", "value" to "accessible"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "42", "*ptr = 42", setOf("heap", "active"))
                ),
                legend = "Dereferencing valid pointer"
            )
        ))

        // Frame 6: Dereferencing NULL pointer
        frames.add(Frame(
            narr = "CRASH! Dereferencing NULL causes SEGMENTATION FAULT. OS blocks memory access.",
            phase = "null_dereference",
            stats = mapOf("dereference_null" to "fatal", "result" to "segfault"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "int* ptr = NULL", setOf("stack", "active")),
                    MemoryCell("0x00000000", "CRASH!", "SEGMENTATION FAULT", setOf("data"))
                ),
                legend = "Dereferencing NULL pointer = segmentation fault"
            )
        ))

        // Frame 7: Segmentation fault explanation
        frames.add(Frame(
            narr = "Segmentation fault: OS Memory Management Unit (MMU) prevents illegal access.",
            phase = "segfault_explanation",
            stats = mapOf("protection" to "enabled", "os_role" to "protector"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "PROTECTED", "Kernel Space", setOf("data")),
                    MemoryCell("0x7FFF0010", "NULL", "User Space", setOf("stack", "active"))
                ),
                legend = "OS MMU blocks access to protected memory"
            )
        ))

        // Frame 8: Random pointer dereference
        frames.add(Frame(
            narr = "Uninitialized pointer = garbage address. Dereferencing reads/writes random memory!",
            phase = "garbage_dereference",
            stats = mapOf("address" to "random", "corruption" to "likely"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0xDEADBEEF", "int* ptr (garbage)", setOf("stack", "active")),
                    MemoryCell("0xDEADBEEF", "corrupted", "unknown memory", setOf("data"))
                ),
                legend = "Garbage pointer dereference = memory corruption"
            )
        ))

        // Frame 9: Pointer before NULL check
        frames.add(Frame(
            narr = "WRONG: Using pointer without checking if NULL. Unsafe code.",
            phase = "wrong_usage",
            stats = mapOf("safe" to "no", "pattern" to "dangerous"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "int* ptr = NULL", setOf("stack", "active")),
                    MemoryCell("0x00000000", "CRASH!", "dereferencing NULL", setOf("data"))
                ),
                legend = "WRONG: No NULL check before dereference"
            )
        ))

        // Frame 10: Pointer with NULL check
        frames.add(Frame(
            narr = "RIGHT: Always check NULL before dereferencing. Prevents crash.",
            phase = "safe_usage",
            stats = mapOf("safe" to "yes", "pattern" to "defensive"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "int* ptr = NULL", setOf("stack")),
                    MemoryCell("0x7FFF0014", "true", "if(ptr != NULL)", setOf("stack", "active"))
                ),
                legend = "RIGHT: NULL check before dereferencing"
            )
        ))

        // Frame 11: Dangling pointer (use-after-free precursor)
        frames.add(Frame(
            narr = "After free(), pointer still contains old address (dangling). Still dangerous!",
            phase = "dangling_pointer",
            stats = mapOf("freed" to "yes", "pointer_valid" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr (freed)", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "[freed]", "use-after-free", setOf("heap", "active"))
                ),
                legend = "Dangling pointer after free()"
            )
        ))

        // Frame 12: Setting freed pointer to NULL
        frames.add(Frame(
            narr = "After free(), set ptr = NULL to prevent use-after-free. Good practice.",
            phase = "null_after_free",
            stats = mapOf("freed" to "yes", "safe" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "ptr = NULL (after free)", setOf("stack"))
                ),
                legend = "Setting freed pointer to NULL prevents bugs"
            )
        ))

        // Frame 13: NULL in C standard
        frames.add(Frame(
            narr = "In C, NULL is a macro that expands to 0. It's defined in <stddef.h>.",
            phase = "null_standard",
            stats = mapOf("definition" to "(void*)0", "header" to "stddef.h"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "#define NULL ((void*)0)", setOf("data"))
                ),
                legend = "NULL is a standard macro in C"
            )
        ))

        // Frame 14: Distinguishing error cases
        frames.add(Frame(
            narr = "malloc() returns NULL on failure. Check before using! Don't assume allocation succeeds.",
            phase = "malloc_failure",
            stats = mapOf("allocation_success" to "no", "return_value" to "NULL"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "ptr = malloc(1000000000)", setOf("stack", "active")),
                    MemoryCell("0x00000000", "OOM", "Out of Memory", setOf("data"))
                ),
                legend = "malloc() returns NULL if allocation fails"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Summary: NULL = 0x00000000, init to NULL, check before deref, free() then set NULL.",
            phase = "summary",
            stats = mapOf("key_practices" to "4"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "int* ptr = NULL", setOf("stack")),
                    MemoryCell("0x7FFF0014", "check", "if (ptr != NULL)", setOf("stack", "active"))
                ),
                legend = "NULL pointer safety practices"
            )
        ))

        return frames
    }
}
