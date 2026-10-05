package com.interviewpreplab.features.progress

import com.interviewpreplab.core.database.entities.TopicProgressEntity
import com.interviewpreplab.core.database.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockProgressRepository : ProgressRepository(
    private val mockDao: com.interviewpreplab.core.database.dao.TopicProgressDao
) {
    private val mockProgress = MutableStateFlow<List<TopicProgressEntity>>(emptyList())
    private val mockCompletedCount = MutableStateFlow(0)
    private val mockTotalTime = MutableStateFlow(0L)
    private val mockMostStudied = MutableStateFlow<List<TopicProgressEntity>>(emptyList())

    override fun getAllProgress() = mockProgress

    override fun getCompletedCount() = mockCompletedCount

    override fun getTotalTimeSpent() = mockTotalTime

    override fun getMostStudiedTopics(limit: Int) = mockMostStudied

    fun setMockProgress(progress: List<TopicProgressEntity>) {
        mockProgress.value = progress
        mockCompletedCount.value = progress.count { it.isCompleted }
    }

    fun setMockTotalTime(time: Long) {
        mockTotalTime.value = time
    }

    fun setMockMostStudied(topics: List<TopicProgressEntity>) {
        mockMostStudied.value = topics
    }

    override suspend fun save(topicId: String, topicName: String, category: String) {
        // Mock implementation
    }

    override suspend fun update(progress: TopicProgressEntity) {
        // Mock implementation
    }

    override suspend fun toggleFavorite(topicId: String, isFavorited: Boolean) {
        // Mock implementation
    }

    override suspend fun markCompleted(topicId: String) {
        // Mock implementation
    }

    override suspend fun updateViewTime(topicId: String, durationMs: Long) {
        // Mock implementation
    }
}

class ProgressViewModelTest {

    private lateinit var mockRepository: MockProgressRepository
    private lateinit var viewModel: ProgressViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        mockRepository = MockProgressRepository(
            mockDao = object : com.interviewpreplab.core.database.dao.TopicProgressDao {
                override fun insertOrUpdate(entity: TopicProgressEntity) {}
                override fun update(entity: TopicProgressEntity) {}
                override fun delete(topicId: String) {}
                override fun getProgress(topicId: String) = MutableStateFlow(null)
                override fun getProgressFlow(topicId: String) = MutableStateFlow(null)
                override fun getAllProgress() = MutableStateFlow(emptyList())
                override fun getProgressByCategory(category: String) = MutableStateFlow(emptyList())
                override fun getFavorites() = MutableStateFlow(emptyList())
                override suspend fun updateViewTime(topicId: String, durationMs: Long) {}
                override suspend fun toggleFavorite(topicId: String, isFavorited: Boolean) {}
                override suspend fun markCompleted(topicId: String) {}
                override fun getCompletedCount() = MutableStateFlow(0)
                override fun getTotalTimeSpent() = MutableStateFlow(null)
                override fun getMostStudiedTopics(limit: Int) = MutableStateFlow(emptyList())
                override suspend fun deleteAll() {}
            }
        )
    }

    @Test
    fun `time formatting works correctly`() = runTest {
        viewModel = ProgressViewModel(mockRepository)

        // Test seconds
        assertEquals("30s", viewModel.getFormattedTime(30_000))

        // Test minutes
        assertEquals("5m", viewModel.getFormattedTime(300_000))

        // Test hours and minutes
        assertEquals("1h 30m", viewModel.getFormattedTime(5_400_000))
    }

    @Test
    fun `progress percentage is 100% for completed topic`() = runTest {
        val completedTopic = TopicProgressEntity(
            topicId = "bubble-sort",
            topicName = "Bubble Sort",
            category = "Sorting",
            lastViewed = System.currentTimeMillis(),
            timeSpent = 3600_000,
            isCompleted = true,
            isFavorited = false
        )

        mockRepository.setMockProgress(listOf(completedTopic))
        viewModel = ProgressViewModel(mockRepository)

        assertEquals(1.0f, viewModel.getProgressPercentage("bubble-sort"))
    }

    @Test
    fun `progress percentage is 50% for incomplete topic`() = runTest {
        val incompleteTopic = TopicProgressEntity(
            topicId = "bubble-sort",
            topicName = "Bubble Sort",
            category = "Sorting",
            lastViewed = System.currentTimeMillis(),
            timeSpent = 1800_000,
            isCompleted = false,
            isFavorited = false
        )

        mockRepository.setMockProgress(listOf(incompleteTopic))
        viewModel = ProgressViewModel(mockRepository)

        assertEquals(0.5f, viewModel.getProgressPercentage("bubble-sort"))
    }

    @Test
    fun `get categories returns unique sorted categories`() = runTest {
        val topics = listOf(
            TopicProgressEntity(
                topicId = "bubble-sort",
                topicName = "Bubble Sort",
                category = "Sorting",
                lastViewed = 0,
                timeSpent = 0,
                isCompleted = false,
                isFavorited = false
            ),
            TopicProgressEntity(
                topicId = "merge-sort",
                topicName = "Merge Sort",
                category = "Sorting",
                lastViewed = 0,
                timeSpent = 0,
                isCompleted = false,
                isFavorited = false
            ),
            TopicProgressEntity(
                topicId = "binary-search",
                topicName = "Binary Search",
                category = "Searching",
                lastViewed = 0,
                timeSpent = 0,
                isCompleted = false,
                isFavorited = false
            ),
            TopicProgressEntity(
                topicId = "stack",
                topicName = "Stack",
                category = "Data Structures",
                lastViewed = 0,
                timeSpent = 0,
                isCompleted = false,
                isFavorited = false
            )
        )

        mockRepository.setMockProgress(topics)
        viewModel = ProgressViewModel(mockRepository)

        val categories = viewModel.getCategories()
        assertEquals(3, categories.size)
        assertTrue(categories.contains("Data Structures"))
        assertTrue(categories.contains("Searching"))
        assertTrue(categories.contains("Sorting"))
        // Verify they are sorted
        assertEquals(listOf("Data Structures", "Searching", "Sorting"), categories)
    }

    @Test
    fun `filtered progress returns only selected category`() = runTest {
        val topics = listOf(
            TopicProgressEntity(
                topicId = "bubble-sort",
                topicName = "Bubble Sort",
                category = "Sorting",
                lastViewed = 0,
                timeSpent = 0,
                isCompleted = false,
                isFavorited = false
            ),
            TopicProgressEntity(
                topicId = "binary-search",
                topicName = "Binary Search",
                category = "Searching",
                lastViewed = 0,
                timeSpent = 0,
                isCompleted = false,
                isFavorited = false
            )
        )

        mockRepository.setMockProgress(topics)
        viewModel = ProgressViewModel(mockRepository)

        viewModel.setSelectedCategory("Sorting")
        val filtered = viewModel.getFilteredProgress()

        assertEquals(1, filtered.size)
        assertEquals("Bubble Sort", filtered.first().topicName)
    }

    @Test
    fun `filtered progress returns all when no category selected`() = runTest {
        val topics = listOf(
            TopicProgressEntity(
                topicId = "bubble-sort",
                topicName = "Bubble Sort",
                category = "Sorting",
                lastViewed = 0,
                timeSpent = 0,
                isCompleted = false,
                isFavorited = false
            ),
            TopicProgressEntity(
                topicId = "binary-search",
                topicName = "Binary Search",
                category = "Searching",
                lastViewed = 0,
                timeSpent = 0,
                isCompleted = false,
                isFavorited = false
            )
        )

        mockRepository.setMockProgress(topics)
        viewModel = ProgressViewModel(mockRepository)

        viewModel.setSelectedCategory(null)
        val filtered = viewModel.getFilteredProgress()

        assertEquals(2, filtered.size)
    }

    @Test
    fun `statistics are correctly calculated`() = runTest {
        val topics = listOf(
            TopicProgressEntity(
                topicId = "bubble-sort",
                topicName = "Bubble Sort",
                category = "Sorting",
                lastViewed = System.currentTimeMillis(),
                timeSpent = 3_600_000,
                isCompleted = true,
                isFavorited = true
            ),
            TopicProgressEntity(
                topicId = "merge-sort",
                topicName = "Merge Sort",
                category = "Sorting",
                lastViewed = System.currentTimeMillis(),
                timeSpent = 1_800_000,
                isCompleted = false,
                isFavorited = false
            )
        )

        mockRepository.setMockProgress(topics)
        mockRepository.setMockTotalTime(5_400_000)
        mockRepository.setMockMostStudied(listOf(topics.first()))

        viewModel = ProgressViewModel(mockRepository)

        // Give coroutines time to complete
        runTest {
            kotlinx.coroutines.delay(100)
            val state = viewModel.uiState.value

            assertEquals(2, state.allProgress.size)
            assertEquals(1, state.completedCount)
            assertEquals(5_400_000, state.totalTimeSpent)
            assertEquals(1, state.favorites.size)
            assertEquals(1, state.mostStudiedTopics.size)
        }
    }

    @Test
    fun `setting category updates selected category state`() = runTest {
        viewModel = ProgressViewModel(mockRepository)

        viewModel.setSelectedCategory("Sorting")
        assertEquals("Sorting", viewModel.selectedCategory.value)

        viewModel.setSelectedCategory(null)
        assertEquals(null, viewModel.selectedCategory.value)
    }
}
