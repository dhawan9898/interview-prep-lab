package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object DNSResolutionRunner {
    fun run(): List<Frame> = listOf(
        Frame("DNS (Domain Name System): translates domain names to IP addresses. Client→Resolver→Root→TLD→Authoritative.", "intro",
            mapOf("layers" to "4", "hierarchy" to "tree")),

        Frame("User enters example.com in browser. Browser queries local resolver (ISP DNS or 8.8.8.8).", "step1_query",
            mapOf("domain" to "example.com", "target" to "resolver")),

        Frame("Resolver asks root nameserver: 'Where is example.com?' Root replies: 'Ask the .com TLD server.'", "step2_root",
            mapOf("query_target" to "root", "response" to "TLD address")),

        Frame("Resolver asks TLD (.com) nameserver: 'Where is example.com?' TLD replies: 'Ask example.com's authoritative server.'", "step3_tld",
            mapOf("query_target" to "TLD", "response" to "authoritative server")),

        Frame("Resolver asks example.com's authoritative nameserver: 'What's the IP of example.com?' Returns: 93.184.216.34", "step4_authoritative",
            mapOf("query_target" to "authoritative", "response" to "IP address")),

        Frame("Resolver caches result, returns to browser: 'example.com = 93.184.216.34'. Browser caches too (TTL-based).", "step5_response",
            mapOf("caching" to "resolver, browser", "ttl" to "variable")),

        Frame("Browser opens TCP connection to 93.184.216.34 (HTTP/HTTPS). DNS job done.", "step6_connect",
            mapOf("next_step" to "TCP handshake", "uses" to "resolved IP")),

        Frame("Record types: A (IPv4), AAAA (IPv6), CNAME (alias), MX (mail), TXT (text records).", "record_types",
            mapOf("common" to "A, AAAA, MX", "others" to "CNAME, TXT, NS")),

        Frame("DNS is hierarchical, distributed, cached. Failure = Internet broken (critical). Recursive query (resolver does work for client).", "summary",
            mapOf("architecture" to "distributed tree", "redundancy" to "critical"))
    )
}
