package com.interviewpreplab.features.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interviewpreplab.core.database.entities.QuizAttemptEntity
import com.interviewpreplab.core.database.repository.ProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuizUIState(
    val currentQuestion: QuizQuestion? = null,
    val questionIndex: Int = 0,
    val totalQuestions: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswered: Boolean = false,
    val isCorrect: Boolean? = null,
    val timeSpent: Long = 0L,
    val correctCount: Int = 0,
    val isComplete: Boolean = false,
    val stats: QuizStats? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUIState())
    val uiState: StateFlow<QuizUIState> = _uiState.asStateFlow()

    private var quizStartTime = 0L
    private var questionStartTime = 0L
    private var questions: List<QuizQuestion> = emptyList()

    fun startQuiz(questions: List<QuizQuestion>) {
        this.questions = questions
        quizStartTime = System.currentTimeMillis()

        if (questions.isNotEmpty()) {
            questionStartTime = System.currentTimeMillis()
            _uiState.value = QuizUIState(
                currentQuestion = questions.first(),
                questionIndex = 0,
                totalQuestions = questions.size,
                isLoading = false
            )
        } else {
            _uiState.value = _uiState.value.copy(
                error = "No questions available",
                isLoading = false
            )
        }
    }

    fun selectAnswer(answer: String) {
        _uiState.value = _uiState.value.copy(selectedAnswer = answer)
    }

    fun submitAnswer(topicId: String) {
        val state = _uiState.value
        val currentQuestion = state.currentQuestion ?: return
        val selectedAnswer = state.selectedAnswer

        if (selectedAnswer == null) {
            _uiState.value = _uiState.value.copy(error = "Please select an answer")
            return
        }

        val isCorrect = QuizEngine.validateAnswer(currentQuestion, selectedAnswer)
        val timeSpent = System.currentTimeMillis() - questionStartTime

        // Save attempt to database
        viewModelScope.launch {
            val attempt = QuizAttemptEntity(
                attemptId = System.currentTimeMillis().toString(),
                quizId = currentQuestion.quizId,
                userAnswer = selectedAnswer,
                isCorrect = isCorrect,
                timeSpent = timeSpent,
                timestamp = System.currentTimeMillis()
            )
            // Note: Need to inject QuizDao to save this, which will be added when integrating with DB
        }

        val newCorrectCount = state.correctCount + if (isCorrect) 1 else 0

        _uiState.value = _uiState.value.copy(
            isAnswered = true,
            isCorrect = isCorrect,
            timeSpent = timeSpent,
            correctCount = newCorrectCount
        )
    }

    fun nextQuestion() {
        val state = _uiState.value
        val nextIndex = state.questionIndex + 1

        if (nextIndex < questions.size) {
            questionStartTime = System.currentTimeMillis()
            _uiState.value = QuizUIState(
                currentQuestion = questions[nextIndex],
                questionIndex = nextIndex,
                totalQuestions = questions.size,
                correctCount = state.correctCount
            )
        } else {
            completeQuiz()
        }
    }

    fun previousQuestion() {
        val state = _uiState.value
        val prevIndex = state.questionIndex - 1

        if (prevIndex >= 0) {
            questionStartTime = System.currentTimeMillis()
            _uiState.value = QuizUIState(
                currentQuestion = questions[prevIndex],
                questionIndex = prevIndex,
                totalQuestions = questions.size,
                correctCount = state.correctCount
            )
        }
    }

    fun skipQuestion() {
        nextQuestion()
    }

    private fun completeQuiz() {
        val totalTime = System.currentTimeMillis() - quizStartTime
        val accuracy = if (questions.isNotEmpty()) {
            (_uiState.value.correctCount.toDouble() / questions.size) * 100.0
        } else {
            0.0
        }

        _uiState.value = QuizUIState(
            isComplete = true,
            correctCount = _uiState.value.correctCount,
            totalQuestions = questions.size,
            stats = QuizStats(
                totalQuizzes = questions.size,
                correctAnswers = _uiState.value.correctCount,
                accuracy = accuracy,
                averageTimePerQuestion = if (questions.isNotEmpty()) {
                    totalTime / questions.size
                } else {
                    0L
                },
                byDifficulty = emptyMap()
            )
        )
    }

    fun resetQuiz() {
        _uiState.value = QuizUIState()
        questions = emptyList()
    }

    fun retryQuiz() {
        resetQuiz()
        startQuiz(questions)
    }

    fun getProgressPercentage(): Float {
        val state = _uiState.value
        return if (state.totalQuestions > 0) {
            (state.questionIndex.toFloat() + 1) / state.totalQuestions
        } else {
            0f
        }
    }

    fun getFormattedTime(milliseconds: Long): String {
        val seconds = milliseconds / 1000
        val minutes = seconds / 60

        return when {
            minutes > 0 -> "${minutes}m ${seconds % 60}s"
            else -> "${seconds}s"
        }
    }

    fun getAccuracyColor(accuracy: Double): String {
        return when {
            accuracy >= 80.0 -> "#4CAF50" // Green
            accuracy >= 60.0 -> "#FFC107" // Amber
            else -> "#F44336" // Red
        }
    }
}
