package com.interviewpreplab.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.interviewpreplab.core.database.dao.TopicProgressDao
import com.interviewpreplab.core.database.dao.QuizDao
import com.interviewpreplab.core.database.dao.FlashcardDao
import com.interviewpreplab.core.database.entities.TopicProgressEntity
import com.interviewpreplab.core.database.entities.QuizEntity
import com.interviewpreplab.core.database.entities.QuizAttemptEntity
import com.interviewpreplab.core.database.entities.FlashcardEntity
import com.interviewpreplab.core.database.entities.FlashcardReviewEntity

@Database(
    entities = [
        TopicProgressEntity::class,
        QuizEntity::class,
        QuizAttemptEntity::class,
        FlashcardEntity::class,
        FlashcardReviewEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun topicProgressDao(): TopicProgressDao
    abstract fun quizDao(): QuizDao
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "interview_prep_lab_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
