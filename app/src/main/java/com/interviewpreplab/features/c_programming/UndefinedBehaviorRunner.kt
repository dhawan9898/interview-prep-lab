package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing undefined behavior in C.
 * Visualizes: signed overflow, uninitialized variables, out-of-bounds access, dereferencing invalid pointers.
 */
object UndefinedBehaviorRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: What is undefined behavior
        frames.add(Frame(
            narr = "Undefined Behavior (UB): C standard doesn't specify what happens. Anything could occur!",
            phase = "definition",
            stats = mapOf("standard_guarantees" to "none", "possible_outcomes" to "any"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "UB", "anything can happen", setOf("data", "active"))
                ),
                legend = "Undefined behavior = no guarantees"
            )
        ))

        // Frame 2: Compiler optimization freedom
        frames.add(Frame(
            narr = "Compiler assumes no UB exists. Uses this to optimize aggressively. Wrong if UB happens!",
            phase = "compiler_freedom",
            stats = mapOf("compiler_action" to "optimize", "safety_assumption" to "no_ub"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "compiler", "assumes no UB, optimizes", setOf("data", "active"))
                ),
                legend = "Compiler's freedom to optimize based on no-UB assumption"
            )
        ))

        // Frame 3: Signed integer overflow
        frames.add(Frame(
            narr = "int x = 2147483647; x++; UNDEFINED! Result could be negative, wrap, or crash.",
            phase = "signed_overflow",
            stats = mapOf("value" to "INT_MAX", "operation" to "increment", "behavior" to "undefined"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "2147483647", "INT_MAX", setOf("stack")),
                    MemoryCell("0x7FFF0010", "???", "after ++: undefined", setOf("stack", "active"))
                ),
                legend = "Signed integer overflow is undefined behavior"
            )
        ))

        // Frame 4: Uninitialized variable
        frames.add(Frame(
            narr = "int x; (no initialization). x contains garbage (previous stack contents).",
            phase = "uninitialized",
            stats = mapOf("value" to "garbage", "predictable" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0xDEADBEEF", "uninitialized (garbage)", setOf("stack", "active"))
                ),
                legend = "Uninitialized variable contains garbage"
            )
        ))

        // Frame 5: Using uninitialized value
        frames.add(Frame(
            narr = "if (x > 100) uses garbage. Behavior unpredictable. Different every run!",
            phase = "uninitialized_use",
            stats = mapOf("behavior" to "nondeterministic", "consistent" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "random", "uninitialized x", setOf("stack", "active"))
                ),
                legend = "Using uninitialized variable = unpredictable behavior"
            )
        ))

        // Frame 6: Array out-of-bounds
        frames.add(Frame(
            narr = "int arr[10]; arr[50] = 5; Writing far past boundary. Undefined!",
            phase = "oob_write",
            stats = mapOf("index" to "50", "size" to "10", "violation" to "severe"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "arr[10]", "legitimate", setOf("stack")),
                    MemoryCell("0x7FFF0200", "arr[50]", "way out of bounds", setOf("data", "active"))
                ),
                legend = "Out-of-bounds write is undefined behavior"
            )
        ))

        // Frame 7: Dangling pointer dereference
        frames.add(Frame(
            narr = "free(ptr); x = *ptr; Reading freed memory is undefined behavior.",
            phase = "dangling_deref",
            stats = mapOf("state" to "freed", "dereference" to "undefined"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[freed]", "dangling pointer", setOf("heap", "active"))
                ),
                legend = "Dereferencing dangling pointer is undefined behavior"
            )
        ))

        // Frame 8: Invalid pointer dereference
        frames.add(Frame(
            narr = "int* p = (int*)0x123456; *p; Address likely invalid. Undefined!",
            phase = "invalid_ptr",
            stats = mapOf("validity" to "no", "access" to "illegal"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x123456", "invalid", "random address", setOf("data", "active"))
                ),
                legend = "Dereferencing arbitrary address is undefined behavior"
            )
        ))

        // Frame 9: Division by zero
        frames.add(Frame(
            narr = "x / 0; Undefined! Might crash (SIGFPE), might return 0, or continue.",
            phase = "div_zero",
            stats = mapOf("divisor" to "0", "behavior" to "undefined"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "SIGFPE", "or undefined", setOf("data", "active"))
                ),
                legend = "Division by zero is undefined behavior"
            )
        ))

        // Frame 10: Null pointer dereference
        frames.add(Frame(
            narr = "*NULL; Undefined! Usually crashes, but compiler might assume it won't happen.",
            phase = "null_deref",
            stats = mapOf("address" to "0x00000000", "behavior" to "undefined"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "crash?", "undefined behavior", setOf("data", "active"))
                ),
                legend = "NULL pointer dereference is undefined behavior"
            )
        ))

        // Frame 11: Compiler optimization consequence
        frames.add(Frame(
            narr = "Compiler sees: if (p == NULL) { *p; } Assumes UB never happens, removes NULL check!",
            phase = "optimization_removal",
            stats = mapOf("code_removed" to "null_check", "assumption" to "no_ub"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "removed", "null check deleted by compiler", setOf("data", "active"))
                ),
                legend = "Compiler removes defensive code based on no-UB assumption"
            )
        ))

        // Frame 12: Signal handler race condition
        frames.add(Frame(
            narr = "Variable modified in signal handler AND main code = undefined!",
            phase = "signal_race",
            stats = mapOf("race_condition" to "yes", "behavior" to "undefined"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "???", "data race in variable", setOf("stack", "active"))
                ),
                legend = "Signal handler + main = race condition = undefined"
            )
        ))

        // Frame 13: Unsequenced modifications
        frames.add(Frame(
            narr = "i = i++; Modify and read without sequence point. Undefined!",
            phase = "unsequenced",
            stats = mapOf("operation" to "self_modify", "sequence" to "none"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "i", "modified and read without order", setOf("stack", "active"))
                ),
                legend = "Unsequenced modifications are undefined behavior"
            )
        ))

        // Frame 14: Removing UB
        frames.add(Frame(
            narr = "Fix: Initialize variables, bounds-check arrays, validate pointers, catch edge cases.",
            phase = "defensive_programming",
            stats = mapOf("practice" to "defensive", "effectiveness" to "high"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0", "initialized", setOf("stack")),
                    MemoryCell("0x7FFF0014", "check", "bounds validated", setOf("stack", "active"))
                ),
                legend = "Preventing undefined behavior with defensive programming"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "UB = no guarantees. Compiler assumes it never happens and optimizes accordingly.",
            phase = "summary",
            stats = mapOf("key_ubs" to "10+", "severity" to "critical"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "initialized", "no UB", setOf("stack")),
                    MemoryCell("0x7FFF0014", "checked", "no UB", setOf("stack", "active"))
                ),
                legend = "Avoiding undefined behavior is essential for correct code"
            )
        ))

        return frames
    }
}
