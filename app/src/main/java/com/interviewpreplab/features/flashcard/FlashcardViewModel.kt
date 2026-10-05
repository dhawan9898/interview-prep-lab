package com.interviewpreplab.features.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interviewpreplab.core.database.entities.FlashcardEntity
import com.interviewpreplab.core.database.entities.FlashcardReviewEntity
import com.interviewpreplab.core.database.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FlashcardUIState(
    val currentCard: FlashcardEntity? = null,
    val cardIndex: Int = 0,
    val totalCards: Int = 0,
    val isFlipped: Boolean = false,
    val reviewQuality: Int? = null,
    val completedToday: Int = 0,
    val remainingToday: Int = 0,
    val streak: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FlashcardViewModel @Inject constructor(
    private val progressRepository: ProgressRepository,
    private val srsAlgorithm: SRSAlgorithm
) : ViewModel() {

    private val _uiState = MutableStateFlow(FlashcardUIState())
    val uiState: StateFlow<FlashcardUIState> = _uiState.asStateFlow()

    private var todayCards: List<FlashcardEntity> = emptyList()
    private val sessionStartTime = System.currentTimeMillis()

    fun loadTodayReviewQueue(topicId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // In a real implementation, this would load from FlashcardDao
            // For now, mock the data
            todayCards = emptyList()

            if (todayCards.isNotEmpty()) {
                _uiState.value = FlashcardUIState(
                    currentCard = todayCards.first(),
                    cardIndex = 0,
                    totalCards = todayCards.size,
                    remainingToday = todayCards.size,
                    isLoading = false
                )
            } else {
                _uiState.value = FlashcardUIState(
                    isLoading = false,
                    completedToday = 0,
                    remainingToday = 0,
                    error = "No cards to review today"
                )
            }
        }
    }

    fun toggleFlip() {
        _uiState.value = _uiState.value.copy(
            isFlipped = !_uiState.value.isFlipped
        )
    }

    fun submitReview(quality: Int) {
        val state = _uiState.value
        val currentCard = state.currentCard ?: return

        if (quality !in 0..5) {
            _uiState.value = _uiState.value.copy(error = "Invalid quality rating")
            return
        }

        viewModelScope.launch {
            // Calculate next review parameters using SM-2
            val (nextInterval, newEaseFactor) = srsAlgorithm.calculateNextReview(
                quality = quality,
                easeFactor = currentCard.easeFactor,
                interval = currentCard.interval,
                repetitions = currentCard.repetitions
            )

            // Create review record
            val review = FlashcardReviewEntity(
                reviewId = System.currentTimeMillis().toString(),
                cardId = currentCard.cardId,
                quality = quality,
                easeFactor = newEaseFactor,
                interval = nextInterval,
                nextReview = srsAlgorithm.calculateNextReviewTimestamp(nextInterval),
                timestamp = System.currentTimeMillis()
            )

            // Update card (in real implementation, save to DB)
            val updatedCard = currentCard.copy(
                easeFactor = newEaseFactor,
                interval = nextInterval,
                repetitions = currentCard.repetitions + 1,
                lastReviewed = System.currentTimeMillis(),
                nextReview = review.nextReview
            )

            // Move to next card
            nextCard()
        }
    }

    fun nextCard() {
        val state = _uiState.value
        val nextIndex = state.cardIndex + 1

        if (nextIndex < todayCards.size) {
            _uiState.value = FlashcardUIState(
                currentCard = todayCards[nextIndex],
                cardIndex = nextIndex,
                totalCards = todayCards.size,
                completedToday = nextIndex,
                remainingToday = todayCards.size - nextIndex,
                streak = state.streak + 1,
                isFlipped = false
            )
        } else {
            completeSessions()
        }
    }

    fun skipCard() {
        nextCard()
    }

    private fun completeSessions() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = "Session completed! All cards reviewed."
        )
    }

    fun resetSession() {
        _uiState.value = FlashcardUIState()
        todayCards = emptyList()
    }

    fun getProgressPercentage(): Float {
        val state = _uiState.value
        return if (state.totalCards > 0) {
            (state.completedToday.toFloat()) / state.totalCards
        } else {
            0f
        }
    }

    fun getQualityLabel(quality: Int): String {
        return srsAlgorithm.getQualityDescription(quality)
    }

    fun estimateRetention(easeFactor: Double): String {
        val rate = srsAlgorithm.estimateRetentionRate(easeFactor)
        return String.format("%.0f%% retention", rate)
    }

    fun getNextReviewIn(nextReviewTime: Long): String {
        val now = System.currentTimeMillis()
        val diffMs = nextReviewTime - now

        if (diffMs <= 0) return "Due now"

        val days = diffMs / (24 * 60 * 60 * 1000)
        val hours = (diffMs % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000)
        val minutes = (diffMs % (60 * 60 * 1000)) / (60 * 1000)

        return when {
            days > 0 -> "${days}d ${hours}h"
            hours > 0 -> "${hours}h ${minutes}m"
            else -> "${minutes}m"
        }
    }

    fun getDifficultyColor(difficulty: String?): String {
        return when (difficulty?.lowercase()) {
            "easy" -> "#4CAF50"
            "medium" -> "#FFC107"
            "hard" -> "#F44336"
            else -> "#9E9E9E"
        }
    }
}
