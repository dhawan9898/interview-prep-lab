package com.interviewpreplab.core.database.repository

import com.interviewpreplab.core.database.dao.TopicProgressDao
import com.interviewpreplab.core.database.entities.TopicProgressEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository for topic progress data.
 * Abstracts database access for the rest of the app.
 */
class ProgressRepository @Inject constructor(
    private val topicProgressDao: TopicProgressDao
) {

    // Create/Update
    suspend fun saveProgress(progress: TopicProgressEntity) {
        topicProgressDao.insertOrUpdate(progress)
    }

    suspend fun updateViewTime(topicId: String, duration: Long) {
        val timestamp = System.currentTimeMillis()
        topicProgressDao.updateViewTime(topicId, timestamp, duration)
    }

    suspend fun toggleFavorite(topicId: String, isFavorited: Boolean) {
        topicProgressDao.toggleFavorite(topicId, isFavorited)
    }

    suspend fun markCompleted(topicId: String) {
        topicProgressDao.markCompleted(topicId)
    }

    // Read
    suspend fun getProgress(topicId: String): TopicProgressEntity? {
        return topicProgressDao.getProgress(topicId)
    }

    fun getProgressFlow(topicId: String): Flow<TopicProgressEntity?> {
        return topicProgressDao.getProgressFlow(topicId)
    }

    fun getAllProgress(): Flow<List<TopicProgressEntity>> {
        return topicProgressDao.getAllProgress()
    }

    fun getProgressByCategory(category: String): Flow<List<TopicProgressEntity>> {
        return topicProgressDao.getProgressByCategory(category)
    }

    fun getFavorites(): Flow<List<TopicProgressEntity>> {
        return topicProgressDao.getFavorites()
    }

    // Statistics
    fun getCompletedCount(): Flow<Int> {
        return topicProgressDao.getCompletedCount()
    }

    fun getTotalTimeSpent(): Flow<Long?> {
        return topicProgressDao.getTotalTimeSpent()
    }

    fun getMostStudiedTopics(limit: Int = 10): Flow<List<TopicProgressEntity>> {
        return topicProgressDao.getMostStudiedTopics(limit)
    }

    // Delete
    suspend fun deleteProgress(progress: TopicProgressEntity) {
        topicProgressDao.delete(progress)
    }

    suspend fun deleteAll() {
        topicProgressDao.deleteAll()
    }
}
