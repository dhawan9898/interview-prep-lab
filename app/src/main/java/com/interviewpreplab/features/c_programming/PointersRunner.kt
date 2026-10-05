package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryPointer
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing pointers and addresses in C.
 * Visualizes: address-of (&), dereference (*), pointer storage, and pointer dereferencing.
 */
object PointersRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Simple variable
        frames.add(Frame(
            narr = "Every variable has two properties: an address (where it's stored) and a value (what it contains).",
            phase = "variable_intro",
            stats = mapOf("variable" to "x", "value" to "42"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x", setOf("stack", "active")),
                    MemoryCell("0x7FFF0014", "?", "uninitialized", setOf("stack"))
                ),
                legend = "Variable with address and value"
            )
        ))

        // Frame 2: Address-of operator
        frames.add(Frame(
            narr = "The & (address-of) operator gives us the memory address of a variable. &x = 0x7FFF0010",
            phase = "address_of",
            stats = mapOf("variable" to "x", "address" to "0x7FFF0010"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x", setOf("stack", "active")),
                    MemoryCell("0x7FFF0014", "?", "&x = 0x7FFF0010", setOf("stack"))
                ),
                legend = "Address-of operator (&x)"
            )
        ))

        // Frame 3: Pointer declaration
        frames.add(Frame(
            narr = "A pointer is a variable that stores an address. 'int* ptr' creates a pointer that can point to an int.",
            phase = "pointer_declaration",
            stats = mapOf("type" to "int*", "ptr_value" to "uninitialized"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x", setOf("stack")),
                    MemoryCell("0x7FFF0018", "0x00000000", "int* ptr", setOf("stack", "active"))
                ),
                legend = "Pointer declaration (uninitialized)"
            )
        ))

        // Frame 4: Pointer assignment
        frames.add(Frame(
            narr = "Assignment: ptr = &x. Now ptr stores the address 0x7FFF0010, pointing to variable x.",
            phase = "pointer_assignment",
            stats = mapOf("ptr_points_to" to "x", "address" to "0x7FFF0010"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x", setOf("stack", "active")),
                    MemoryCell("0x7FFF0018", "0x7FFF0010", "int* ptr", setOf("stack", "pointer"))
                ),
                pointers = listOf(
                    MemoryPointer("0x7FFF0018", "0x7FFF0010", "ptr→")
                ),
                legend = "Pointer assignment (ptr = &x)"
            )
        ))

        // Frame 5: Dereference operator
        frames.add(Frame(
            narr = "The * (dereference) operator follows the pointer to get the value. *ptr = 42 (the value of x).",
            phase = "dereference",
            stats = mapOf("ptr_value" to "0x7FFF0010", "dereferenced" to "42"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x (*ptr)", setOf("stack", "active")),
                    MemoryCell("0x7FFF0018", "0x7FFF0010", "int* ptr", setOf("stack", "active"))
                ),
                pointers = listOf(
                    MemoryPointer("0x7FFF0018", "0x7FFF0010", "ptr→")
                ),
                legend = "Dereference operator (*ptr = 42)"
            )
        ))

        // Frame 6: Modify through pointer
        frames.add(Frame(
            narr = "*ptr = 100 changes the value at the address ptr points to. Now both x and *ptr are 100.",
            phase = "modify_through_pointer",
            stats = mapOf("original" to "42", "new_value" to "100"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "100", "int x (*ptr)", setOf("stack", "active")),
                    MemoryCell("0x7FFF0018", "0x7FFF0010", "int* ptr", setOf("stack"))
                ),
                pointers = listOf(
                    MemoryPointer("0x7FFF0018", "0x7FFF0010", "ptr→")
                ),
                legend = "Modifying variable through pointer"
            )
        ))

        // Frame 7: Multiple pointers
        frames.add(Frame(
            narr = "Multiple pointers can point to the same variable. ptr1 and ptr2 both point to x.",
            phase = "multiple_pointers",
            stats = mapOf("pointers" to "2", "all_point_to" to "x"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "100", "int x", setOf("stack", "active")),
                    MemoryCell("0x7FFF0018", "0x7FFF0010", "int* ptr1", setOf("stack")),
                    MemoryCell("0x7FFF001C", "0x7FFF0010", "int* ptr2", setOf("stack", "active"))
                ),
                pointers = listOf(
                    MemoryPointer("0x7FFF0018", "0x7FFF0010", "ptr1→"),
                    MemoryPointer("0x7FFF001C", "0x7FFF0010", "ptr2→")
                ),
                legend = "Multiple pointers to the same variable"
            )
        ))

        // Frame 8: Pointer to pointer
        frames.add(Frame(
            narr = "A pointer can point to another pointer. int** pptr points to ptr, which points to x.",
            phase = "pointer_to_pointer",
            stats = mapOf("pptr_points_to" to "ptr", "ptr_points_to" to "x"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "100", "int x", setOf("stack")),
                    MemoryCell("0x7FFF0018", "0x7FFF0010", "int* ptr", setOf("stack", "pointer")),
                    MemoryCell("0x7FFF001C", "0x7FFF0018", "int** pptr", setOf("stack", "active"))
                ),
                pointers = listOf(
                    MemoryPointer("0x7FFF0018", "0x7FFF0010", "ptr→"),
                    MemoryPointer("0x7FFF001C", "0x7FFF0018", "pptr→")
                ),
                legend = "Pointer to pointer (**pptr)"
            )
        ))

        // Frame 9: Null pointer danger
        frames.add(Frame(
            narr = "A NULL pointer (0x00000000) doesn't point anywhere. Dereferencing it causes a crash (segfault).",
            phase = "null_pointer",
            stats = mapOf("ptr_value" to "NULL", "safe" to "FALSE"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00000000", "INVALID", "[restricted]", setOf("data")),
                    MemoryCell("0x7FFF0018", "0x00000000", "int* ptr (NULL)", setOf("stack", "active"))
                ),
                legend = "NULL pointer (dangerous!)"
            )
        ))

        // Frame 10: Summary
        frames.add(Frame(
            narr = "Summary: & gets address, * follows pointer, pointers store addresses, NULL causes crash.",
            phase = "summary",
            stats = mapOf("operators" to "2", "& means" to "address-of"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "42", "int x", setOf("stack")),
                    MemoryCell("0x7FFF0018", "0x7FFF0010", "int* ptr = &x", setOf("stack", "pointer"))
                ),
                pointers = listOf(
                    MemoryPointer("0x7FFF0018", "0x7FFF0010", "ptr→")
                ),
                legend = "Pointers: & (address) and * (dereference)"
            )
        ))

        return frames
    }
}
