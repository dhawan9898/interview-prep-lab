package com.interviewpreplab.core.di

import android.content.Context
import com.interviewpreplab.core.database.AppDatabase
import com.interviewpreplab.core.database.dao.FlashcardDao
import com.interviewpreplab.core.database.dao.QuizDao
import com.interviewpreplab.core.database.dao.TopicProgressDao
import com.interviewpreplab.core.database.repository.ProgressRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideTopicProgressDao(database: AppDatabase): TopicProgressDao {
        return database.topicProgressDao()
    }

    @Provides
    @Singleton
    fun provideQuizDao(database: AppDatabase): QuizDao {
        return database.quizDao()
    }

    @Provides
    @Singleton
    fun provideFlashcardDao(database: AppDatabase): FlashcardDao {
        return database.flashcardDao()
    }

    @Provides
    @Singleton
    fun provideProgressRepository(dao: TopicProgressDao): ProgressRepository {
        return ProgressRepository(dao)
    }
}
