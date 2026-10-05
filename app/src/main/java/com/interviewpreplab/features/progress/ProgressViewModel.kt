package com.interviewpreplab.features.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interviewpreplab.core.database.entities.TopicProgressEntity
import com.interviewpreplab.core.database.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProgressUIState(
    val allProgress: List<TopicProgressEntity> = emptyList(),
    val favorites: List<TopicProgressEntity> = emptyList(),
    val completedCount: Int = 0,
    val totalTimeSpent: Long = 0L,
    val mostStudiedTopics: List<TopicProgressEntity> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProgressUIState())
    val uiState: StateFlow<ProgressUIState> = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        loadProgressData()
    }

    private fun loadProgressData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Collect all progress
            progressRepository.getAllProgress().collect { allProgress ->
                val favorites = allProgress.filter { it.isFavorited }
                updateState { copy(allProgress = allProgress, favorites = favorites) }
            }
        }

        viewModelScope.launch {
            progressRepository.getCompletedCount().collect { count ->
                updateState { copy(completedCount = count) }
            }
        }

        viewModelScope.launch {
            progressRepository.getTotalTimeSpent().collect { time ->
                updateState { copy(totalTimeSpent = time ?: 0L) }
            }
        }

        viewModelScope.launch {
            progressRepository.getMostStudiedTopics(limit = 5).collect { topics ->
                updateState { copy(mostStudiedTopics = topics, isLoading = false) }
            }
        }
    }

    fun setSelectedCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(topicId: String) {
        viewModelScope.launch {
            val currentProgress = _uiState.value.allProgress.find { it.topicId == topicId }
            if (currentProgress != null) {
                progressRepository.toggleFavorite(topicId, !currentProgress.isFavorited)
            }
        }
    }

    fun markCompleted(topicId: String) {
        viewModelScope.launch {
            progressRepository.markCompleted(topicId)
        }
    }

    fun updateTopicViewTime(topicId: String, durationMs: Long) {
        viewModelScope.launch {
            progressRepository.updateViewTime(topicId, durationMs)
        }
    }

    fun refreshData() {
        loadProgressData()
    }

    private fun updateState(update: ProgressUIState.() -> ProgressUIState) {
        _uiState.value = _uiState.value.update()
    }

    // Helper functions for UI
    fun getProgressPercentage(topicId: String): Float {
        val progress = _uiState.value.allProgress.find { it.topicId == topicId }
        return if (progress?.isCompleted == true) 1.0f else 0.5f
    }

    fun getFormattedTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return when {
            hours > 0 -> "${hours}h ${minutes % 60}m"
            minutes > 0 -> "${minutes}m"
            else -> "${seconds}s"
        }
    }

    fun getCategories(): List<String> {
        return _uiState.value.allProgress.map { it.category }.distinct().sorted()
    }

    fun getFilteredProgress(): List<TopicProgressEntity> {
        val category = _selectedCategory.value
        return if (category != null) {
            _uiState.value.allProgress.filter { it.category == category }
        } else {
            _uiState.value.allProgress
        }
    }
}
