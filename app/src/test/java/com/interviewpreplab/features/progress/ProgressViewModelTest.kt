package com.interviewpreplab.features.progress

import com.interviewpreplab.core.database.dao.TopicProgressDao
import com.interviewpreplab.core.database.entities.TopicProgressEntity
import com.interviewpreplab.core.database.repository.ProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** In-memory TopicProgressDao backed by StateFlows, so the real ProgressRepository can be used. */
class FakeTopicProgressDao : TopicProgressDao {
    val progress = MutableStateFlow<List<TopicProgressEntity>>(emptyList())
    val totalTime = MutableStateFlow<Long?>(null)
    val mostStudied = MutableStateFlow<List<TopicProgressEntity>>(emptyList())

    override suspend fun insertOrUpdate(progress: TopicProgressEntity) {
        this.progress.value = this.progress.value.filter { it.topicId != progress.topicId } + progress
    }

    override suspend fun update(progress: TopicProgressEntity) = insertOrUpdate(progress)

    override suspend fun delete(progress: TopicProgressEntity) {
        this.progress.value = this.progress.value.filter { it.topicId != progress.topicId }
    }

    override suspend fun getProgress(topicId: String) = progress.value.find { it.topicId == topicId }

    override fun getProgressFlow(topicId: String): Flow<TopicProgressEntity?> =
        progress.map { list -> list.find { it.topicId == topicId } }

    override fun getAllProgress(): Flow<List<TopicProgressEntity>> = progress

    override fun getProgressByCategory(category: String): Flow<List<TopicProgressEntity>> =
        progress.map { list -> list.filter { it.category == category } }

    override fun getFavorites(): Flow<List<TopicProgressEntity>> =
        progress.map { list -> list.filter { it.isFavorited } }

    override suspend fun updateViewTime(topicId: String, timestamp: Long, duration: Long) {}

    override suspend fun toggleFavorite(topicId: String, isFavorited: Boolean) {}

    override suspend fun markCompleted(topicId: String) {}

    override fun getCompletedCount(): Flow<Int> = progress.map { list -> list.count { it.isCompleted } }

    override fun getTotalTimeSpent(): Flow<Long?> = totalTime

    override fun getMostStudiedTopics(limit: Int): Flow<List<TopicProgressEntity>> = mostStudied

    override suspend fun deleteAll() {
        progress.value = emptyList()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ProgressViewModelTest {

    private lateinit var fakeDao: FakeTopicProgressDao
    private lateinit var repository: ProgressRepository
    private lateinit var viewModel: ProgressViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeDao = FakeTopicProgressDao()
        repository = ProgressRepository(fakeDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun topic(
        id: String,
        name: String,
        category: String,
        timeSpent: Long = 0,
        isCompleted: Boolean = false,
        isFavorited: Boolean = false
    ) = TopicProgressEntity(
        topicId = id,
        topicName = name,
        category = category,
        lastViewed = 0,
        timeSpent = timeSpent,
        isCompleted = isCompleted,
        isFavorited = isFavorited
    )

    @Test
    fun `time formatting works correctly`() = runTest {
        viewModel = ProgressViewModel(repository)

        assertEquals("30s", viewModel.getFormattedTime(30_000))
        assertEquals("5m", viewModel.getFormattedTime(300_000))
        assertEquals("1h 30m", viewModel.getFormattedTime(5_400_000))
    }

    @Test
    fun `progress percentage is 100% for completed topic`() = runTest {
        fakeDao.progress.value = listOf(
            topic("bubble-sort", "Bubble Sort", "Sorting", timeSpent = 3_600_000, isCompleted = true)
        )
        viewModel = ProgressViewModel(repository)

        assertEquals(1.0f, viewModel.getProgressPercentage("bubble-sort"))
    }

    @Test
    fun `progress percentage is 50% for incomplete topic`() = runTest {
        fakeDao.progress.value = listOf(
            topic("bubble-sort", "Bubble Sort", "Sorting", timeSpent = 1_800_000)
        )
        viewModel = ProgressViewModel(repository)

        assertEquals(0.5f, viewModel.getProgressPercentage("bubble-sort"))
    }

    @Test
    fun `get categories returns unique sorted categories`() = runTest {
        fakeDao.progress.value = listOf(
            topic("bubble-sort", "Bubble Sort", "Sorting"),
            topic("merge-sort", "Merge Sort", "Sorting"),
            topic("binary-search", "Binary Search", "Searching"),
            topic("stack", "Stack", "Data Structures")
        )
        viewModel = ProgressViewModel(repository)

        val categories = viewModel.getCategories()
        assertEquals(3, categories.size)
        assertTrue(categories.contains("Data Structures"))
        assertTrue(categories.contains("Searching"))
        assertTrue(categories.contains("Sorting"))
        assertEquals(listOf("Data Structures", "Searching", "Sorting"), categories)
    }

    @Test
    fun `filtered progress returns only selected category`() = runTest {
        fakeDao.progress.value = listOf(
            topic("bubble-sort", "Bubble Sort", "Sorting"),
            topic("binary-search", "Binary Search", "Searching")
        )
        viewModel = ProgressViewModel(repository)

        viewModel.setSelectedCategory("Sorting")
        val filtered = viewModel.getFilteredProgress()

        assertEquals(1, filtered.size)
        assertEquals("Bubble Sort", filtered.first().topicName)
    }

    @Test
    fun `filtered progress returns all when no category selected`() = runTest {
        fakeDao.progress.value = listOf(
            topic("bubble-sort", "Bubble Sort", "Sorting"),
            topic("binary-search", "Binary Search", "Searching")
        )
        viewModel = ProgressViewModel(repository)

        viewModel.setSelectedCategory(null)

        assertEquals(2, viewModel.getFilteredProgress().size)
    }

    @Test
    fun `statistics are correctly calculated`() = runTest {
        val topics = listOf(
            topic("bubble-sort", "Bubble Sort", "Sorting", timeSpent = 3_600_000, isCompleted = true, isFavorited = true),
            topic("merge-sort", "Merge Sort", "Sorting", timeSpent = 1_800_000)
        )
        fakeDao.progress.value = topics
        fakeDao.totalTime.value = 5_400_000
        fakeDao.mostStudied.value = listOf(topics.first())

        viewModel = ProgressViewModel(repository)
        val state = viewModel.uiState.value

        assertEquals(2, state.allProgress.size)
        assertEquals(1, state.completedCount)
        assertEquals(5_400_000, state.totalTimeSpent)
        assertEquals(1, state.favorites.size)
        assertEquals(1, state.mostStudiedTopics.size)
    }

    @Test
    fun `setting category updates selected category state`() = runTest {
        viewModel = ProgressViewModel(repository)

        viewModel.setSelectedCategory("Sorting")
        assertEquals("Sorting", viewModel.selectedCategory.value)

        viewModel.setSelectedCategory(null)
        assertEquals(null, viewModel.selectedCategory.value)
    }
}
