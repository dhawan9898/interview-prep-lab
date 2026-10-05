package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object TCPTeardownRunner {
    private fun baseFrames(): List<Frame> = listOf(
        Frame("TCP connection teardown (FIN-ACK handshake): closes connection gracefully. 4-way handshake.", "intro",
            mapOf("handshake_way" to "4", "purpose" to "graceful close")),

        Frame("Initiator (client) sends FIN (finish) flag. Signals: 'I have no more data to send.'", "step1_fin",
            mapOf("sender" to "client", "flag" to "FIN", "meaning" to "no more data")),

        Frame("Responder (server) sends ACK to the FIN. Signals: 'I received your FIN.'", "step2_ack",
            mapOf("sender" to "server", "flag" to "ACK", "acknowledges" to "client FIN")),

        Frame("Responder sends its own FIN. Signals: 'I also have no more data to send.'", "step3_responder_fin",
            mapOf("sender" to "server", "flag" to "FIN", "meaning" to "server done sending")),

        Frame("Initiator sends ACK to responder's FIN. Signals: 'I received your FIN. Connection closed.'", "step4_final_ack",
            mapOf("sender" to "client", "flag" to "ACK", "result" to "connection closed")),

        Frame("Half-close: after step 1-2, initiator can't send but can receive (useful for piped processes). Server still sends data.", "half_close",
            mapOf("state" to "FIN-WAIT", "capability" to "receive only")),

        Frame("TIME_WAIT state: initiator waits 2*MSL (max segment lifetime, ~30-60 sec) after final ACK. Absorbs delayed packets.", "time_wait",
            mapOf("duration" to "2*MSL", "purpose" to "safety")),

        Frame("RST (reset): abrupt close. TCP sends RST flag instead of FIN. Used for error conditions (invalid state, attack defense).", "rst",
            mapOf("type" to "abrupt", "use" to "error/DoS defense")),

        Frame("TCP teardown ensures both sides agree connection is closed. Prevents data loss and packet confusion.", "summary",
            mapOf("reliability" to "high", "steps" to "4"))
    )

    private val nodes = listOf(
        FlowNode("client", "Initiator", "host"),
        FlowNode("server", "Responder", "host")
    )

    fun run(): List<Frame> = baseFrames().withFlow(
        nodes = nodes,
        stages = mapOf(
            1 to stage(FlowStep("client", "server", "TCP", "FIN seq=x", setOf("FIN"))),
            2 to stage(FlowStep("server", "client", "TCP", "ACK ack=x+1", setOf("ACK"))),
            3 to stage(FlowStep("server", "client", "TCP", "FIN seq=y", setOf("FIN"))),
            4 to stage(FlowStep("client", "server", "TCP", "ACK ack=y+1", setOf("ACK")))
        ),
        description = "TCP connection close"
    )
}
