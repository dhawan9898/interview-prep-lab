package com.interviewpreplab.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topic_progress")
data class TopicProgressEntity(
    @PrimaryKey
    val topicId: String,
    val topicName: String,
    val category: String,           // "Sorting", "Data Structures", etc.
    val lastViewed: Long = 0L,      // timestamp in ms
    val timeSpent: Long = 0L,       // milliseconds
    val isCompleted: Boolean = false,
    val isFavorited: Boolean = false,
    val dateAdded: Long = System.currentTimeMillis()
)
