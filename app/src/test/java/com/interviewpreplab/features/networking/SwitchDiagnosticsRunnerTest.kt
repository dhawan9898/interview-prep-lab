package com.interviewpreplab.features.networking

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SwitchDiagnosticsRunnerTest {
    @Test
    fun testFrameCount() {
        val frames = SwitchDiagnosticsRunner.run()
        assertEquals(9, frames.size, "SwitchDiagnosticsRunner should generate 9 frames")
    }

    @Test
    fun testFrameStructure() {
        val frames = SwitchDiagnosticsRunner.run()
        frames.forEach { frame ->
            assertNotNull(frame.narration, "Frame narration should not be null")
            assertNotNull(frame.phase, "Frame phase should not be null")
        }
    }

    @Test
    fun testMACTableCommandPresent() {
        val frames = SwitchDiagnosticsRunner.run()
        assert(frames.any { it.phase == "cmd_mac_table" }, "Should explain MAC table command")
    }

    @Test
    fun testDiagnosisPresent() {
        val frames = SwitchDiagnosticsRunner.run()
        assert(frames.any { it.phase.contains("diagnosis") }, "Should explain troubleshooting")
    }
}
