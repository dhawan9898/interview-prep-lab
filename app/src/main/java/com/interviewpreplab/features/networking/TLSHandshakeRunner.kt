package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object TLSHandshakeRunner {
    fun run(): List<Frame> = listOf(
        Frame("TLS (Transport Layer Security): encrypts data between client and server. Uses certificates + key exchange.", "intro",
            mapOf("layer" to "5 (application)", "security" to "encryption + authentication")),

        Frame("TLS 1.2 handshake: 4 messages. ClientHello → ServerHello+Cert → KeyExchange → Finished. Then encrypted session.", "overview",
            mapOf("handshake_messages" to "4", "result" to "shared cipher suite")),

        Frame("ClientHello: client sends supported TLS versions, cipher suites, extensions. Includes random nonce.", "step1_client_hello",
            mapOf("includes" to "versions, ciphers, random", "initiates" to "handshake")),

        Frame("ServerHello: server picks TLS version, cipher suite from client's list. Sends server random nonce, session ID.", "step2_server_hello",
            mapOf("selects" to "version, cipher", "sends" to "random, session_id")),

        Frame("ServerCertificate: server sends X.509 certificate (public key, domain, issuer). Client verifies certificate chain.", "step3_cert",
            mapOf("contains" to "public key, domain", "verified_by" to "trusted CA")),

        Frame("ServerKeyExchange (or not in ECDHE): server sends DH parameters or ECDH public key. Enables client to compute shared secret.", "step4_key_exchange",
            mapOf("algorithm" to "ECDHE (preferred)", "result" to "ephemeral shared secret")),

        Frame("ClientKeyExchange: client sends its DH public key (or finishes ECDH). Both now compute shared secret independently.", "step5_client_key",
            mapOf("sends" to "DH public key", "result" to "both have same secret")),

        Frame("Finished (both): client sends MAC of all messages so far (encrypted). Server verifies. Then server sends its Finished.", "step6_finished",
            mapOf("provides" to "proof of handshake", "prevents" to "tampering")),

        Frame("Result: both have shared secret. Derive encryption keys (symmetric cipher), MAC key, IV. Switch to encrypted tunnel.", "summary",
            mapOf("encryption" to "symmetric (AES)", "speed" to "fast (after handshake)"))
    )
}
