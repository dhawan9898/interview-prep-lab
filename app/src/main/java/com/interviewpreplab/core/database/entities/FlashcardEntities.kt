package com.interviewpreplab.core.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = TopicProgressEntity::class,
            parentColumns = ["topicId"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FlashcardEntity(
    @PrimaryKey
    val cardId: String,
    val topicId: String,
    val front: String,                      // Question
    val back: String,                       // Answer
    val difficulty: Int = 1,                // 1-5
    val nextReview: Long = System.currentTimeMillis(), // When to show next
    val easeFactor: Double = 2.5,           // SM-2 algorithm (default = 2.5)
    val interval: Int = 0,                  // Days until next review
    val repetitions: Int = 0,               // Number of times reviewed
    val lastReviewed: Long = 0L,            // Last review timestamp
    val dateCreated: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "flashcard_reviews",
    foreignKeys = [
        ForeignKey(
            entity = FlashcardEntity::class,
            parentColumns = ["cardId"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FlashcardReviewEntity(
    @PrimaryKey
    val reviewId: String,
    val cardId: String,
    val quality: Int,                       // 0-5 (SM-2 quality rating)
    val easeFactor: Double,                 // New ease factor after review
    val interval: Int,                      // New interval days
    val nextReview: Long,                   // Next review timestamp
    val timestamp: Long = System.currentTimeMillis()
)

data class DailyStreakEntity(
    val date: String,                       // YYYY-MM-DD format
    val topicsStudied: Int = 0,
    val totalMinutes: Long = 0L,
    val cardsReviewed: Int = 0,
    val quizzesTaken: Int = 0
)
