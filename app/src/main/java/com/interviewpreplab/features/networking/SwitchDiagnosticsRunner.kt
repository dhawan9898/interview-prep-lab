package com.interviewpreplab.features.networking

import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.FlowNode
import com.interviewpreplab.core.model.FlowPacket
import com.interviewpreplab.core.model.PacketFlowScene

object SwitchDiagnosticsRunner {
    fun run(): List<Frame> = listOf(
        Frame("Switch diagnostics: troubleshoot switching problems (loops, MAC table full, STP issues, VLAN misconfigs).", "intro",
            mapOf("focus" to "layer 2", "goal" to "verify connectivity")),

        Frame("'show mac address-table': displays learned MAC → port mappings. Check if devices on expected ports.", "cmd_mac_table",
            mapOf("command" to "show mac-table", "shows" to "MAC → port entries")),

        Frame("'show spanning-tree': displays root bridge, port roles (root/designated/blocked), BPDIUs sent/received.", "cmd_stp",
            mapOf("command" to "show stp", "shows" to "STP topology")),

        Frame("'show interfaces': shows port status (up/down), errors, collisions, discards. Detects bad cables, congestion.", "cmd_interfaces",
            mapOf("command" to "show int", "shows" to "port statistics")),

        Frame("'show vlan': lists VLAN IDs, port membership. Verify access/trunk assignments. Catch misconfigs.", "cmd_vlan",
            mapOf("command" to "show vlan", "shows" to "VLAN membership")),

        Frame("'show port-channel': displays LACP bundle status, member links active/inactive. Spot failed links.", "cmd_lacp",
            mapOf("command" to "show lacp", "shows" to "bundle health")),

        Frame("Broadcast storm: MAC table full, STP converging, unknown unicast flooding. Check 'show processes' (CPU spike).", "diagnosis_storm",
            mapOf("symptoms" to "slow network, high CPU", "check" to "STP/MAC learning")),

        Frame("VLAN not working: check port VLAN assignment (access vs trunk), native VLAN mismatch, routing between VLANs.", "diagnosis_vlan",
            mapOf("common_issue" to "port VLAN mismatch", "fix" to "verify config")),

        Frame("Diagnostics = understand topology, monitor health, detect misconfigs before users complain. Proactive ops.", "summary",
            mapOf("approach" to "proactive", "tools" to "show commands"))
    )
}
