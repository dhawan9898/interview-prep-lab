package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.PacketFlowScene
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FlowAttachTest {
    @Test
    fun flowRunnersKeepTheirFrameCountAndGainScenes() {
        val expected = mapOf(
            "tcp-teardown" to TCPTeardownRunner.run(),
            "dns" to DNSResolutionRunner.run(),
            "dhcp" to DHCPLeaseRunner.run(),
            "tls" to TLSHandshakeRunner.run(),
            "icmp" to ICMPTracerouteRunner.run(),
            "nat" to NATRunner.run()
        )
        expected.forEach { (name, frames) ->
            assertEquals(9, frames.size, "$name should still have 9 frames")
            assertTrue(frames.all { it.scene is PacketFlowScene }, "$name frames should all carry a flow scene")
        }
    }

    @Test
    fun packetsAccumulateAndResetWhenRequested() {
        val frames = ICMPTracerouteRunner.run()
        val counts = frames.map { (it.scene as PacketFlowScene).packets.size }
        assertEquals(2, counts[1], "ping adds echo request + reply")
        assertEquals(2, counts[3], "traceroute reset clears the ping packets")
        assertEquals(4, counts[4], "second hop accumulates")
        assertEquals(6, counts[5], "destination accumulates")
    }
}
