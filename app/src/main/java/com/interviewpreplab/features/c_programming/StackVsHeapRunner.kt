package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryPointer
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames comparing stack and heap memory allocation.
 * Visualizes: automatic vs manual allocation, LIFO, fragmentation, lifecycle.
 */
object StackVsHeapRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Local variable on stack
        frames.add(Frame(
            narr = "When you declare a local variable, it goes on the STACK. Automatic allocation.",
            phase = "stack_allocation",
            stats = mapOf("memory" to "stack", "allocation" to "automatic"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x", setOf("stack", "active"))
                ),
                legend = "Local variable on stack (automatic)"
            )
        ))

        // Frame 2: Function call adds to stack
        frames.add(Frame(
            narr = "When a function is called, its parameters and locals are pushed onto the stack.",
            phase = "function_call",
            stats = mapOf("stack_depth" to "2", "locals" to "3"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x (main)", setOf("stack")),
                    MemoryCell("0x7FFF0014", "3.14", "float y (main)", setOf("stack")),
                    MemoryCell("0x7FFF0018", "100", "int z (foo)", setOf("stack", "active"))
                ),
                legend = "Stack growing downward as functions call each other"
            )
        ))

        // Frame 3: Return pops from stack
        frames.add(Frame(
            narr = "When a function returns, its locals are POPPED off the stack. Auto-freed.",
            phase = "function_return",
            stats = mapOf("freed_automatically" to "yes", "stack_depth" to "1"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x (main)", setOf("stack", "active")),
                    MemoryCell("0x7FFF0014", "3.14", "float y (main)", setOf("stack"))
                ),
                legend = "Function return pops locals from stack"
            )
        ))

        // Frame 4: Dynamic allocation with malloc
        frames.add(Frame(
            narr = "malloc() allocates memory on the HEAP. Manual allocation - pointer on stack.",
            phase = "heap_allocation",
            stats = mapOf("memory" to "heap", "allocation" to "manual"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "[100 bytes]", "malloc(100)", setOf("heap", "active"))
                ),
                pointers = listOf(MemoryPointer("0x7FFF0010", "0x55558000", "ptr")),
                legend = "Pointer on stack, data on heap"
            )
        ))

        // Frame 5: Heap grows upward
        frames.add(Frame(
            narr = "As you malloc more, heap grows upward. Multiple allocations coexist.",
            phase = "multiple_allocations",
            stats = mapOf("allocations" to "3", "fragmentation" to "low"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[100 bytes]", "ptr1=malloc(100)", setOf("heap")),
                    MemoryCell("0x55558064", "[50 bytes]", "ptr2=malloc(50)", setOf("heap")),
                    MemoryCell("0x55558096", "[200 bytes]", "ptr3=malloc(200)", setOf("heap", "active"))
                ),
                legend = "Multiple heap allocations"
            )
        ))

        // Frame 6: free() deallocates
        frames.add(Frame(
            narr = "free(ptr) deallocates. Manual responsibility - forgetting causes memory leak.",
            phase = "free_allocation",
            stats = mapOf("freed" to "1", "remaining" to "2"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[free]", "freed", setOf("heap")),
                    MemoryCell("0x55558064", "[50 bytes]", "ptr2 (still allocated)", setOf("heap")),
                    MemoryCell("0x55558096", "[200 bytes]", "ptr3 (still allocated)", setOf("heap", "active"))
                ),
                legend = "After free() - hole in heap"
            )
        ))

        // Frame 7: Heap fragmentation
        frames.add(Frame(
            narr = "Repeated malloc/free creates fragmentation. Free blocks scattered throughout.",
            phase = "fragmentation",
            stats = mapOf("free_blocks" to "3", "utilization" to "65%"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[32 bytes]", "allocated", setOf("heap")),
                    MemoryCell("0x55558020", "[16 bytes]", "FREE", setOf("data")),
                    MemoryCell("0x55558030", "[48 bytes]", "allocated", setOf("heap")),
                    MemoryCell("0x55558060", "[8 bytes]", "FREE", setOf("data")),
                    MemoryCell("0x55558068", "[64 bytes]", "allocated", setOf("heap", "active"))
                ),
                legend = "Heap fragmentation from repeated alloc/free"
            )
        ))

        // Frame 8: Stack vs Heap - Speed
        frames.add(Frame(
            narr = "Stack is fast: O(1) allocation (just move pointer). Heap is slow: O(log n) search for free block.",
            phase = "speed_comparison",
            stats = mapOf("stack_speed" to "nanoseconds", "heap_speed" to "microseconds"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "instant", "Stack (push)", setOf("stack", "active")),
                    MemoryCell("0x55558000", "slow", "Heap (search)", setOf("heap"))
                ),
                legend = "Stack allocation is much faster than heap"
            )
        ))

        // Frame 9: Stack vs Heap - Size
        frames.add(Frame(
            narr = "Stack: Limited (usually 1-8 MB). Heap: Large (limited by RAM).",
            phase = "size_comparison",
            stats = mapOf("stack_size" to "8MB", "heap_size" to "4GB"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "8 MB limit", "Stack", setOf("stack")),
                    MemoryCell("0x55558000", "large", "Heap", setOf("heap", "active"))
                ),
                legend = "Stack limited, heap large"
            )
        ))

        // Frame 10: Stack overflow
        frames.add(Frame(
            narr = "Stack OVERFLOW: Recursion or large arrays cause stack to run out. Crash!",
            phase = "stack_overflow",
            stats = mapOf("risk" to "high", "cause" to "infinite_recursion"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "nearly full!", "Stack exhausted", setOf("stack", "active")),
                    MemoryCell("0x7FFF0100", "CRASH!", "No space left", setOf("data"))
                ),
                legend = "Stack overflow from deep recursion"
            )
        ))

        // Frame 11: Stack safety
        frames.add(Frame(
            narr = "Stack is SAFE: auto-freed when function exits. No memory leak possible.",
            phase = "stack_safety",
            stats = mapOf("automatic_cleanup" to "yes", "memory_leak" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x (auto)", setOf("stack")),
                    MemoryCell("0x7FFF0014", "3.14", "float y (auto)", setOf("stack"))
                ),
                legend = "Stack variables are automatically freed"
            )
        ))

        // Frame 12: Heap responsibility
        frames.add(Frame(
            narr = "Heap is RISKY: YOU must free(). Forgetting causes memory leak.",
            phase = "heap_responsibility",
            stats = mapOf("manual_management" to "required", "leak_risk" to "high"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[malloc'd]", "forgotten", setOf("heap", "active")),
                    MemoryCell("0x55558020", "[malloc'd]", "forgotten", setOf("heap"))
                ),
                legend = "Heap requires manual free() - memory leak if forgotten"
            )
        ))

        // Frame 13: When to use stack
        frames.add(Frame(
            narr = "Use STACK for: small, fixed-size data (int, struct, array[10]).",
            phase = "stack_usage",
            stats = mapOf("use_case" to "fixed_size", "examples" to "int, float, struct"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x", setOf("stack")),
                    MemoryCell("0x7FFF0014", "3.14", "float pi", setOf("stack")),
                    MemoryCell("0x7FFF0018", "{...}", "struct Point", setOf("stack", "active"))
                ),
                legend = "Stack for small, fixed-size data"
            )
        ))

        // Frame 14: When to use heap
        frames.add(Frame(
            narr = "Use HEAP for: large, variable-size data (arrays, strings, lists, trees).",
            phase = "heap_usage",
            stats = mapOf("use_case" to "variable_size", "examples" to "malloc"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* arr", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "[1000 ints]", "malloc(4000)", setOf("heap", "active"))
                ),
                pointers = listOf(MemoryPointer("0x7FFF0010", "0x55558000", "arr")),
                legend = "Heap for large, variable-size data"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Stack: automatic, fast, safe, limited. Heap: manual, slow, risky, large.",
            phase = "summary",
            stats = mapOf("stack_best" to "small_fixed", "heap_best" to "large_variable"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "auto-freed", "Stack", setOf("stack")),
                    MemoryCell("0x55558000", "manual-free", "Heap", setOf("heap", "active"))
                ),
                legend = "Stack vs Heap summary"
            )
        ))

        return frames
    }
}
