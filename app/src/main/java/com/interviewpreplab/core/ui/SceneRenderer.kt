package com.interviewpreplab.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.model.Frame
import com.interviewpreplab.core.model.GraphScene
import com.interviewpreplab.core.model.ListScene
import com.interviewpreplab.core.model.MemoryScene
import com.interviewpreplab.core.model.PacketFlowScene
import com.interviewpreplab.core.model.PacketHeaderScene
import com.interviewpreplab.core.model.SlotsScene
import com.interviewpreplab.core.model.TextScene
import com.interviewpreplab.core.model.TopologyScene
import com.interviewpreplab.core.model.TreeScene

/** Picks the renderer from the frame's own scene type, so a runner and its renderer can never disagree. */
@Composable
fun SceneRenderer(
    frames: List<Frame>,
    index: Int,
    verticalSlots: Boolean,
    modifier: Modifier = Modifier
) {
    val frame = frames.getOrNull(index) ?: return
    val m = modifier.fillMaxWidth()
    when (val scene = frame.scene) {
        is BarsScene -> BarsRenderer(scene, scene.bars.maxOfOrNull { it.value }?.coerceAtLeast(1) ?: 1, m)
        is SlotsScene -> SlotsRenderer(scene, if (verticalSlots) "v" else "h", m)
        is ListScene -> ListRenderer(scene, m)
        is TreeScene -> TreeRenderer(scene, m)
        is GraphScene -> GraphRenderer(scene, m)
        is MemoryScene -> MemoryRenderer(scene, m)
        is PacketFlowScene -> PacketFlowRenderer(scene, m)
        is PacketHeaderScene -> PacketHeaderRenderer(scene, m)
        is TopologyScene -> TopologyRenderer(scene, m)
        TextScene -> ConceptRenderer(frames, index, m)
    }
}
