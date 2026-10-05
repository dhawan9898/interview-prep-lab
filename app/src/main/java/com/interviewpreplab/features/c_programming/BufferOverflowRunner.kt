package com.interviewpreplab.features.c_programming

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.MemoryCell
import com.interviewpreplab.core.model.MemoryScene

/**
 * Generates frames showing buffer overflow vulnerabilities.
 * Visualizes: stack layout, writing past boundaries, return address corruption, code execution.
 */
object BufferOverflowRunner {
    fun run(): List<Frame> {
        val frames = mutableListOf<Frame>()

        // Frame 1: Stack layout before overflow
        frames.add(Frame(
            narr = "Function stack frame: local buffer, other variables, return address at bottom.",
            phase = "normal_layout",
            stats = mapOf("buffer_size" to "10", "layout" to "normal"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "buf[0-9]", "char buf[10]", setOf("stack")),
                    MemoryCell("0x7FFF000A", "42", "int x", setOf("stack")),
                    MemoryCell("0x7FFF000E", "0x00400100", "return address", setOf("stack", "active"))
                ),
                legend = "Normal stack layout"
            )
        ))

        // Frame 2: Safe string copy
        frames.add(Frame(
            narr = "strcpy(buf, \"hello\") copies \"hello\" into buffer. Fits within 10 bytes.",
            phase = "safe_copy",
            stats = mapOf("input_length" to "5", "buffer_capacity" to "10", "overflow" to "no"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "hello\\0", "buf[0-5]", setOf("stack", "active")),
                    MemoryCell("0x7FFF000A", "42", "int x", setOf("stack")),
                    MemoryCell("0x7FFF000E", "0x00400100", "return address", setOf("stack"))
                ),
                legend = "Safe: string fits in buffer"
            )
        ))

        // Frame 3: Unsafe string copy
        frames.add(Frame(
            narr = "strcpy(buf, \"helloworld!!!\") is 14 bytes. Buffer only has 10. OVERFLOW!",
            phase = "overflow_start",
            stats = mapOf("input_length" to "14", "buffer_capacity" to "10", "overflow" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "helloworl", "buf[0-9] FULL", setOf("stack")),
                    MemoryCell("0x7FFF000A", "d!", "buf[10-11] OVERFLOW", setOf("stack", "active")),
                    MemoryCell("0x7FFF000C", "!?", "buf[12-13] OVERFLOW", setOf("stack"))
                ),
                legend = "Buffer overflow: writing past boundary"
            )
        ))

        // Frame 4: Overwriting adjacent variable
        frames.add(Frame(
            narr = "First 4 extra bytes corrupt variable x. Instead of 42, now contains 'd!' + garbage.",
            phase = "variable_corruption",
            stats = mapOf("corrupted_var" to "x", "original_value" to "42", "new_value" to "corrupted"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "helloworl", "buf[0-9]", setOf("stack")),
                    MemoryCell("0x7FFF000A", "d!!!", "int x CORRUPTED", setOf("stack", "active")),
                    MemoryCell("0x7FFF000E", "0x00400100", "return address", setOf("stack"))
                ),
                legend = "Overflow corrupts adjacent variable x"
            )
        ))

        // Frame 5: Return address vulnerability
        frames.add(Frame(
            narr = "Final bytes overwrite RETURN ADDRESS. When function returns, jumps to attacker's address!",
            phase = "return_address_overwrite",
            stats = mapOf("critical" to "yes", "target" to "return_address"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "helloworl", "buf[0-9]", setOf("stack")),
                    MemoryCell("0x7FFF000A", "d!", "int x", setOf("stack")),
                    MemoryCell("0x7FFF000E", "0xDEADBEEF", "return address HIJACKED", setOf("stack", "active"))
                ),
                legend = "Overflow overwrites return address"
            )
        ))

        // Frame 6: Jumping to malicious code
        frames.add(Frame(
            narr = "Return jumps to 0xDEADBEEF. If attacker injected shellcode there, it runs!",
            phase = "code_execution",
            stats = mapOf("execution" to "compromised", "attack" to "active"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0xDEADBEEF", "shellcode", "attacker code", setOf("data", "active"))
                ),
                legend = "Return address points to attacker's shellcode"
            )
        ))

        // Frame 7: Shellcode injection
        frames.add(Frame(
            narr = "Attacker embeds shellcode in input: spawns shell, modifies files, exfiltrates data.",
            phase = "shellcode",
            stats = mapOf("payload" to "malicious", "impact" to "critical"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "SHELLCODE", "buf (malicious input)", setOf("stack", "active")),
                    MemoryCell("0x7FFF000E", "buf_addr", "return -> shellcode", setOf("stack"))
                ),
                legend = "Shellcode payload in buffer"
            )
        ))

        // Frame 8: Stack smashing terminology
        frames.add(Frame(
            narr = "Stack smashing: overflowing buffer to corrupt stack frame and return address.",
            phase = "stack_smashing",
            stats = mapOf("vulnerability_class" to "stack_overflow", "severity" to "critical"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "overflowed", "buf[0-13] SMASHED", setOf("stack", "active")),
                    MemoryCell("0x7FFF000E", "hijacked", "return address SMASHED", setOf("stack"))
                ),
                legend = "Stack smashing = buffer overflow + return address hijack"
            )
        ))

        // Frame 9: strncpy() defense
        frames.add(Frame(
            narr = "strncpy(buf, src, 10) limits copy to 10 bytes. Prevents overflow!",
            phase = "strncpy_defense",
            stats = mapOf("defense" to "length_limit", "overflow_prevention" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "helloworl\\0", "buf[0-9] SAFE", setOf("stack", "active")),
                    MemoryCell("0x7FFF000A", "42", "int x SAFE", setOf("stack")),
                    MemoryCell("0x7FFF000E", "0x00400100", "return SAFE", setOf("stack"))
                ),
                legend = "Using strncpy() with length limit prevents overflow"
            )
        ))

        // Frame 10: Input validation
        frames.add(Frame(
            narr = "Always validate input length BEFORE copying. Check input.length < buffer.size.",
            phase = "validation",
            stats = mapOf("practice" to "defensive", "check" to "required"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "hello", "safe input", setOf("stack")),
                    MemoryCell("0x7FFF000A", "42", "int x", setOf("stack", "active"))
                ),
                legend = "Input validation prevents overflow"
            )
        ))

        // Frame 11: Modern defenses - Stack canaries
        frames.add(Frame(
            narr = "Stack canary: sentinel value between buffer and return address. Detects overflow!",
            phase = "stack_canary",
            stats = mapOf("defense" to "canary", "detection" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "buf[10]", "buffer", setOf("stack")),
                    MemoryCell("0x7FFF000A", "0xDEADBEEF", "CANARY (random)", setOf("stack", "active")),
                    MemoryCell("0x7FFF000E", "0x00400100", "return address", setOf("stack"))
                ),
                legend = "Stack canary inserted by compiler"
            )
        ))

        // Frame 12: Canary detection
        frames.add(Frame(
            narr = "If canary is corrupted, program detects attack BEFORE return. Aborts safely!",
            phase = "canary_check",
            stats = mapOf("canary_valid" to "no", "action" to "abort"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "helloworl", "buf[0-9] OVERFLOWED", setOf("stack")),
                    MemoryCell("0x7FFF000A", "0xDEAD00FF", "CANARY CORRUPTED!", setOf("stack", "active")),
                    MemoryCell("0x00000000", "ABORT!", "program stops", setOf("data"))
                ),
                legend = "Stack canary detects and prevents exploitation"
            )
        ))

        // Frame 13: ASLR - Address Space Layout Randomization
        frames.add(Frame(
            narr = "ASLR: kernel randomizes memory addresses. Attacker can't hardcode shellcode address!",
            phase = "aslr",
            stats = mapOf("defense" to "aslr", "predictability" to "low"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "buf", "address random", setOf("stack")),
                    MemoryCell("0x7FFF000E", "return random", "each run different", setOf("stack", "active"))
                ),
                legend = "ASLR randomizes addresses each run"
            )
        ))

        // Frame 14: DEP/NX - Data Execution Prevention
        frames.add(Frame(
            narr = "DEP: stack memory marked non-executable. Shellcode in buffer can't run!",
            phase = "dep",
            stats = mapOf("defense" to "dep", "shellcode_execution" to "blocked"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "shellcode", "non-executable", setOf("stack")),
                    MemoryCell("0x00000000", "BLOCKED!", "CPU refuses to execute", setOf("data", "active"))
                ),
                legend = "DEP/NX prevents stack shellcode execution"
            )
        ))

        // Frame 15: Summary
        frames.add(Frame(
            narr = "Buffer overflow is critical. Use strncpy, validate input, enable canaries, ASLR, DEP.",
            phase = "summary",
            stats = mapOf("defenses" to "4", "essential" to "yes"),
            scene = MemoryScene(
                cells = listOf(
                    MemoryCell("0x7FFF0000", "buf[10]", "bounded", setOf("stack")),
                    MemoryCell("0x7FFF000A", "0xDEADBEEF", "canary", setOf("stack")),
                    MemoryCell("0x7FFF000E", "ASLR/DEP", "protected", setOf("stack", "active"))
                ),
                legend = "Buffer overflow prevention: multiple layers"
            )
        ))

        return frames
    }
}
