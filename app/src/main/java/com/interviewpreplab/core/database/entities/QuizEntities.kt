package com.interviewpreplab.core.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class QuizType {
    MCQ,                    // Multiple choice question
    PREDICT_FRAME,          // Predict next frame in animation
    CODE_COMPLETION         // Fill in code snippet
}

@Entity(
    tableName = "quizzes",
    foreignKeys = [
        ForeignKey(
            entity = TopicProgressEntity::class,
            parentColumns = ["topicId"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuizEntity(
    @PrimaryKey
    val quizId: String,
    val topicId: String,
    val quizType: QuizType,
    val question: String,
    val options: String,                // JSON array ["A", "B", "C", "D"]
    val correctAnswer: Int,
    val difficulty: Int = 1,            // 1-5 scale
    val explanation: String = "",
    val dateCreated: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "quiz_attempts",
    foreignKeys = [
        ForeignKey(
            entity = QuizEntity::class,
            parentColumns = ["quizId"],
            childColumns = ["quizId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class QuizAttemptEntity(
    @PrimaryKey
    val attemptId: String,
    val quizId: String,
    val userAnswer: Int,
    val isCorrect: Boolean,
    val timeSpent: Long = 0L,           // milliseconds
    val timestamp: Long = System.currentTimeMillis()
)

data class QuizWithAttempts(
    val quiz: QuizEntity,
    val attempts: List<QuizAttemptEntity> = emptyList()
)
