package com.interviewpreplab.features.topics

data class Topic(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    // Child lessons hang off a parent lesson (via Subtopic.topicId) and are not listed in the course.
    val parentId: String? = null
)

val topicList = listOf(
    // C Programming
    Topic("arrays", "Arrays", "C Programming", "Indexing, decay to pointers, 2D layout, bounds, VLAs"),
    Topic("strings", "Strings", "C Programming", "NUL-terminated arrays, string.h, safe copying and input"),
    Topic("pointers", "Pointers & Addresses", "C Programming", "Addresses, dereference, pointer-to-pointer, const pointers"),
    Topic("pointer-arithmetic", "Pointer Arithmetic", "C Programming", "Scaled offsets, subtraction, array walking, pitfalls"),
    Topic("null-pointers", "Null Pointers", "C Programming", "NULL vs nullptr, checks, segfaults, defensive patterns"),
    Topic("dynamic-memory", "Dynamic Memory", "C Programming", "malloc, calloc, realloc, free, ownership and failure paths"),
    Topic("memory-layout", "Memory Layout", "C Programming", "Text, data, BSS, heap, stack and the process map"),
    Topic("stack-vs-heap", "Stack vs Heap", "C Programming", "Lifetime, cost, fragmentation and when to use each"),
    Topic("storage-classes", "Storage Classes", "C Programming", "auto, static, extern, register; scope, linkage, lifetime"),
    Topic("structs-unions", "Structs, Unions & Enums", "C Programming", "Records, unions, tagged variants, enums, layout"),
    Topic("struct-padding", "Struct Padding", "C Programming", "Alignment rules, member ordering, packing and ABI"),
    Topic("bitwise", "Bitwise Operators", "C Programming", "Masks, shifts, flags, XOR tricks, bit manipulation patterns"),
    Topic("endianness", "Endianness", "C Programming", "Byte order, network order, detection, portable serialization"),
    Topic("function-pointers", "Function Pointers", "C Programming", "Declarators, callbacks, dispatch tables, qsort"),
    Topic("qualifiers", "const, volatile, restrict", "C Programming", "const placement, volatile, restrict, const correctness"),
    Topic("preprocessor", "Preprocessor & Macros", "C Programming", "include, macros, conditional compilation, X-macros"),
    Topic("file-io", "File I/O", "C Programming", "FILE streams, text vs binary, buffering, seek, error handling"),
    Topic("buffer-overflow", "Buffer Overflow", "C Programming", "Stack smashing, exploit mechanics, canaries, ASLR, NX"),
    Topic("use-after-free", "Use-After-Free", "C Programming", "Dangling pointers, double free, ASan, ownership patterns"),
    Topic("memory-leaks", "Memory Leaks", "C Programming", "Leak kinds, detection with Valgrind and ASan, cleanup idioms"),
    Topic("undefined-behavior", "Undefined Behavior", "C Programming", "Overflow, aliasing, sequence points, sanitizers"),

    // Sorting
    Topic("bubble-sort", "Bubble Sort", "Sorting", "Adjacent swaps, early exit, invariants, inversions"),
    Topic("selection-sort", "Selection Sort", "Sorting", "Minimum selection, minimal writes, cycle sort contrast"),
    Topic("insertion-sort", "Insertion Sort", "Sorting", "Shifting into a sorted prefix; fast on nearly sorted data"),
    Topic("shell-sort", "Shell Sort", "Sorting", "Gapped insertion sort, gap sequences, complexity"),
    Topic("merge-sort", "Merge Sort", "Sorting", "Divide and conquer, stable O(n log n), linked lists, external sort"),
    Topic("quick-sort", "Quick Sort", "Sorting", "Partition schemes, pivot choice, worst case, introsort"),
    Topic("heap-sort", "Heap Sort", "Sorting", "Heapify, in-place O(n log n), comparison with quick and merge"),
    Topic("counting-sort", "Counting Sort", "Sorting", "Counting and prefix sums, stability, radix sort link"),

    // Searching
    Topic("linear-search", "Linear Search", "Searching", "Sequential scan, sentinel, move-to-front, when it wins"),
    Topic("binary-search", "Binary Search", "Searching", "Halving, overflow-safe midpoint, bounds, search on answer"),
    Topic("jump-search", "Jump Search", "Searching", "Sqrt(n) blocks then linear scan; versus binary and skip lists"),

    // Data Structures
    Topic("stack", "Stack", "Data Structures", "LIFO, array and list forms, bracket matching, postfix, monotonic stack"),
    Topic("queue", "Queue", "Data Structures", "FIFO, array and list forms, two-stack queue, deque, BFS use"),
    Topic("circular-queue", "Circular Queue", "Data Structures", "Ring buffer, modulo wraparound, full vs empty"),
    Topic("linked-list", "Singly Linked List", "Data Structures", "Nodes, insert and delete, reversal, cycle detection"),
    Topic("doubly-linked-list", "Doubly Linked List", "Data Structures", "prev/next links, O(1) unlink, sentinels, LRU cache"),
    Topic("hash-table", "Hash Table", "Data Structures", "Hash functions, chaining, open addressing, load factor, resizing"),
    Topic("bst", "Binary Search Tree", "Data Structures", "Ordered tree, traversals, deletion, successor, red-black overview"),
    Topic("avl-tree", "AVL Tree", "Data Structures", "Balance factors, four rotation cases, height bound"),
    Topic("heap", "Min Heap", "Data Structures", "Binary heap, priority queue, Huffman, k-th largest, median stream"),
    Topic("trie", "Trie", "Data Structures", "Prefix tree, insert and search, autocomplete, memory trade-offs"),
    Topic("graph", "Graph (BFS/DFS)", "Data Structures", "Representations, BFS and DFS, components, cycles"),

    // Graph Algorithms
    Topic("dijkstra", "Dijkstra's Algorithm", "Graph Algorithms", "Shortest paths, priority queue, negative edges, path recovery"),
    Topic("topological-sort", "Topological Sort", "Graph Algorithms", "Kahn and DFS orderings, cycle detection, scheduling"),
    Topic("union-find", "Union-Find", "Graph Algorithms", "Disjoint sets, path compression, union by rank, Kruskal"),
    Topic("mst", "Minimum Spanning Tree", "Graph Algorithms", "Kruskal and Prim, cut property, comparison and uses"),

    // Algorithm Techniques
    Topic("recursion", "Recursion & Backtracking", "Algorithm Techniques", "Base case, call stack, backtracking, tail recursion"),
    Topic("two-pointers", "Two Pointers & Sliding Window", "Algorithm Techniques", "Opposite ends, fast/slow, sliding window patterns"),
    Topic("dynamic-programming", "Dynamic Programming", "Algorithm Techniques", "Memoization, tabulation, state design, classic problems"),
    Topic("kmp", "KMP String Matching", "Algorithm Techniques", "Failure table, O(n+m) matching, related string problems"),

    // Networking L2/L3
    Topic("ethernet", "Ethernet Framing", "Networking", "MAC addresses, frame layout, FCS, MTU, VLAN tag"),
    Topic("arp", "ARP Protocol", "Networking", "IP-to-MAC resolution, cache, gratuitous ARP, spoofing"),
    Topic("ipv4-header", "IPv4 Header", "Networking", "Fields, TTL, fragmentation, checksum, options"),
    Topic("ipv6-header", "IPv6 Header", "Networking", "128-bit addresses, fixed header, extension headers, ICMPv6"),
    Topic("subnetting", "Subnetting & CIDR", "Networking", "CIDR, masks, VLSM, summarization, worked examples"),
    Topic("lpm", "Longest-Prefix Match", "Networking", "Longest-prefix match, FIB lookup, tries, TCAM"),
    Topic("icmp-traceroute", "ICMP & Traceroute", "Networking", "Message types, ping, TTL probing, path MTU discovery"),
    Topic("nat", "NAT", "Networking", "SNAT/DNAT, PAT port mapping, conntrack, traversal"),
    Topic("ospf", "OSPF Routing", "Networking", "Link-state, areas, DR/BDR, LSAs, SPF, adjacency states"),
    Topic("bgp", "BGP Routing", "Networking", "Path attributes, best-path selection, iBGP/eBGP, policy"),
    Topic("tcp-handshake", "TCP 3-Way Handshake", "Networking", "SYN, SYN-ACK, ACK, ISNs, options, SYN flood defenses"),
    Topic("tcp-teardown", "TCP Teardown", "Networking", "FIN/ACK close, TIME_WAIT, half-close, RST"),
    Topic("dns-resolution", "DNS Resolution", "Networking", "Hierarchy, recursive vs iterative, caching, record types"),
    Topic("dhcp-lease", "DHCP Lease", "Networking", "DORA, lease timers T1/T2, relay, options"),
    Topic("tls-handshake", "TLS Handshake", "Networking", "TLS 1.3 key exchange, certificates, resumption, 0-RTT"),
    Topic("mac-learning", "MAC Learning", "Networking", "Switch table, flooding, aging, CAM overflow"),
    Topic("vlan", "VLAN Trunking", "Networking", "802.1Q tags, access vs trunk ports, native VLAN, inter-VLAN routing"),
    Topic("stp", "Spanning Tree", "Networking", "Root election, port roles and states, RSTP, loop prevention"),
    Topic("lacp", "Link Aggregation", "Networking", "Port-channels, LACPDUs, hashing, modes, failure handling"),
    Topic("switch-diag", "Switch Diagnostics", "Networking", "show commands, counters, port mirroring, common faults"),
)

enum class CourseAccent { Memory, Packet, Kernel, Stack }

data class Course(
    val id: String,
    val title: String,
    val subtitle: String,
    val categories: List<String>,
    val accent: CourseAccent
) {
    val topics: List<Topic> get() = topicList.filter { it.category in categories && it.parentId == null }
}

val courses = listOf(
    Course("c-systems", "C Systems Programming", "Memory, pointers, undefined behavior", listOf("C Programming"), CourseAccent.Memory),
    Course("networking", "Network L2/L3 Architecture", "Frames, routing, TCP, switching", listOf("Networking"), CourseAccent.Packet),
    Course("kernel", "Linux Kernel Internals", "Scheduler, syscalls, VFS, eBPF", emptyList(), CourseAccent.Kernel),
    Course("dsa", "DSA Foundations", "Sorting, searching, data structures", listOf("Sorting", "Searching", "Data Structures", "Graph Algorithms", "Algorithm Techniques"), CourseAccent.Stack),
)

fun courseOf(topic: Topic): Course? = courses.firstOrNull { topic.category in it.categories }
