package com.interviewpreplab.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.interviewpreplab.core.database.entities.TopicProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: TopicProgressEntity)

    @Update
    suspend fun update(progress: TopicProgressEntity)

    @Delete
    suspend fun delete(progress: TopicProgressEntity)

    @Query("SELECT * FROM topic_progress WHERE topicId = :topicId")
    suspend fun getProgress(topicId: String): TopicProgressEntity?

    @Query("SELECT * FROM topic_progress WHERE topicId = :topicId")
    fun getProgressFlow(topicId: String): Flow<TopicProgressEntity?>

    @Query("SELECT * FROM topic_progress ORDER BY lastViewed DESC")
    fun getAllProgress(): Flow<List<TopicProgressEntity>>

    @Query("SELECT * FROM topic_progress WHERE category = :category ORDER BY lastViewed DESC")
    fun getProgressByCategory(category: String): Flow<List<TopicProgressEntity>>

    @Query("SELECT * FROM topic_progress WHERE isFavorited = 1 ORDER BY lastViewed DESC")
    fun getFavorites(): Flow<List<TopicProgressEntity>>

    @Query("UPDATE topic_progress SET lastViewed = :timestamp, timeSpent = timeSpent + :duration WHERE topicId = :topicId")
    suspend fun updateViewTime(topicId: String, timestamp: Long, duration: Long)

    @Query("UPDATE topic_progress SET isFavorited = :isFavorited WHERE topicId = :topicId")
    suspend fun toggleFavorite(topicId: String, isFavorited: Boolean)

    @Query("UPDATE topic_progress SET isCompleted = 1 WHERE topicId = :topicId")
    suspend fun markCompleted(topicId: String)

    @Query("SELECT COUNT(*) FROM topic_progress WHERE isCompleted = 1")
    fun getCompletedCount(): Flow<Int>

    @Query("SELECT SUM(timeSpent) FROM topic_progress")
    fun getTotalTimeSpent(): Flow<Long?>

    @Query("SELECT * FROM topic_progress ORDER BY timeSpent DESC LIMIT :limit")
    fun getMostStudiedTopics(limit: Int = 10): Flow<List<TopicProgressEntity>>

    @Query("DELETE FROM topic_progress")
    suspend fun deleteAll()
}
