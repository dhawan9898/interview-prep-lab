package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TLSHandshakeRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = TLSHandshakeRunner.run()
        assertEquals(9, frames.size, "TLSHandshakeRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = TLSHandshakeRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narration, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testClientHelloPresent() {
        val frames = TLSHandshakeRunner.run()
        assert(frames.any { it.phase == "step1_client_hello" }, "Should explain ClientHello")
    }

    @Test
    fun testCertificatePresent() {
        val frames = TLSHandshakeRunner.run()
        assert(frames.any { it.phase == "step3_cert" }, "Should explain certificate exchange")
    }
}
