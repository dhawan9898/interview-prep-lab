# Phase 4: Networking L2/L3 Module - Complete Plan

**Status:** 🚀 **STARTING**  
**Duration Estimate:** 3-4 weeks  
**Complexity:** High (new domain, protocol visualization)

---

## 🎯 Phase 4 Goals

Learn networking fundamentals through interactive visualizations:
- **Layer 2 (Data Link)** — Ethernet framing, MAC addressing, switching, STP, VLAN
- **Layer 3 (Network)** — IP routing, subnetting, ICMP, ARP, NAT
- **Layer 4 (Transport)** — TCP/UDP, port numbers, connection lifecycle
- **Application Layer** — DNS, DHCP, TLS, HTTP (as interview glue)

---

## 📊 Phase 4 Topics (20 core topics)

### **Layer 2 Fundamentals (4 topics)**
1. **Ethernet Frame** — Structure, MAC addresses, frame format
2. **ARP (Address Resolution Protocol)** — MAC discovery, ARP request/reply
3. **MAC Learning & Switching** — Bridge tables, frame forwarding
4. **VLAN & Trunking** — Virtual LAN separation, 802.1Q tagging

### **Layer 2 Advanced (3 topics)**
5. **Spanning Tree Protocol (STP)** — Loop prevention, port roles
6. **Link Aggregation** — LACP, load balancing
7. **Switch Diagnostics** — CAM table learning, aging

### **Layer 3 Fundamentals (4 topics)**
8. **IPv4 Header** — Addresses, routing, fragmentation
9. **IPv6 Header** — Modern addressing, flow labels
10. **Subnetting & CIDR** — Subnet masks, address ranges
11. **Longest-Prefix Match** — Route lookup algorithm

### **Layer 3 Protocols (4 topics)**
12. **ICMP & Traceroute** — Echo, time exceeded, path discovery
13. **NAT (Network Address Translation)** — Address rewriting, port translation
14. **OSPF (Open Shortest Path First)** — Link-state routing, LSA flooding
15. **BGP (Border Gateway Protocol)** — AS paths, best path selection

### **Layer 4 & Above (5 topics)**
16. **TCP Handshake** — SYN, SYN-ACK, ACK, connection establishment
17. **TCP Teardown** — FIN, RST, graceful closure
18. **DNS Resolution** — Query/response, recursive resolution
19. **DHCP Lease** — DISCOVER, OFFER, REQUEST, ACK
20. **TLS Handshake** — Certificate exchange, key agreement (overview)

---

## 🎨 New Visualizers

### **PacketFlowScene** — Sequence Diagrams
Shows protocol exchanges as timeline with packets flowing between nodes:

```
┌──────────────────────────────────────────────────────┐
│ TCP Handshake: Client → Server → Client              │
├──────────────────────────────────────────────────────┤
│ Client                Server                          │
│   │                     │                             │
│   ├─────SYN(seq=1)─────→│                            │
│   │                     │                             │
│   │←──SYN-ACK(seq=2)────┤                            │
│   │                     │                            │
│   ├─────ACK(seq=2)─────→│                            │
│   │                     │                            │
│   Connection established                             │
└──────────────────────────────────────────────────────┘
```

**Data Structure:**
```kotlin
data class PacketFlowScene(
    val nodes: List<Node>,              // Endpoints (Client, Server, Router, etc.)
    val packets: List<Packet>,          // Messages flowing between nodes
    val timeline: List<TimelineStep>,   // Sequence of events
    val description: String             // Legend/explanation
) : Scene

data class Node(
    val id: String,                     // "client", "server", "router-1"
    val label: String,                  // "Client", "Server", "Router 1"
    val position: Float                 // Y position for drawing
)

data class Packet(
    val from: String,                   // Source node ID
    val to: String,                     // Destination node ID
    val protocol: String,               // "TCP", "ARP", "DNS"
    val details: String,                // "SYN(seq=1000, ack=0)"
    val direction: Int,                 // 1=forward, -1=return
    val timestamp: Int                  // Sequence order
)

data class TimelineStep(
    val time: Int,
    val description: String,
    val highlightedPacket: Int?         // Which packet index is active
)
```

### **PacketHeaderScene** — Bit-Level Protocol Fields
Shows protocol header structure with fields and values:

```
┌────────────────────────────────────────────────────────────┐
│ IPv4 Header (20 bytes minimum)                             │
├────────────────────────────────────────────────────────────┤
│ Version │ IHL │ DSCP │ ECN │ Total Length: 1234           │
│    4    │  5  │ 000000 │ 00 │ 0000010011010010           │
├────────────────────────────────────────────────────────────┤
│ Identification: 12345 │ Flags │ Fragment Offset: 0        │
│ 0011000000111001      │ 000  │ 0000000000000000           │
├────────────────────────────────────────────────────────────┤
│ TTL: 64         │ Protocol: 6 (TCP) │ Checksum: 0x1234   │
│ 01000000        │ 00000110          │ 0001001000110100   │
├────────────────────────────────────────────────────────────┤
│ Source IP: 192.168.1.100                                  │
│ 11000000.10101000.00000001.01100100                        │
├────────────────────────────────────────────────────────────┤
│ Dest IP: 8.8.8.8                                          │
│ 00001000.00001000.00001000.00001000                        │
└────────────────────────────────────────────────────────────┘
```

**Data Structure:**
```kotlin
data class PacketHeaderScene(
    val protocol: String,               // "IPv4", "Ethernet", "TCP", "DNS"
    val fields: List<HeaderField>,      // Individual protocol fields
    val totalSize: Int,                 // Total bytes
    val highlights: Map<String, String>,// Field → meaning mapping
    val description: String
) : Scene

data class HeaderField(
    val name: String,                   // "Version", "IHL", "TTL"
    val bitOffset: Int,                 // Start bit position
    val bitLength: Int,                 // Number of bits
    val value: String,                  // Decimal or hex value
    val meaning: String,                // Human-readable explanation
    val highlighted: Boolean            // Is this field active?
)
```

### **TopologyScene** — Network Diagram
Shows network topology with nodes, links, and state:

```
        ┌─────────────────────────────────┐
        │     Internet (AS 65001)         │
        └────────────┬────────────────────┘
                     │ BGP Peer
        ┌────────────┴────────────┐
        │   Core Router (R1)      │
        │   10.0.0.1              │
        └────────────┬────────────┘
                     │ OSPF Neighbor
        ┌────────────┴────────────┐
     ┌──┴──┐                   ┌──┴──┐
     │ Sw1 │ (VLAN 10/20)     │ Sw2 │ (VLAN 30)
     └──┬──┘                   └──┬──┘
    ┌───┴────┬────────┐      ┌───┴──┐
    │        │        │      │      │
  PC1      PC2      PC3   PC4     PC5
10.1.1.10 10.1.1.11 10.1.1.12 10.1.2.10 10.1.2.11
```

**Data Structure:**
```kotlin
data class TopologyScene(
    val nodes: List<TopoNode>,          // Routers, switches, hosts
    val links: List<TopoLink>,          // Connections between nodes
    val protocols: List<String>,        // Active protocols (OSPF, BGP, STP)
    val description: String
) : Scene

data class TopoNode(
    val id: String,
    val label: String,
    val type: String,                   // "router", "switch", "host"
    val ip: String?,                    // IP address if applicable
    val highlighted: Boolean
)

data class TopoLink(
    val from: String,
    val to: String,
    val bandwidth: String?,             // "1Gbps", "100Mbps"
    val status: String,                 // "up", "down", "blocked" (STP)
    val protocol: String?               // "OSPF", "BGP", "STP"
)
```

---

## 📚 Topic Implementation Plan

### **Topic 1: Ethernet Frame**

**Frames to Generate:**
1. Frame structure (48 bits dest MAC, 48 bits src MAC, 16 bits type, etc.)
2. MAC address format (hexadecimal pairs)
3. Frame types (unicast, broadcast, multicast)
4. Checksum and FCS

**Runner:** `EthernetRunner.kt`

**Visualizer:** PacketHeaderScene showing frame fields

---

### **Topic 2: ARP (Address Resolution Protocol)**

**Frames to Generate:**
1. ARP message format
2. ARP request (who has IP X.X.X.X?)
3. ARP reply (IP is at MAC AA:BB:CC:DD:EE:FF)
4. ARP cache learning
5. Gratuitous ARP

**Runner:** `ARPRunner.kt`

**Visualizer:** PacketFlowScene showing request/reply exchange

---

### **Topic 8: IPv4 Header**

**Frames to Generate:**
1. IPv4 header structure (20-60 bytes)
2. Version, IHL, DSCP, ECN
3. Total Length, Identification, Flags
4. TTL and Protocol fields
5. Source and Destination IP addresses
6. Options and padding

**Runner:** `IPv4HeaderRunner.kt`

**Visualizer:** PacketHeaderScene with bit-level fields

---

### **Topic 16: TCP Handshake**

**Frames to Generate:**
1. TCP header structure
2. SYN packet (client initiates)
3. SYN-ACK packet (server responds)
4. ACK packet (client confirms)
5. Connection established state
6. Window sizes and sequence numbers

**Runner:** `TCPHandshakeRunner.kt`

**Visualizer:** PacketFlowScene showing 3-way handshake

---

### **Topic 12: ICMP & Traceroute**

**Frames to Generate:**
1. ICMP header format
2. Echo Request (ping)
3. Echo Reply
4. Time Exceeded
5. Traceroute sequence
6. TTL decrement at each hop

**Runner:** `ICMPTracerouteRunner.kt`

**Visualizer:** PacketFlowScene showing TTL progression

---

## 🎨 Renderer Implementations

### **PacketFlowRenderer** — Sequence Diagram
- Timeline on X-axis
- Nodes on Y-axis
- Packet arrows between nodes
- Sequence numbers
- Timestamps
- Animation showing packet movement

### **PacketHeaderRenderer** — Protocol Fields
- Byte-by-byte layout
- Bit field visualization
- Color-coded field types
- Value display (decimal/hex/binary)
- Meaning tooltips
- Interactive field highlighting

### **TopologyRenderer** — Network Diagram
- Node positioning
- Link drawing
- Color-coded status
- Protocol labels
- IP address display
- Path highlighting (for routing)

---

## 🛠️ Implementation Steps

### **Week 1: Foundation**
- [ ] Create PacketFlowScene and PacketFlowRenderer
- [ ] Create PacketHeaderScene and PacketHeaderRenderer
- [ ] Create TopologyScene and TopologyRenderer
- [ ] Prove with Ethernet & ARP examples

### **Week 2: Layer 2 Topics**
- [ ] EthernetRunner (topic 1)
- [ ] ARPRunner (topic 2)
- [ ] MACLearningRunner (topic 3)
- [ ] VLANRunner (topic 4)
- [ ] Unit tests for each

### **Week 3: Layer 3 Topics**
- [ ] IPv4HeaderRunner (topic 8)
- [ ] IPv6HeaderRunner (topic 9)
- [ ] SubnettingRunner (topic 10)
- [ ] LongestPrefixMatchRunner (topic 11)
- [ ] Unit tests for each

### **Week 4: Layer 4 & Routing**
- [ ] TCPHandshakeRunner (topic 16)
- [ ] TCPTeardownRunner (topic 17)
- [ ] ICMPTracerouteRunner (topic 12)
- [ ] DNSResolutionRunner (topic 18)
- [ ] OSPFRunner (topic 14)
- [ ] BGPRunner (topic 15)
- [ ] Unit tests for all

---

## 📊 Testing Strategy

**Unit Tests:** Each runner generates deterministic packets
- Verify packet count and types
- Check node relationships
- Validate IP addresses and MAC addresses
- Confirm protocol field values

**Visual Tests:** Manual verification
- Packet flow arrows point correctly
- Protocol fields render accurately
- Colors represent correct meanings
- Topology layout is readable

**Integration Tests:**
- Topics load into UI
- Playback works smoothly
- Animations flow naturally

---

## 🎓 Learning Outcomes

After Phase 4, users understand:

✅ **Layer 2 Fundamentals**
- Ethernet frame structure
- MAC addressing and ARP
- Switch learning and forwarding
- VLANs and port isolation

✅ **Switching & Redundancy**
- STP root election
- Port states and roles
- Loop prevention
- VLAN trunking

✅ **Layer 3 Fundamentals**
- IPv4 and IPv6 headers
- Subnetting and CIDR notation
- IP routing tables
- Longest-prefix match lookup

✅ **Routing Protocols**
- OSPF link-state routing
- BGP interdomain routing
- Path selection algorithms
- AS path propagation

✅ **Connection Management**
- TCP 3-way handshake
- TCP graceful closure
- Window sizes and flow control
- Sequence and acknowledgement numbers

✅ **Application Protocols**
- DNS recursive resolution
- DHCP lease process
- ICMP and traceroute
- NAT address translation

---

## 🚀 Success Criteria

✅ All 20 topics implemented  
✅ 3 new scene/renderer types working  
✅ 150+ frames generated  
✅ All runners have unit tests (100% passing)  
✅ Visualizations are intuitive and educational  
✅ Color coding is consistent  
✅ Performance is smooth (60fps)  
✅ Code is well-documented  

---

## 📁 Files to Create

**New Package:** `com.interviewpreplab.features.networking`

```
features/networking/
├── runners/
│   ├── layer2/
│   │   ├── EthernetRunner.kt
│   │   ├── ARPRunner.kt
│   │   ├── MACLearningRunner.kt
│   │   ├── VLANRunner.kt
│   │   ├── STPRunner.kt
│   │   ├── LACPRunner.kt
│   │   └── SwitchDiagnosticsRunner.kt
│   ├── layer3/
│   │   ├── IPv4HeaderRunner.kt
│   │   ├── IPv6HeaderRunner.kt
│   │   ├── SubnettingRunner.kt
│   │   ├── LongestPrefixMatchRunner.kt
│   │   ├── ICMPTracerouteRunner.kt
│   │   ├── NATRunner.kt
│   │   ├── OSPFRunner.kt
│   │   └── BGPRunner.kt
│   └── layer4/
│       ├── TCPHandshakeRunner.kt
│       ├── TCPTeardownRunner.kt
│       ├── DNSResolutionRunner.kt
│       ├── DHCPLeaseRunner.kt
│       └── TLSHandshakeRunner.kt
├── tests/
│   ├── EthernetRunnerTest.kt
│   ├── ARPRunnerTest.kt
│   ├── ... (20 test files)
└── scenes/
    ├── PacketFlowScene.kt (core/model/)
    ├── PacketHeaderScene.kt (core/model/)
    └── TopologyScene.kt (core/model/)

core/model/
├── PacketFlowScene.kt (new)
├── PacketHeaderScene.kt (new)
├── TopologyScene.kt (new)
├── Node.kt (new)
├── Packet.kt (new)
├── TopoNode.kt (new)
├── TopoLink.kt (new)
└── HeaderField.kt (new)

core/ui/
├── PacketFlowRenderer.kt (new)
├── PacketHeaderRenderer.kt (new)
└── TopologyRenderer.kt (new)
```

---

## 🎯 Phase 4 Roadmap

```
Week 1: Renderers & Foundation        [████░░░░░]
Week 2: Layer 2 Topics (7)            [░░░░████░]
Week 3: Layer 3 Topics (8)            [░░░░░░██░]
Week 4: Layer 4 & Polish (5)          [░░░░░░░░░]
```

---

## 🏁 Phase 4 Completion

When complete, users will have:

- ✅ **20 interactive networking topics**
- ✅ **150+ educational packet flow diagrams**
- ✅ **Protocol header visualizations**
- ✅ **Network topology diagrams**
- ✅ **Understanding of Layer 2-4 fundamentals**
- ✅ **Preparation for networking interviews**

**Phase 4 prepares developers for:**
- Network administration roles
- Network engineering interviews
- Cloud infrastructure work
- Cybersecurity understanding
- DevOps platform knowledge

---

**Ready to build Phase 4!** 🚀

