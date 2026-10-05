package com.interviewpreplab.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.interviewpreplab.core.database.entities.QuizEntity
import com.interviewpreplab.core.database.entities.QuizAttemptEntity
import com.interviewpreplab.core.database.entities.QuizWithAttempts
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttemptEntity)

    @Update
    suspend fun updateQuiz(quiz: QuizEntity)

    @Delete
    suspend fun deleteQuiz(quiz: QuizEntity)

    @Query("SELECT * FROM quizzes WHERE quizId = :quizId")
    suspend fun getQuiz(quizId: String): QuizEntity?

    @Transaction
    @Query("SELECT * FROM quizzes WHERE quizId = :quizId")
    suspend fun getQuizWithAttempts(quizId: String): QuizWithAttempts?

    @Query("SELECT * FROM quizzes WHERE topicId = :topicId")
    fun getQuizzesByTopic(topicId: String): Flow<List<QuizEntity>>

    @Query("SELECT * FROM quizzes WHERE difficulty = :difficulty ORDER BY RANDOM() LIMIT :limit")
    fun getQuizzesByDifficulty(difficulty: String, limit: Int = 10): Flow<List<QuizEntity>>

    @Query("SELECT * FROM quiz_attempts WHERE quizId = :quizId ORDER BY timestamp DESC")
    fun getAttempts(quizId: String): Flow<List<QuizAttemptEntity>>

    @Query("SELECT AVG(CASE WHEN isCorrect = 1 THEN 1 ELSE 0 END) * 100 FROM quiz_attempts WHERE quizId = :quizId")
    fun getAccuracyPercentage(quizId: String): Flow<Double?>

    @Query("SELECT COUNT(DISTINCT DATE(timestamp / 1000, 'unixepoch')) FROM quiz_attempts")
    fun getDaysSinceStarted(): Flow<Int>

    @Query("SELECT COUNT(*) FROM quiz_attempts WHERE DATE(timestamp / 1000, 'unixepoch') = DATE('now')")
    fun getTodayAttempts(): Flow<Int>

    @Query("SELECT COUNT(*) FROM quiz_attempts WHERE isCorrect = 1")
    fun getTotalCorrectAnswers(): Flow<Int>

    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentAttempts(limit: Int = 20): Flow<List<QuizAttemptEntity>>

    @Query("DELETE FROM quizzes")
    suspend fun deleteAllQuizzes()

    @Query("DELETE FROM quiz_attempts")
    suspend fun deleteAllAttempts()
}
