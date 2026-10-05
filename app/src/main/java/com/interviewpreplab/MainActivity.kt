package com.interviewpreplab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.interviewpreplab.core.model.BarsScene
import com.interviewpreplab.core.player.PlayerViewModel
import com.interviewpreplab.core.ui.BarsRenderer
import com.interviewpreplab.core.ui.PlayerControls
import com.interviewpreplab.features.sorts.BubbleSortRunner
import com.interviewpreplab.ui.theme.InterviewPrepLabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val playerVM: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InterviewPrepLabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BubbleSortDemo(playerVM)
                }
            }
        }

        // Load bubble sort demo on first launch
        val demoArray = listOf(5, 2, 8, 1, 9, 3)
        val frames = BubbleSortRunner.run(demoArray)
        playerVM.loadFrames(frames)
    }
}

@Composable
fun BubbleSortDemo(playerVM: PlayerViewModel) {
    val state by playerVM.state.collectAsState()
    var demoArray by remember { mutableStateOf(listOf(5, 2, 8, 1, 9, 3)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            "Bubble Sort",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Visualization
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val scene = state.currentFrame?.scene as? BarsScene
            if (scene != null) {
                val maxVal = demoArray.maxOrNull() ?: 1
                BarsRenderer(scene, maxVal)
            }
        }

        // Narration
        state.currentFrame?.let { frame ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    "Phase: ${frame.phase}",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    frame.narr,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Stats
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    frame.stats.forEach { (key, value) ->
                        Text(
                            "$key: $value",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        // Controls
        PlayerControls(
            state = state,
            onPlay = playerVM::play,
            onPause = playerVM::pause,
            onStepBack = playerVM::stepBack,
            onStepForward = playerVM::stepForward,
            onReset = playerVM::reset,
            onScrub = playerVM::scrubTo,
            onSpeedChange = playerVM::setSpeed
        )
    }
}
