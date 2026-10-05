package com.interviewpreplab.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.interviewpreplab.core.database.entities.FlashcardEntity
import com.interviewpreplab.core.database.entities.FlashcardReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: FlashcardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: FlashcardReviewEntity)

    @Update
    suspend fun updateCard(card: FlashcardEntity)

    @Delete
    suspend fun deleteCard(card: FlashcardEntity)

    @Query("SELECT * FROM flashcards WHERE cardId = :cardId")
    suspend fun getCard(cardId: String): FlashcardEntity?

    @Query("SELECT * FROM flashcards WHERE topicId = :topicId ORDER BY nextReview ASC")
    fun getCardsByTopic(topicId: String): Flow<List<FlashcardEntity>>

    @Query("SELECT * FROM flashcards WHERE nextReview <= :currentTime ORDER BY nextReview ASC LIMIT :limit")
    fun getTodayReviewQueue(currentTime: Long = System.currentTimeMillis(), limit: Int = 20): Flow<List<FlashcardEntity>>

    @Query("SELECT COUNT(*) FROM flashcards WHERE nextReview <= :currentTime")
    fun getPendingReviewCount(currentTime: Long = System.currentTimeMillis()): Flow<Int>

    @Query("SELECT * FROM flashcard_reviews WHERE cardId = :cardId ORDER BY timestamp DESC LIMIT :limit")
    fun getReviewHistory(cardId: String, limit: Int = 10): Flow<List<FlashcardReviewEntity>>

    @Query("""
        SELECT
            CAST(COUNT(CASE WHEN repetitions > 0 THEN 1 END) AS FLOAT) /
            CAST(COUNT(*) AS FLOAT) * 100
        FROM flashcards
        WHERE topicId = :topicId
    """)
    fun getRetentionRate(topicId: String): Flow<Double?>

    @Query("SELECT AVG(easeFactor) FROM flashcards WHERE topicId = :topicId")
    fun getAverageDifficulty(topicId: String): Flow<Double?>

    @Query("SELECT COUNT(*) FROM flashcards")
    fun getTotalCards(): Flow<Int>

    @Query("SELECT COUNT(*) FROM flashcards WHERE repetitions > 0")
    fun getReviewedCards(): Flow<Int>

    @Query("SELECT COUNT(*) FROM flashcard_reviews WHERE DATE(timestamp / 1000, 'unixepoch') = DATE('now')")
    fun getTodayReviewCount(): Flow<Int>

    @Query("SELECT COUNT(DISTINCT DATE(timestamp / 1000, 'unixepoch')) FROM flashcard_reviews")
    fun getStudyDayCount(): Flow<Int>

    @Query("UPDATE flashcards SET nextReview = :nextReview, easeFactor = :easeFactor, interval = :interval, repetitions = repetitions + 1, lastReviewed = :timestamp WHERE cardId = :cardId")
    suspend fun updateAfterReview(
        cardId: String,
        nextReview: Long,
        easeFactor: Double,
        interval: Int,
        timestamp: Long
    )

    @Query("DELETE FROM flashcards")
    suspend fun deleteAllCards()

    @Query("DELETE FROM flashcard_reviews")
    suspend fun deleteAllReviews()
}
