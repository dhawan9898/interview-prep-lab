package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryPointer
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames demonstrating pointer arithmetic.
 * Visualizes: array indexing, pointer increment, offset calculations.
 */
object PointerArithmeticRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Array in memory
        frames.add(Frame(
            narr = "Array arr[3] occupies consecutive memory cells. Each int is 4 bytes.",
            phase = "array_layout",
            stats = mapOf("element_count" to "3", "element_size" to "4 bytes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0]", setOf("stack")),
                    MemoryCell("0x7FFF0014", "20", "arr[1]", setOf("stack")),
                    MemoryCell("0x7FFF0018", "30", "arr[2]", setOf("stack", "active"))
                ),
                legend = "Array stored in consecutive memory"
            )
        ))

        // Frame 2: Pointer to first element
        frames.add(Frame(
            narr = "ptr = &arr[0] points to the first element. arr[0] == *ptr.",
            phase = "pointer_to_array",
            stats = mapOf("ptr_equals" to "&arr[0]", "ptr_value" to "0x7FFF0010"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0] (*ptr)", setOf("stack", "active")),
                    MemoryCell("0x7FFF0014", "20", "arr[1]", setOf("stack")),
                    MemoryCell("0x7FFF0018", "30", "arr[2]", setOf("stack")),
                    MemoryCell("0x7FFF001C", "0x7FFF0010", "int* ptr", setOf("stack", "pointer"))
                ),
                pointers = listOf(
                    MemoryPointer("0x7FFF001C", "0x7FFF0010", "ptr→")
                ),
                legend = "Pointer to first array element"
            )
        ))

        // Frame 3: Pointer increment
        frames.add(Frame(
            narr = "ptr++ moves to NEXT int. Address increases by 4 (int size), NOT 1.",
            phase = "pointer_increment",
            stats = mapOf("ptr_moved_by" to "4 bytes", "new_value" to "0x7FFF0014"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0]", setOf("stack")),
                    MemoryCell("0x7FFF0014", "20", "arr[1] (*ptr)", setOf("stack", "active")),
                    MemoryCell("0x7FFF0018", "30", "arr[2]", setOf("stack")),
                    MemoryCell("0x7FFF001C", "0x7FFF0014", "int* ptr", setOf("stack", "pointer"))
                ),
                pointers = listOf(
                    MemoryPointer("0x7FFF001C", "0x7FFF0014", "ptr→")
                ),
                legend = "After ptr++ - points to arr[1]"
            )
        ))

        // Frame 4: Pointer arithmetic with offset
        frames.add(Frame(
            narr = "ptr + 2 skips 2 elements (8 bytes). Direct access to arr[2].",
            phase = "pointer_offset",
            stats = mapOf("offset" to "2", "total_bytes" to "8"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0]", setOf("stack")),
                    MemoryCell("0x7FFF0014", "20", "arr[1]", setOf("stack")),
                    MemoryCell("0x7FFF0018", "30", "arr[2] (ptr+2)", setOf("stack", "active")),
                    MemoryCell("0x7FFF001C", "0x7FFF0010", "int* ptr", setOf("stack", "pointer"))
                ),
                legend = "Pointer arithmetic: ptr + 2"
            )
        ))

        // Frame 5: Dereference with offset
        frames.add(Frame(
            narr = "*(ptr + 1) dereferences the offset pointer. Gets arr[1] value = 20.",
            phase = "dereference_offset",
            stats = mapOf("operation" to "*(ptr+1)", "result" to "20"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0]", setOf("stack")),
                    MemoryCell("0x7FFF0014", "20", "arr[1] (*(ptr+1))", setOf("stack", "active")),
                    MemoryCell("0x7FFF0018", "30", "arr[2]", setOf("stack")),
                    MemoryCell("0x7FFF001C", "0x7FFF0010", "int* ptr", setOf("stack", "pointer"))
                ),
                pointers = listOf(
                    MemoryPointer("0x7FFF001C", "0x7FFF0014", "ptr+1→")
                ),
                legend = "Dereferencing offset pointer"
            )
        ))

        // Frame 6: Array indexing equivalence
        frames.add(Frame(
            narr = "arr[i] is IDENTICAL to *(ptr + i) where ptr = &arr[0]. They're equivalent!",
            phase = "indexing_equivalence",
            stats = mapOf("arr[i]" to "*(ptr+i)", "equivalent" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0]", setOf("stack")),
                    MemoryCell("0x7FFF0014", "20", "arr[1]", setOf("stack", "active")),
                    MemoryCell("0x7FFF0018", "30", "arr[2]", setOf("stack")),
                    MemoryCell("0x7FFF001C", "0x7FFF0010", "int* ptr = &arr[0]", setOf("stack", "pointer"))
                ),
                legend = "arr[i] == *(ptr+i)"
            )
        ))

        // Frame 7: Pointer subtraction
        frames.add(Frame(
            narr = "ptr2 - ptr1 gives the NUMBER OF ELEMENTS between pointers (not bytes).",
            phase = "pointer_subtraction",
            stats = mapOf("ptr2" to "0x7FFF0018", "ptr1" to "0x7FFF0010", "difference" to "2"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0] (ptr1)", setOf("stack")),
                    MemoryCell("0x7FFF0014", "20", "arr[1]", setOf("stack")),
                    MemoryCell("0x7FFF0018", "30", "arr[2] (ptr2)", setOf("stack", "active"))
                ),
                legend = "Pointer subtraction: ptr2 - ptr1 = 2 elements"
            )
        ))

        // Frame 8: Looping with pointer
        frames.add(Frame(
            narr = "Loop: for(p = &arr[0]; p < &arr[3]; p++) traverses array element-by-element.",
            phase = "pointer_loop",
            stats = mapOf("loop_type" to "pointer", "iterations" to "3"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0]", setOf("stack")),
                    MemoryCell("0x7FFF0014", "20", "arr[1]", setOf("stack")),
                    MemoryCell("0x7FFF0018", "30", "arr[2] (p)", setOf("stack", "active")),
                    MemoryCell("0x7FFF001C", "0x7FFF0018", "int* p", setOf("stack", "pointer"))
                ),
                legend = "Using pointers in loops"
            )
        ))

        // Frame 9: Pointer to char vs int
        frames.add(Frame(
            narr = "char* ptr increments by 1 byte. int* ptr increments by 4 bytes. Type matters!",
            phase = "type_size_matters",
            stats = mapOf("char_increment" to "1", "int_increment" to "4"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "a", "str[0] (char)", setOf("stack")),
                    MemoryCell("0x7FFF0011", "b", "str[1]", setOf("stack")),
                    MemoryCell("0x7FFF0012", "c", "str[2] (p after p++)", setOf("stack", "active")),
                    MemoryCell("0x7FFF0013", "\\0", "str[3]", setOf("stack"))
                ),
                legend = "char* increments by 1 byte"
            )
        ))

        // Frame 10: Out-of-bounds access
        frames.add(Frame(
            narr = "Pointer arithmetic doesn't check bounds. ptr[100] accesses garbage memory!",
            phase = "out_of_bounds",
            stats = mapOf("safe" to "no", "undefined_behavior" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0]", setOf("stack")),
                    MemoryCell("0x7FFF0014", "20", "arr[1]", setOf("stack")),
                    MemoryCell("0x7FFF0018", "30", "arr[2]", setOf("stack")),
                    MemoryCell("0x7FFF0200", "???", "ptr[100] GARBAGE", setOf("data", "active"))
                ),
                legend = "Out-of-bounds pointer access (undefined behavior!)"
            )
        ))

        // Frame 11: Pointer comparison
        frames.add(Frame(
            narr = "You can compare pointers: ptr1 < ptr2 checks if ptr1 points earlier in memory.",
            phase = "pointer_comparison",
            stats = mapOf("ptr1_less_than_ptr2" to "true"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "ptr1", "first", setOf("stack", "active")),
                    MemoryCell("0x7FFF0018", "ptr2", "later", setOf("stack"))
                ),
                legend = "Pointer comparison (relative positions)"
            )
        ))

        // Frame 12: Summary
        frames.add(Frame(
            narr = "Summary: ptr++ skips by sizeof(type), arr[i] == *(ptr+i), subtract pointers for distance.",
            phase = "summary",
            stats = mapOf("key_operations" to "3"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "10", "arr[0]", setOf("stack")),
                    MemoryCell("0x7FFF0014", "20", "arr[1]", setOf("stack")),
                    MemoryCell("0x7FFF0018", "30", "arr[2]", setOf("stack")),
                    MemoryCell("0x7FFF001C", "0x7FFF0010", "int* ptr", setOf("stack", "pointer"))
                ),
                legend = "Pointer arithmetic fundamentals"
            )
        ))

        return frames
    }
}
