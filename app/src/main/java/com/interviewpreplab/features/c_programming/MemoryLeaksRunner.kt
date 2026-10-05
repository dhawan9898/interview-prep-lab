package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing memory leak vulnerabilities.
 * Visualizes: allocation without deallocation, unreachable memory, accumulation, detection.
 */
object MemoryLeaksRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Normal malloc-free cycle
        frames.add(Frame(
            narr = "Normal: malloc() allocates, use, free() deallocates. Memory recycled.",
            phase = "normal_cycle",
            stats = mapOf("allocated" to "yes", "freed" to "yes", "leaked" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr = malloc(100)", setOf("stack", "pointer")),
                    MemoryCell("0x55558000", "[used]", "allocated memory", setOf("heap")),
                    MemoryCell("0x7FFF0014", "NULL", "ptr = NULL (after free)", setOf("stack", "active"))
                ),
                legend = "Proper malloc-free cycle"
            )
        ))

        // Frame 2: Forgetting to free
        frames.add(Frame(
            narr = "Forgetting free(): pointer goes out of scope. Memory is now unreachable!",
            phase = "forgotten_free",
            stats = mapOf("freed" to "no", "reachable" to "no", "leaked" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x55558000", "int* ptr (going out of scope)", setOf("stack", "pointer", "active")),
                    MemoryCell("0x55558000", "[orphaned]", "unreachable memory", setOf("heap"))
                ),
                legend = "Memory leak: forgotten free()"
            )
        ))

        // Frame 3: Variable goes out of scope
        frames.add(Frame(
            narr = "Function returns. ptr is popped from stack. No way to reach 0x55558000 anymore!",
            phase = "scope_exit",
            stats = mapOf("ptr_gone" to "yes", "memory_unreachable" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[orphaned]", "unreachable, not freed", setOf("heap", "active"))
                ),
                legend = "Pointer lost, memory permanently unreachable"
            )
        ))

        // Frame 4: Memory leak accumulation
        frames.add(Frame(
            narr = "Each iteration: malloc() but no free(). Heap fills with unreachable blocks.",
            phase = "leak_accumulation",
            stats = mapOf("iteration" to "1000", "leaked_blocks" to "1000"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[leak 1]", "unreachable", setOf("heap")),
                    MemoryCell("0x55558100", "[leak 2]", "unreachable", setOf("heap")),
                    MemoryCell("0x55558200", "[leak 3]", "unreachable", setOf("heap")),
                    MemoryCell("0x55558300", "[leak ...N]", "unreachable", setOf("heap", "active"))
                ),
                legend = "Memory leak accumulation over time"
            )
        ))

        // Frame 5: Out of memory condition
        frames.add(Frame(
            narr = "After enough iterations, heap exhausted. malloc() fails. Program crashes or behaves badly.",
            phase = "oom",
            stats = mapOf("heap_available" to "0%", "malloc_result" to "NULL"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "FULL", "heap exhausted", setOf("heap", "active")),
                    MemoryCell("0x7FFF0010", "0x00000000", "malloc() returned NULL", setOf("stack"))
                ),
                legend = "Out of memory: program failure"
            )
        ))

        // Frame 6: Server memory leak
        frames.add(Frame(
            narr = "On a server: small leak per request × millions of requests = disaster!",
            phase = "server_leak",
            stats = mapOf("scenario" to "server", "requests" to "1000000", "leak_per_request" to "100"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[1M leaks]", "accumulated over time", setOf("heap", "active")),
                    MemoryCell("0x55560000", "OOM crash", "server restarts", setOf("data"))
                ),
                legend = "Server leak: tiny leak × huge scale = catastrophe"
            )
        ))

        // Frame 7: Pointer reassignment leak
        frames.add(Frame(
            narr = "ptr = malloc(100); ptr = malloc(200); lost reference to first block. LEAK!",
            phase = "reassignment_leak",
            stats = mapOf("leak_type" to "pointer_reassignment", "blocks_leaked" to "1"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[orphaned]", "lost pointer", setOf("heap")),
                    MemoryCell("0x55558200", "[valid]", "ptr now points here", setOf("heap", "pointer", "active"))
                ),
                legend = "Reassigning pointer without freeing first"
            )
        ))

        // Frame 8: Error path leak
        frames.add(Frame(
            narr = "malloc() succeeds, but early return on error skips free(). Leak on error path!",
            phase = "error_path_leak",
            stats = mapOf("path" to "error", "allocation" to "yes", "free" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[leaked]", "malloc() but error returns", setOf("heap", "active"))
                ),
                legend = "Error path memory leak"
            )
        ))

        // Frame 9: Exception safety
        frames.add(Frame(
            narr = "If exception thrown between malloc() and free(), leak happens!",
            phase = "exception_leak",
            stats = mapOf("event" to "exception", "cleanup" to "skipped"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[leaked]", "exception thrown before free()", setOf("heap", "active"))
                ),
                legend = "Exception thrown before cleanup"
            )
        ))

        // Frame 10: Circular references
        frames.add(Frame(
            narr = "A->B->A: circular linked list. Neither can be freed (reference count never 0).",
            phase = "circular_reference",
            stats = mapOf("cycle" to "yes", "refcount" to "!=0", "collectible" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "nodeA->nodeB", "pointing to B", setOf("heap")),
                    MemoryCell("0x55558100", "nodeB->nodeA", "pointing back to A", setOf("heap", "active"))
                ),
                legend = "Circular reference memory leak"
            )
        ))

        // Frame 11: Valgrind detection
        frames.add(Frame(
            narr = "Valgrind (memory debugger) detects leaks: 'definitely lost', 'indirectly lost'.",
            phase = "valgrind",
            stats = mapOf("tool" to "valgrind", "detection" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "==1234== Valgrind", "Memcheck output", setOf("data", "active"))
                ),
                legend = "Valgrind detects memory leaks"
            )
        ))

        // Frame 12: AddressSanitizer detection
        frames.add(Frame(
            narr = "AddressSanitizer (ASan): runtime library detects leaks with allocation tracking.",
            phase = "asan",
            stats = mapOf("tool" to "asan", "detection" to "runtime"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "ASAN report", "leaked 100 bytes", setOf("data", "active"))
                ),
                legend = "AddressSanitizer runtime leak detection"
            )
        ))

        // Frame 13: Prevention - always pair malloc-free
        frames.add(Frame(
            narr = "Rule: Every malloc() must have a corresponding free() in all code paths.",
            phase = "pairing_rule",
            stats = mapOf("rule" to "mandatory", "coverage" to "all_paths"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[allocated]", "malloc()", setOf("heap")),
                    MemoryCell("0x00000000", "free()", "all paths", setOf("data", "active"))
                ),
                legend = "Always pair malloc() with free()"
            )
        ))

        // Frame 14: RAII pattern (C++)
        frames.add(Frame(
            narr = "C++: Use RAII. Smart pointers (unique_ptr, shared_ptr) auto-free on scope exit.",
            phase = "smart_pointers",
            stats = mapOf("language" to "cpp", "pattern" to "raii"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "auto-freed", "unique_ptr<int>", setOf("heap", "active"))
                ),
                legend = "Smart pointers prevent memory leaks"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Memory leak: malloc without free leaves unreachable memory. Prevent with pairing, test with Valgrind/ASan.",
            phase = "summary",
            stats = mapOf("prevention" to "pairing", "detection" to "tools"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x55558000", "[freed]", "proper cleanup", setOf("heap")),
                    MemoryCell("0x7FFF0010", "NULL", "pointer cleared", setOf("stack", "active"))
                ),
                legend = "Memory leak prevention and detection"
            )
        ))

        return frames
    }
}
