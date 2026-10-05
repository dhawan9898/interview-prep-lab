package com.interviewpreplab.core.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

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
    val question: String,
    val options: String,                // JSON array ["A", "B", "C", "D"]
    val correctAnswer: String,          // Text of the correct option
    val difficulty: String = "easy",    // easy | medium | hard
    val explanation: String = "",
    val quizType: QuizType = QuizType.MCQ,
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
    val userAnswer: String,
    val isCorrect: Boolean,
    val timeSpent: Long = 0L,           // milliseconds
    val timestamp: Long = System.currentTimeMillis()
)

data class QuizWithAttempts(
    @Embedded val quiz: QuizEntity,
    @Relation(parentColumn = "quizId", entityColumn = "quizId")
    val attempts: List<QuizAttemptEntity> = emptyList()
)
