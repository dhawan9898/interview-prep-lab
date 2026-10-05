package com.interviewpreplab.core.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interviewpreplab.core.model.Frame
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerState(
    val frames: List<Frame> = emptyList(),
    val frameIdx: Int = 0,
    val isPlaying: Boolean = false,
    val speed: Float = 1f,  // playback speed multiplier
    val currentFrame: Frame? = null
)

/**
 * Core playback engine. Owns state and animation timing.
 * Reused across all domains (sorts, trees, kernel, networking).
 */
@HiltViewModel
class PlayerViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(PlayerState())
    val state: StateFlow<PlayerState> = _state

    private var playbackJob: Job? = null
    private val baseFrameDuration = 500L  // ms per frame

    fun loadFrames(frames: List<Frame>) {
        stopPlayback()
        _state.value = PlayerState(
            frames = frames,
            frameIdx = 0,
            currentFrame = frames.getOrNull(0)
        )
    }

    fun play() {
        if (_state.value.isPlaying || _state.value.frames.isEmpty()) return
        _state.value = _state.value.copy(isPlaying = true)
        playbackJob = viewModelScope.launch {
            while (_state.value.isPlaying && _state.value.frameIdx < _state.value.frames.size - 1) {
                delay((baseFrameDuration / _state.value.speed).toLong())
                _state.value = _state.value.copy(frameIdx = _state.value.frameIdx + 1).also {
                    it.copy(currentFrame = it.frames.getOrNull(it.frameIdx))
                }.let { newState ->
                    newState.copy(currentFrame = newState.frames.getOrNull(newState.frameIdx))
                }
            }
            // Auto-stop at end
            if (_state.value.frameIdx >= _state.value.frames.size - 1) {
                _state.value = _state.value.copy(isPlaying = false)
            }
        }
    }

    fun pause() {
        _state.value = _state.value.copy(isPlaying = false)
        playbackJob?.cancel()
        playbackJob = null
    }

    fun stepForward() {
        pause()
        val newIdx = (_state.value.frameIdx + 1).coerceAtMost(_state.value.frames.size - 1)
        _state.value = _state.value.copy(frameIdx = newIdx).let { newState ->
            newState.copy(currentFrame = newState.frames.getOrNull(newState.frameIdx))
        }
    }

    fun stepBack() {
        pause()
        val newIdx = (_state.value.frameIdx - 1).coerceAtLeast(0)
        _state.value = _state.value.copy(frameIdx = newIdx).let { newState ->
            newState.copy(currentFrame = newState.frames.getOrNull(newState.frameIdx))
        }
    }

    fun scrubTo(frameIdx: Int) {
        pause()
        val clipped = frameIdx.coerceIn(0, _state.value.frames.size - 1)
        _state.value = _state.value.copy(frameIdx = clipped).let { newState ->
            newState.copy(currentFrame = newState.frames.getOrNull(newState.frameIdx))
        }
    }

    fun reset() {
        pause()
        _state.value = _state.value.copy(frameIdx = 0).let { newState ->
            newState.copy(currentFrame = newState.frames.getOrNull(0))
        }
    }

    fun setSpeed(speed: Float) {
        _state.value = _state.value.copy(speed = speed.coerceIn(0.25f, 4f))
    }

    fun stopPlayback() {
        playbackJob?.cancel()
        playbackJob = null
        _state.value = _state.value.copy(isPlaying = false)
    }

    override fun onCleared() {
        stopPlayback()
        super.onCleared()
    }
}
