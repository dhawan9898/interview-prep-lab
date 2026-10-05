package com.interviewpreplab.features.topics

data class Topic(
    val id: String,
    val title: String,
    val category: String,
    val description: String
)

val topicList = listOf(
    // C Programming
    Topic("memory-layout", "Memory Layout", "C Programming", "Stack, Heap, Data, Code segments"),
    Topic("pointers", "Pointers & Addresses", "C Programming", "Address-of, dereference operators"),
    Topic("stack-vs-heap", "Stack vs Heap", "C Programming", "Automatic vs manual allocation"),
    Topic("pointer-arithmetic", "Pointer Arithmetic", "C Programming", "Array indexing, offsets, loops"),
    Topic("null-pointers", "Null Pointers", "C Programming", "NULL checks, segmentation faults"),
    Topic("function-pointers", "Function Pointers", "C Programming", "Callbacks, dynamic dispatch"),
    Topic("buffer-overflow", "Buffer Overflow", "C Programming", "Stack smashing, defenses"),
    Topic("use-after-free", "Use-After-Free", "C Programming", "Dangling pointers, memory reuse"),
    Topic("memory-leaks", "Memory Leaks", "C Programming", "Accumulation, Valgrind/ASan"),
    Topic("struct-padding", "Struct Padding", "C Programming", "Alignment, optimization"),
    Topic("endianness", "Endianness", "C Programming", "Big/little-endian, network order"),
    Topic("undefined-behavior", "Undefined Behavior", "C Programming", "Signed overflow, race conditions"),

    // Sorting
    Topic("bubble-sort", "Bubble Sort", "Sorting", "Simple comparison-based sort"),
    Topic("selection-sort", "Selection Sort", "Sorting", "Find minimum and swap"),
    Topic("insertion-sort", "Insertion Sort", "Sorting", "Build sorted array incrementally"),
    Topic("quick-sort", "Quick Sort", "Sorting", "Divide and conquer with pivot"),

    // Searching
    Topic("binary-search", "Binary Search", "Searching", "Halve search space each iteration"),

    // Data Structures
    Topic("stack", "Stack", "Data Structures", "LIFO data structure"),
    Topic("queue", "Queue", "Data Structures", "FIFO data structure with dead space"),
    Topic("circular-queue", "Circular Queue", "Data Structures", "FIFO with modulo wraparound"),
    Topic("linked-list", "Singly Linked List", "Data Structures", "Dynamic list with pointers"),
    Topic("bst", "Binary Search Tree", "Data Structures", "Ordered tree for efficient search"),
    Topic("heap", "Min Heap", "Data Structures", "Priority queue with heap property"),
    Topic("graph", "Graph (BFS/DFS)", "Data Structures", "Node and edge traversal"),

    // Networking L2/L3
    Topic("ethernet", "Ethernet Framing", "Networking", "MAC addresses, frame structure, FCS"),
    Topic("arp", "ARP Protocol", "Networking", "IP-to-MAC address resolution"),
    Topic("ipv4-header", "IPv4 Header", "Networking", "Addressing, TTL, fragmentation"),
    Topic("ipv6-header", "IPv6 Header", "Networking", "128-bit addressing, simplified header"),
    Topic("subnetting", "Subnetting & CIDR", "Networking", "Network division, prefix notation"),
    Topic("lpm", "Longest-Prefix Match", "Networking", "Routing algorithm, path selection"),
    Topic("icmp-traceroute", "ICMP & Traceroute", "Networking", "Diagnostics, TTL-based discovery"),
    Topic("nat", "NAT", "Networking", "Private-to-public translation"),
    Topic("ospf", "OSPF Routing", "Networking", "Link-state routing, SPF algorithm"),
    Topic("bgp", "BGP Routing", "Networking", "Interdomain routing, AS paths"),
    Topic("tcp-handshake", "TCP 3-Way Handshake", "Networking", "SYN, SYN-ACK, ACK, connection setup"),
    Topic("tcp-teardown", "TCP Teardown", "Networking", "FIN-ACK handshake, graceful close"),
    Topic("dns-resolution", "DNS Resolution", "Networking", "Domain-to-IP lookup, hierarchy"),
    Topic("dhcp-lease", "DHCP Lease", "Networking", "Dynamic IP assignment, lease management"),
    Topic("tls-handshake", "TLS Handshake", "Networking", "Encryption setup, certificate exchange"),
    Topic("mac-learning", "MAC Learning", "Networking", "Switch table building, flooding"),
    Topic("vlan", "VLAN Trunking", "Networking", "Virtual networks, 802.1Q tagging"),
    Topic("stp", "Spanning Tree", "Networking", "Loop prevention, redundancy"),
    Topic("lacp", "Link Aggregation", "Networking", "Multi-link bundling, load balancing"),
    Topic("switch-diag", "Switch Diagnostics", "Networking", "Troubleshooting, show commands"),
)

enum class CourseAccent { Memory, Packet, Kernel, Stack }

data class Course(
    val id: String,
    val title: String,
    val subtitle: String,
    val categories: List<String>,
    val accent: CourseAccent
) {
    val topics: List<Topic> get() = topicList.filter { it.category in categories }
}

val courses = listOf(
    Course("c-systems", "C Systems Programming", "Memory, pointers, undefined behavior", listOf("C Programming"), CourseAccent.Memory),
    Course("networking", "Network L2/L3 Architecture", "Frames, routing, TCP, switching", listOf("Networking"), CourseAccent.Packet),
    Course("kernel", "Linux Kernel Internals", "Scheduler, syscalls, VFS, eBPF", emptyList(), CourseAccent.Kernel),
    Course("dsa", "DSA Foundations", "Sorting, searching, data structures", listOf("Sorting", "Searching", "Data Structures"), CourseAccent.Stack),
)

fun courseOf(topic: Topic): Course? = courses.firstOrNull { topic.category in it.categories }
