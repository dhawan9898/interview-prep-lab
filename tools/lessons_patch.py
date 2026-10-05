"""Second pass over lesson JSON: GfG-outline items that the base generators omit
(extra subtopics, practice problems). Always run after the lessons_*.py generators."""
import json, os
from lessonlib import OUT, S, SUB

E, M, H = 'Easy', 'Medium', 'Hard'

PRACTICE = {
 'pointers': [('Swap two integers using pointers', E), ('Reverse an array in place with two pointers', E), ('Implement strlen and strcpy using pointers', E), ('Allocate a 2D array dynamically with int **', M), ('Spot the wild and dangling pointers in five snippets', M)],
 'memory-layout': [('Print the addresses of a global, static, local, and heap object and order them', E), ('Use the size command to see how initialized vs uninitialized globals change data and bss', E), ('Show stack growth direction with a recursive function', M), ('Explain why writing through a string literal pointer crashes', M)],
 'stack-vs-heap': [('Fix a function that returns the address of a local variable', E), ('Trigger a stack overflow and find the maximum recursion depth', M), ('Benchmark malloc against stack allocation', M), ('Implement a simple bump allocator', H)],
 'pointer-arithmetic': [('Sum an array by walking a pointer', E), ('Reverse a string in place with two pointers', E), ('Compute the distance between two pointers into one array', E), ('Implement memmove handling overlapping regions', M), ('Index a 2D array using only pointer arithmetic', M)],
 'null-pointers': [('Write a strlen that safely handles NULL', E), ('Wrap malloc in a checked version that reports failure', E), ('Find the NULL dereference in a linked-list delete routine', M), ('Add NULL validation and error codes to a small API', M)],
 'function-pointers': [('Build a calculator using an array of function pointers', E), ('Sort ints ascending and descending with qsort comparators', E), ('Sort an array of structs by multiple keys', M), ('Implement a generic map(array, fn)', M), ('Build an event callback registry', H)],
 'buffer-overflow': [('Spot the overflow in a strcpy/gets snippet and fix it with snprintf', E), ('Compile with and without -fstack-protector and compare behaviour', M), ('Use AddressSanitizer to locate a heap overflow', M), ('Write a bounded string copy that always NUL-terminates', M)],
 'use-after-free': [('Fix a function that returns a dangling pointer', E), ('Reproduce a double free and read the error', E), ('Find the use-after-free in a linked-list deletion loop', M), ('Use ASan to read the allocation, free, and use stacks', M)],
 'memory-leaks': [('Find a leak with valgrind --leak-check=full', E), ('Fix leaks on every error path using a goto cleanup block', M), ('Free a nested structure (list of strings) correctly', M), ('Write a malloc/free tracking wrapper that reports leaks at exit', H)],
 'struct-padding': [('Compute sizeof for five structs by hand, then verify', E), ('Reorder members to minimize struct size', E), ('Print offsetof for every member', M), ('Serialize a struct field by field instead of fwrite', M)],
 'endianness': [('Detect host endianness', E), ('Write a 32-bit byte swap without builtins', E), ('Parse an Ethernet and IP header from a byte buffer portably', M), ('Convert a file of 16-bit big-endian samples to host order', M)],
 'undefined-behavior': [('Identify the undefined behavior in eight short snippets', E), ('Write overflow-safe add and multiply checks', M), ('Fix a strict-aliasing violation with memcpy', M), ('Show an optimizer removing a check that relies on signed overflow', H)],

 'bubble-sort': [('Implement with an early-exit flag and count swaps', E), ('Sort in descending order and sort strings', E), ('Implement recursive bubble sort', M), ('Count inversions with bubble sort and compare with merge sort', M)],
 'selection-sort': [('Implement and count the swaps', E), ('Find the k-th smallest element using k passes', E), ('Make selection sort stable', M), ('Sort a linked list with selection sort', M)],
 'insertion-sort': [('Sort a nearly sorted array and count shifts', E), ('Implement binary insertion sort', M), ('Sort a linked list with insertion sort', M), ('Build a merge sort that switches to insertion sort for short runs', H)],
 'quick-sort': [('Implement Lomuto partition', E), ('Sort colors with a three-way (Dutch flag) partition', M), ('Find the k-th smallest element with quickselect', M), ('Bound the stack to O(log n) by recursing into the smaller side first', H)],
 'binary-search': [('Search a sorted array iteratively', E), ('Compute integer square root with binary search', E), ('Find first and last position of a target', M), ('Search in a rotated sorted array', M), ('Minimum ship capacity to deliver within D days', H)],
 'stack': [('Check balanced parentheses', E), ('Convert infix to postfix', E), ('Reverse a string using a stack', E), ('Next greater element', M), ('Stock span problem', M), ('Tower of Hanoi', M), ('Largest rectangle in a histogram', H), ('Detect redundant brackets', H)],
 'queue': [('Implement a queue using two stacks', E), ('Implement a stack using queues', E), ('Level-order traversal with BFS', E), ('Reverse the first K elements of a queue', M), ('Implement K queues in one array', M), ('Sliding window maximum', H), ('Shortest path in a maze', H)],
 'circular-queue': [('Implement with a size counter', E), ('Moving average from a data stream', E), ('Implement without a size counter using one spare slot', M), ('Design a circular deque', M)],
 'linked-list': [('Reverse a linked list', E), ('Find the middle node', E), ('Merge two sorted lists', E), ('Detect a cycle with Floyd fast/slow pointers', M), ('Remove the n-th node from the end', M), ('Reverse nodes in groups of k', H)],
 'bst': [('Validate a binary search tree', E), ('Convert a sorted array to a balanced BST', E), ('k-th smallest element in a BST', M), ('Lowest common ancestor of two nodes', M), ('Delete a node (all three cases)', M), ('Recover a BST with two swapped nodes', H), ('Merge two BSTs', H)],
 'heap': [('Check whether an array is a min-heap', E), ('Implement heap sort in place', M), ('Find the k largest elements', M), ('Merge k sorted lists', M), ('Median of a data stream', H)],
 'graph': [('BFS shortest path in an unweighted graph', E), ('Number of islands', M), ('Detect a cycle in a directed graph', M), ('Topological sort', M), ('Word ladder', H)],

 'ethernet': [('Parse a hex dump of a frame into its fields', E), ('Compute the padding needed for a 20-byte payload', E), ('Identify protocols from EtherType values', E), ('Verify an FCS with CRC-32', M)],
 'arp': [('Capture an ARP exchange in Wireshark and label each field', E), ('Show the ARP cache before and after pinging a neighbour', E), ('Build a raw ARP request with scapy', M), ('Detect ARP spoofing from duplicate MAC entries', H)],
 'ipv4-header': [('Decode an IPv4 header from hex', E), ('Explain why a TTL=1 packet dies at the first router', E), ('Compute a header checksum by hand', M), ('Fragment a 4000-byte payload for MTU 1500 and list the offsets', M)],
 'ipv6-header': [('Compress and expand IPv6 addresses', E), ('Explain why routers cannot fragment IPv6', E), ('Decode a 40-byte IPv6 header', M), ('Order a set of extension headers correctly', M)],
 'subnetting': [('Network, broadcast, and host range for 172.16.37.200/21', E), ('Split 192.168.10.0/24 into four subnets', E), ('VLSM plan for 100, 50, 20, and 2 hosts', M), ('Summarize four contiguous /24s into one prefix', M)],
 'lpm': [('Choose the route for ten destinations in a six-entry table', E), ('Build a binary trie for a routing table', M), ('Implement a longest-prefix lookup in code', M), ('Explain a /24 hijack against a /16', H)],
 'icmp-traceroute': [('Interpret a traceroute that contains asterisks', E), ('Find the path MTU with ping and the DF bit', M), ('Explain why hops differ between runs', M), ('Write a toy traceroute with raw sockets', H)],
 'nat': [('Fill in the translation table for three hosts sharing one IP', E), ('Configure port forwarding with iptables', M), ('Explain why active FTP breaks behind NAT', M), ('Compare cone and symmetric NAT for peer-to-peer', H)],
 'ospf': [('Elect the DR and BDR from priorities and router IDs', E), ('Compute the SPF tree and costs for a five-router graph', M), ('Diagnose a neighbour stuck in ExStart', M), ('Design areas for three sites', M)],
 'bgp': [('Explain AS_PATH loop prevention', E), ('Pick the best path among four routes', M), ('Prefer one provider by setting LOCAL_PREF', M), ('Describe a prefix hijack and how RPKI mitigates it', H)],
 'tcp-handshake': [('Label seq and ack numbers in a captured handshake', E), ('Watch connection states with ss during a connect', E), ('Explain SYN floods and SYN cookies', M), ('Describe a simultaneous open', H)],
 'tcp-teardown': [('Draw the state transitions on both sides of a close', E), ('Capture a RST close and a FIN close and compare', E), ('Explain why TIME-WAIT lasts 2 x MSL', M), ('Diagnose thousands of sockets stuck in CLOSE-WAIT', M)],
 'dns-resolution': [('Use dig +trace and label each referral', E), ('Predict cache behaviour from a set of TTLs', M), ('Explain CNAME chains and why a CNAME cannot sit at the zone apex', M), ('Find DNSSEC records in a response', H)],
 'dhcp-lease': [('Order the DORA messages and name the UDP ports', E), ('Compute T1 and T2 for a 24-hour lease', E), ('Configure a relay and explain giaddr', M), ('Defend against a rogue DHCP server with snooping', M)],
 'tls-handshake': [('Explain forward secrecy', E), ('Inspect a certificate chain with openssl s_client', E), ('Identify TLS 1.3 handshake messages in a capture', M), ('Compare the security of 1-RTT and 0-RTT', H)],
 'mac-learning': [('Trace the MAC table after four frames', E), ('Explain unknown-unicast flooding', E), ('Diagnose MAC flapping', M), ('Simulate MAC flooding and apply port security', M)],
 'vlan': [('Configure two VLANs with an access port and a trunk', E), ('Decode an 802.1Q tag from hex', M), ('Design router-on-a-stick for three VLANs', M), ('Explain double-tagging VLAN hopping', H)],
 'stp': [('Compute path costs for a topology', E), ('Configure PortFast and BPDU Guard', E), ('Elect the root and port roles for a four-switch ring', M), ('Explain the RSTP proposal/agreement handshake', M)],
 'lacp': [('Which active/passive combinations form a bundle?', E), ('Explain why one flow cannot exceed one member link', E), ('Configure EtherChannel and verify it', M), ('Choose a hashing policy for a given traffic mix', H)],
 'switch-diag': [('Interpret the counters in show interfaces', E), ('Troubleshoot a host that cannot reach its gateway', M), ('Find a loop from rising broadcast counters', M), ('Diagnose a native VLAN mismatch', M)],
}

EXTRA = {
 'bubble-sort': [SUB('Recursive bubble sort', 'Same algorithm, recursion for the passes.', S(None, "One recursive call performs a single pass (moving the maximum to the end) and then recurses on the first n-1 elements, stopping when n is 1 or no swap occurred. Time stays O(n²) and the call stack adds O(n) space, so the iterative form is preferred.", code="void bubble_rec(int a[], int n) {\n  if (n <= 1) return;\n  int swapped = 0;\n  for (int i = 0; i < n - 1; i++)\n    if (a[i] > a[i+1]) { swap(&a[i], &a[i+1]); swapped = 1; }\n  if (swapped) bubble_rec(a, n - 1);\n}"))],
 'selection-sort': [SUB('Applications', 'Where it is still reasonable.', S(None, bullets=["Teaching sorting fundamentals.", "Very small arrays where overhead of better algorithms is not worth it.", "Situations where memory writes are costly, because it does at most n-1 swaps.", "Conceptual foundation of heap sort."]))],
 'insertion-sort': [SUB('Applications', 'Where it is used for real.', S(None, bullets=["Small or nearly sorted data.", "Base case inside hybrid sorts such as Timsort and introsort.", "Sub-step inside bucket sort for each bucket.", "Online situations where items arrive one at a time."]))],
 'quick-sort': [SUB('Naive partition', 'The simplest, memory-hungry scheme.', S(None, "Copy elements smaller than the pivot into a temporary array, then the pivot, then larger ones, and copy back. It is stable and easy to reason about but uses O(n) extra space per partition, which defeats quick sort's in-place advantage; Lomuto and Hoare avoid it.")),
  SUB('Where quick sort is used', 'Applications.', S(None, bullets=["C and C++ standard-library sorts (typically introsort variants).", "Database record ordering and in-memory sorting.", "K-th element selection through quickselect.", "Preprocessing for binary search.", "Geometry: convex hull algorithms sort points first."]))],
 'binary-search': [SUB('Recursive version', 'Clear but costs stack.', S(None, "The recursive form searches (lo, hi) by calling itself on one half. It has the same O(log n) time but uses O(log n) stack frames; the iterative loop uses O(1) space and is the better default.", code="int bs(const int a[], int lo, int hi, int x) {\n  if (lo > hi) return -1;\n  int mid = lo + (hi - lo) / 2;\n  if (a[mid] == x) return mid;\n  return a[mid] < x ? bs(a, mid + 1, hi, x) : bs(a, lo, mid - 1, x);\n}")),
  SUB('Unbounded (exponential) search', 'When the size is unknown.', S(None, "Double an index 1, 2, 4, 8, ... until the value there reaches the target or the end, then binary search the last interval. Costs O(log p) where p is the target's position, useful for infinite streams and very large sorted sources."))],
 'stack': [SUB('Deque-based implementation', 'Using a double-ended queue as a stack.', S(None, "A deque (C++ `std::deque`, Python `collections.deque`) supports O(1) push and pop at one end and grows without reallocating the whole buffer, so it makes a convenient stack with no capacity limit. Array stacks are tighter in memory; linked stacks avoid resizing but add per-node overhead."))],
 'queue': [SUB('Types of queues', 'Variants at a glance.', S(None, bullets=["Simple (linear) queue: FIFO with the dead-space problem.", "Circular queue: wraps indices to reuse space.", "Deque: insert and remove at both ends.", "Priority queue: dequeue by priority, usually a heap.", "Blocking and concurrent queues: thread-safe producer-consumer buffers."]))],
 'circular-queue': [SUB('getFront and getRear', 'Peeking without removing.', S(None, "With front, size, and capacity: the front element is `a[front]`; the rear element is `a[(front + size - 1) % capacity]`. Return a sentinel or error when size is 0.", code="int get_front(const Q *q) { return q->size ? q->a[q->front] : -1; }\nint get_rear(const Q *q)  { return q->size ? q->a[(q->front + q->size - 1) % q->cap] : -1; }"))],
 'bst': [SUB('Handling duplicates', 'Choose a policy.', S(None, "A strict BST rejects duplicates. Practical options: store a count in each node, always send equal keys to the right (or left) subtree consistently, or keep a list of values per key. Whatever you choose, make search, insert, and delete agree on it."))],
 'graph': [SUB('Tree traversals are special-case graph searches', 'DFS orders and level order.', S(None, "On a binary tree, DFS gives inorder (left, node, right), preorder (node, left, right), and postorder (left, right, node); BFS gives level order. A tree is a connected acyclic graph, so no visited set is needed, but general graphs need one to avoid infinite loops."))],
 'undefined-behavior': [SUB('Why UB can help performance', 'The compiler-side argument.', S(None, "Because the compiler may assume UB never happens it can skip bounds and overflow checks, keep values in registers wider than their declared type, and reorder or vectorize loops. This is why signed overflow is undefined while unsigned wraps: the freedom is a deliberate trade for speed.")),
  SUB('Related traps', 'Other places UB hides.', S(None, bullets=["Passing NULL to printf's `%s`.", "Calling realloc with a size of 0 (implementation-defined or UB depending on version).", "Using a pointer after realloc moved the block.", "Comparing or subtracting pointers to different arrays.", "In C++: `delete this`, deleting through a base pointer without a virtual destructor."]))],
 'memory-leaks': [SUB('Smart pointers in C++', 'Automatic cleanup with RAII.', S(None, "`std::unique_ptr` owns one object and frees it when it goes out of scope; `std::shared_ptr` reference-counts shared ownership; `std::weak_ptr` observes without owning and breaks reference cycles. Using them removes most manual delete calls and therefore most leaks.", code="auto p = std::make_unique<Widget>();   // freed automatically"))],
 'stack-vs-heap': [SUB('Automatic memory management in other languages', 'Java and garbage collection.', S(None, "In Java, primitives and references live in stack frames while objects live on the heap; the garbage collector reclaims unreachable objects, so there is no free(). That removes dangling pointers and most leaks but adds GC pauses and less predictable timing than C's explicit control."))],
}

def main():
    for tid, probs in PRACTICE.items():
        p = os.path.join(OUT, tid + '.json')
        d = json.load(open(p))
        d['practice'] = [{'title': t, 'difficulty': lvl} for t, lvl in probs]
        titles = {s['title'] for s in d['subtopics']}
        for extra in EXTRA.get(tid, []):
            if extra['title'] not in titles:
                d['subtopics'].append(extra)
        json.dump(d, open(p, 'w'), indent=2, ensure_ascii=False)
    print('patched', len(PRACTICE))

if __name__ == '__main__':
    main()
