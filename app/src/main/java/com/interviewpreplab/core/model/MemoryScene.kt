package com.interviewpreplab.core.model

/**
 * Represents a single memory cell for visualization.
 *
 * @param address Hexadecimal memory address (e.g., "0x7FFF0010")
 * @param value Displayed value (e.g., "42", "0xDEADBEEF", "hello")
 * @param label Variable name or identifier (e.g., "x", "ptr", "arr[0]")
 * @param roles Tags for visual styling (e.g., "stack", "heap", "active", "pointer")
 * @param size Size in bytes (e.g., 4 for int, 1 for char)
 */
data class MemoryCell(
    val address: String,
    val value: String,
    val label: String,
    val roles: Set<String> = setOf(),
    val size: Int = 4
)

/**
 * Represents a pointer relationship between memory cells.
 *
 * @param fromAddress Source address (where pointer variable is)
 * @param toAddress Target address (what pointer points to)
 * @param label Pointer label (e.g., "ptr→", "sp", "fp")
 */
data class MemoryPointer(
    val fromAddress: String,
    val toAddress: String,
    val label: String = "→"
)

/**
 * Complete memory layout visualization.
 * Shows stack, heap, data segment, and code segment with proper addressing.
 *
 * @param cells Individual memory cells to display
 * @param pointers Pointer relationships
 * @param stackStart Stack starting address (high)
 * @param heapStart Heap starting address (low)
 * @param legend Visual legend/description
 */
data class MemoryScene(
    val cells: List<MemoryCell>,
    val pointers: List<MemoryPointer> = emptyList(),
    val stackStart: String = "0x7FFF0000",
    val heapStart: String = "0x55558000",
    val legend: String = "Memory Layout"
) : Scene
