package com.interviewpreplab.features.quiz

import com.interviewpreplab.core.database.entities.QuizAttemptEntity
import com.interviewpreplab.core.database.entities.QuizEntity

data class QuizQuestion(
    val quizId: String,
    val topicId: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val difficulty: String,
    val explanation: String,
    val quizType: String // MCQ, PREDICT_FRAME, CODE_COMPLETION
)

data class QuizAttempt(
    val quizId: String,
    val userAnswer: String,
    val isCorrect: Boolean,
    val timeSpent: Long // milliseconds
)

data class QuizStats(
    val totalQuizzes: Int,
    val correctAnswers: Int,
    val accuracy: Double, // 0.0 to 100.0
    val averageTimePerQuestion: Long, // milliseconds
    val byDifficulty: Map<String, DifficultyStats>
)

data class DifficultyStats(
    val total: Int,
    val correct: Int,
    val accuracy: Double
)

object QuizEngine {

    fun createQuestionFromEntity(entity: QuizEntity): QuizQuestion {
        val optionsList = parseOptions(entity.options)
        return QuizQuestion(
            quizId = entity.quizId,
            topicId = entity.topicId,
            question = entity.question,
            options = optionsList,
            correctAnswer = entity.correctAnswer,
            difficulty = entity.difficulty,
            explanation = entity.explanation,
            quizType = entity.quizType
        )
    }

    fun validateAnswer(question: QuizQuestion, userAnswer: String): Boolean {
        return userAnswer.trim().equals(question.correctAnswer.trim(), ignoreCase = true)
    }

    fun calculateAccuracy(attempts: List<QuizAttemptEntity>): Double {
        if (attempts.isEmpty()) return 0.0
        val correct = attempts.count { it.isCorrect }
        return (correct.toDouble() / attempts.size) * 100.0
    }

    fun calculateAverageTime(attempts: List<QuizAttemptEntity>): Long {
        if (attempts.isEmpty()) return 0L
        return attempts.map { it.timeSpent }.sum() / attempts.size
    }

    fun buildStats(
        quizzes: List<QuizEntity>,
        attempts: List<QuizAttemptEntity>
    ): QuizStats {
        val totalQuizzes = quizzes.size
        val correctAnswers = attempts.count { it.isCorrect }
        val accuracy = if (attempts.isNotEmpty()) {
            (correctAnswers.toDouble() / attempts.size) * 100.0
        } else {
            0.0
        }

        val byDifficulty = mutableMapOf<String, DifficultyStats>()

        quizzes.groupBy { it.difficulty }.forEach { (difficulty, diffQuizzes) ->
            val diffAttempts = attempts.filter { attempt ->
                diffQuizzes.any { it.quizId == attempt.quizId }
            }
            val diffCorrect = diffAttempts.count { it.isCorrect }
            val diffAccuracy = if (diffAttempts.isNotEmpty()) {
                (diffCorrect.toDouble() / diffAttempts.size) * 100.0
            } else {
                0.0
            }

            byDifficulty[difficulty] = DifficultyStats(
                total = diffQuizzes.size,
                correct = diffCorrect,
                accuracy = diffAccuracy
            )
        }

        return QuizStats(
            totalQuizzes = totalQuizzes,
            correctAnswers = correctAnswers,
            accuracy = accuracy,
            averageTimePerQuestion = calculateAverageTime(attempts),
            byDifficulty = byDifficulty
        )
    }

    fun getQuizzesByDifficulty(
        quizzes: List<QuizEntity>,
        difficulty: String?
    ): List<QuizEntity> {
        return if (difficulty != null) {
            quizzes.filter { it.difficulty == difficulty }
        } else {
            quizzes
        }
    }

    fun getRandomQuizzes(quizzes: List<QuizEntity>, count: Int): List<QuizEntity> {
        return quizzes.shuffled().take(count)
    }

    fun getDifficultyColor(difficulty: String): String {
        return when (difficulty.lowercase()) {
            "easy" -> "#4CAF50"    // Green
            "medium" -> "#FFC107"  // Amber
            "hard" -> "#F44336"    // Red
            else -> "#9E9E9E"      // Gray
        }
    }

    fun getDifficultyLevel(difficulty: String): Int {
        return when (difficulty.lowercase()) {
            "easy" -> 1
            "medium" -> 2
            "hard" -> 3
            else -> 0
        }
    }

    private fun parseOptions(optionsJson: String): List<String> {
        return try {
            // Simple JSON array parsing: ["option1", "option2", "option3"]
            optionsJson
                .removePrefix("[").removeSuffix("]")
                .split(",")
                .map { it.trim().removePrefix("\"").removeSuffix("\"") }
                .filter { it.isNotEmpty() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getQuizTypeLabel(quizType: String): String {
        return when (quizType.uppercase()) {
            "MCQ" -> "Multiple Choice"
            "PREDICT_FRAME" -> "Predict the Frame"
            "CODE_COMPLETION" -> "Code Completion"
            else -> "Quiz"
        }
    }
}
