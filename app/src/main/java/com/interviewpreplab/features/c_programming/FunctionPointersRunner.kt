package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing function pointers and callbacks.
 * Visualizes: function addresses, function calls via pointers, callbacks.
 */
object FunctionPointersRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Functions in code segment
        frames.add(Frame(
            narr = "Functions are compiled into CODE SEGMENT. Each function has a start address.",
            phase = "function_location",
            stats = mapOf("segment" to "code", "count" to "3"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "add_code", "int add(int a, int b)", setOf("data")),
                    MemoryCell("0x00400100", "mul_code", "int mul(int a, int b)", setOf("data")),
                    MemoryCell("0x00400200", "sub_code", "int sub(int a, int b)", setOf("data", "active"))
                ),
                legend = "Functions stored in code segment"
            )
        ))

        // Frame 2: Getting function address
        frames.add(Frame(
            narr = "A function name (without parentheses) gives its address. func == &func.",
            phase = "function_address",
            stats = mapOf("function" to "add", "address" to "0x00400000"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "add_code", "int add(...)", setOf("data", "active"))
                ),
                legend = "Function name decays to its address"
            )
        ))

        // Frame 3: Declaring function pointer
        frames.add(Frame(
            narr = "int (*fp)(int, int) declares a pointer to a function that takes 2 ints.",
            phase = "function_pointer_type",
            stats = mapOf("type" to "int (*)(int, int)", "purpose" to "declaration"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "int (*fp)(int, int)", setOf("stack", "active"))
                ),
                legend = "Function pointer declaration"
            )
        ))

        // Frame 4: Assigning function address to pointer
        frames.add(Frame(
            narr = "fp = add assigns function address to pointer. Now fp points to add().",
            phase = "function_assign",
            stats = mapOf("fp_points_to" to "add", "address" to "0x00400000"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "add_code", "int add(...)", setOf("data")),
                    MemoryCell("0x7FFF0010", "0x00400000", "int (*fp)(int, int) = add", setOf("stack", "pointer", "active"))
                ),
                legend = "Assigning function address to pointer"
            )
        ))

        // Frame 5: Calling function via pointer
        frames.add(Frame(
            narr = "fp(2, 3) calls the function through the pointer. Same as add(2, 3).",
            phase = "function_call_ptr",
            stats = mapOf("call_style" to "fp(2, 3)", "result" to "5"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "add_code", "int add(...)", setOf("data", "active")),
                    MemoryCell("0x7FFF0010", "0x00400000", "int (*fp)(int, int)", setOf("stack", "pointer")),
                    MemoryCell("0x7FFF0014", "5", "result of fp(2, 3)", setOf("stack"))
                ),
                legend = "Calling function via pointer"
            )
        ))

        // Frame 6: Switching function pointers
        frames.add(Frame(
            narr = "fp can point to different functions. Switch behavior at runtime!",
            phase = "function_switch",
            stats = mapOf("fp_now_points_to" to "mul", "new_address" to "0x00400100"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "add_code", "int add(...)", setOf("data")),
                    MemoryCell("0x00400100", "mul_code", "int mul(...)", setOf("data", "active")),
                    MemoryCell("0x7FFF0010", "0x00400100", "int (*fp)(int, int) = mul", setOf("stack", "pointer"))
                ),
                legend = "Function pointer can switch to different function"
            )
        ))

        // Frame 7: Function pointer in array
        frames.add(Frame(
            narr = "Array of function pointers: int (*ops[3])(int, int) = {add, mul, sub}.",
            phase = "function_array",
            stats = mapOf("array_size" to "3", "functions" to "3"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00400000", "ops[0] -> add", setOf("stack")),
                    MemoryCell("0x7FFF0018", "0x00400100", "ops[1] -> mul", setOf("stack")),
                    MemoryCell("0x7FFF0020", "0x00400200", "ops[2] -> sub", setOf("stack", "active"))
                ),
                legend = "Array of function pointers"
            )
        ))

        // Frame 8: Array indexing with function pointers
        frames.add(Frame(
            narr = "ops[1](2, 3) calls the second function (mul). Result: 2 * 3 = 6.",
            phase = "array_call",
            stats = mapOf("index" to "1", "function_called" to "mul", "result" to "6"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400100", "mul_code", "int mul(...)", setOf("data", "active")),
                    MemoryCell("0x7FFF0010", "0x00400000", "ops[0] -> add", setOf("stack")),
                    MemoryCell("0x7FFF0018", "0x00400100", "ops[1] -> mul", setOf("stack", "pointer"))
                ),
                legend = "Calling function from array via pointer"
            )
        ))

        // Frame 9: Callback concept
        frames.add(Frame(
            narr = "CALLBACK: Pass function pointer to another function. It calls your function.",
            phase = "callback_intro",
            stats = mapOf("pattern" to "callback", "use_case" to "event_handler"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "my_func", "void my_func(int x)", setOf("data")),
                    MemoryCell("0x7FFF0010", "0x00400000", "register_callback(my_func)", setOf("stack", "pointer"))
                ),
                legend = "Callback pattern: pass function to register"
            )
        ))

        // Frame 10: Callback registration
        frames.add(Frame(
            narr = "register_callback(fp) stores pointer. Later, library calls fp() on events.",
            phase = "callback_register",
            stats = mapOf("registered" to "yes", "pending_call" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00400000", "int (*callback)(int)", setOf("stack", "pointer", "active")),
                    MemoryCell("0x7FFF0018", "waiting", "for event...", setOf("stack"))
                ),
                legend = "Callback registered and waiting"
            )
        ))

        // Frame 11: Callback execution
        frames.add(Frame(
            narr = "Event happens! Library calls callback() without you knowing implementation.",
            phase = "callback_execute",
            stats = mapOf("event" to "triggered", "function_called" to "callback"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "my_func", "executing...", setOf("data", "active")),
                    MemoryCell("0x7FFF0010", "0x00400000", "int (*callback)(int)", setOf("stack", "pointer"))
                ),
                legend = "Callback executed on event"
            )
        ))

        // Frame 12: Function pointer type safety
        frames.add(Frame(
            narr = "Type matters! int (*fp)(int) points to functions returning int with 1 param.",
            phase = "type_safety",
            stats = mapOf("signature_match" to "required", "type_mismatch" to "crash"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "int f(int)", "matches", setOf("data")),
                    MemoryCell("0x00400100", "void f()", "MISMATCH!", setOf("data", "active"))
                ),
                legend = "Function pointer type must match function signature"
            )
        ))

        // Frame 13: NULL function pointer
        frames.add(Frame(
            narr = "Always initialize function pointers to NULL and check before calling!",
            phase = "null_function_ptr",
            stats = mapOf("initialized" to "yes", "safe" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0010", "0x00000000", "int (*fp)(int, int) = NULL", setOf("stack", "active"))
                ),
                legend = "Initialize function pointer to NULL"
            )
        ))

        // Frame 14: Practical use: sorting with comparator
        frames.add(Frame(
            narr = "qsort(array, n, size, compare_fn) sorts using custom comparison function.",
            phase = "qsort_example",
            stats = mapOf("use_case" to "sorting", "comparator" to "function_pointer"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "compare", "int compare(const void* a, const void* b)", setOf("data", "active"))
                ),
                legend = "Practical example: qsort with function pointer"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Summary: functions have addresses, pointers can store them, indirect calls via pointers.",
            phase = "summary",
            stats = mapOf("key_concepts" to "5"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x00400000", "func_code", "int func(...)", setOf("data")),
                    MemoryCell("0x7FFF0010", "0x00400000", "int (*fp)(...) = func", setOf("stack", "pointer"))
                ),
                legend = "Function pointers for dynamic behavior"
            )
        ))

        return frames
    }
}
