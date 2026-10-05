package com.interviewpreplab.features.quiz

import com.interviewpreplab.core.database.entities.QuizAttemptEntity
import com.interviewpreplab.core.database.entities.QuizEntity
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class QuizEngineTest {

    @Test
    fun `validate correct answer`() {
        val question = QuizQuestion(
            quizId = "q1",
            topicId = "topic1",
            question = "What is 2+2?",
            options = listOf("3", "4", "5"),
            correctAnswer = "4",
            difficulty = "easy",
            explanation = "Simple addition",
            quizType = "MCQ"
        )

        assertTrue(QuizEngine.validateAnswer(question, "4"))
    }

    @Test
    fun `validate incorrect answer`() {
        val question = QuizQuestion(
            quizId = "q1",
            topicId = "topic1",
            question = "What is 2+2?",
            options = listOf("3", "4", "5"),
            correctAnswer = "4",
            difficulty = "easy",
            explanation = "Simple addition",
            quizType = "MCQ"
        )

        assertTrue(!QuizEngine.validateAnswer(question, "3"))
    }

    @Test
    fun `answer validation is case insensitive`() {
        val question = QuizQuestion(
            quizId = "q1",
            topicId = "topic1",
            question = "Capital of France?",
            options = listOf("London", "Paris", "Berlin"),
            correctAnswer = "Paris",
            difficulty = "easy",
            explanation = "France's capital",
            quizType = "MCQ"
        )

        assertTrue(QuizEngine.validateAnswer(question, "paris"))
        assertTrue(QuizEngine.validateAnswer(question, "PARIS"))
        assertTrue(QuizEngine.validateAnswer(question, "PaRiS"))
    }

    @Test
    fun `calculate accuracy with perfect score`() {
        val attempts = listOf(
            QuizAttemptEntity("a1", "q1", "correct", true, 1000, System.currentTimeMillis()),
            QuizAttemptEntity("a2", "q2", "correct", true, 1000, System.currentTimeMillis()),
            QuizAttemptEntity("a3", "q3", "correct", true, 1000, System.currentTimeMillis())
        )

        assertEquals(100.0, QuizEngine.calculateAccuracy(attempts))
    }

    @Test
    fun `calculate accuracy with partial score`() {
        val attempts = listOf(
            QuizAttemptEntity("a1", "q1", "correct", true, 1000, System.currentTimeMillis()),
            QuizAttemptEntity("a2", "q2", "wrong", false, 1000, System.currentTimeMillis()),
            QuizAttemptEntity("a3", "q3", "correct", true, 1000, System.currentTimeMillis())
        )

        assertEquals(66.66666666666666, QuizEngine.calculateAccuracy(attempts), 0.1)
    }

    @Test
    fun `calculate accuracy with zero score`() {
        val attempts = listOf(
            QuizAttemptEntity("a1", "q1", "wrong", false, 1000, System.currentTimeMillis()),
            QuizAttemptEntity("a2", "q2", "wrong", false, 1000, System.currentTimeMillis())
        )

        assertEquals(0.0, QuizEngine.calculateAccuracy(attempts))
    }

    @Test
    fun `calculate accuracy with empty attempts`() {
        assertEquals(0.0, QuizEngine.calculateAccuracy(emptyList()))
    }

    @Test
    fun `calculate average time per question`() {
        val attempts = listOf(
            QuizAttemptEntity("a1", "q1", "correct", true, 1000, System.currentTimeMillis()),
            QuizAttemptEntity("a2", "q2", "correct", true, 2000, System.currentTimeMillis()),
            QuizAttemptEntity("a3", "q3", "correct", true, 3000, System.currentTimeMillis())
        )

        assertEquals(2000L, QuizEngine.calculateAverageTime(attempts))
    }

    @Test
    fun `get difficulty color for easy`() {
        assertEquals("#4CAF50", QuizEngine.getDifficultyColor("easy"))
        assertEquals("#4CAF50", QuizEngine.getDifficultyColor("EASY"))
    }

    @Test
    fun `get difficulty color for medium`() {
        assertEquals("#FFC107", QuizEngine.getDifficultyColor("medium"))
        assertEquals("#FFC107", QuizEngine.getDifficultyColor("MEDIUM"))
    }

    @Test
    fun `get difficulty color for hard`() {
        assertEquals("#F44336", QuizEngine.getDifficultyColor("hard"))
        assertEquals("#F44336", QuizEngine.getDifficultyColor("HARD"))
    }

    @Test
    fun `get difficulty level for easy`() {
        assertEquals(1, QuizEngine.getDifficultyLevel("easy"))
    }

    @Test
    fun `get difficulty level for medium`() {
        assertEquals(2, QuizEngine.getDifficultyLevel("medium"))
    }

    @Test
    fun `get difficulty level for hard`() {
        assertEquals(3, QuizEngine.getDifficultyLevel("hard"))
    }

    @Test
    fun `get quiz type label for MCQ`() {
        assertEquals("Multiple Choice", QuizEngine.getQuizTypeLabel("MCQ"))
    }

    @Test
    fun `get quiz type label for PREDICT_FRAME`() {
        assertEquals("Predict the Frame", QuizEngine.getQuizTypeLabel("PREDICT_FRAME"))
    }

    @Test
    fun `get quiz type label for CODE_COMPLETION`() {
        assertEquals("Code Completion", QuizEngine.getQuizTypeLabel("CODE_COMPLETION"))
    }

    @Test
    fun `filter quizzes by difficulty`() {
        val quizzes = listOf(
            QuizEntity("q1", "topic1", "Q1?", "[]", "a", "easy", "exp"),
            QuizEntity("q2", "topic1", "Q2?", "[]", "a", "medium", "exp"),
            QuizEntity("q3", "topic1", "Q3?", "[]", "a", "hard", "exp"),
            QuizEntity("q4", "topic1", "Q4?", "[]", "a", "easy", "exp")
        )

        val easyQuizzes = QuizEngine.getQuizzesByDifficulty(quizzes, "easy")
        assertEquals(2, easyQuizzes.size)
        assertTrue(easyQuizzes.all { it.difficulty == "easy" })
    }

    @Test
    fun `get all quizzes when no difficulty filter`() {
        val quizzes = listOf(
            QuizEntity("q1", "topic1", "Q1?", "[]", "a", "easy", "exp"),
            QuizEntity("q2", "topic1", "Q2?", "[]", "a", "medium", "exp"),
            QuizEntity("q3", "topic1", "Q3?", "[]", "a", "hard", "exp")
        )

        val filtered = QuizEngine.getQuizzesByDifficulty(quizzes, null)
        assertEquals(3, filtered.size)
    }

    @Test
    fun `get random quizzes respects count limit`() {
        val quizzes = (1..10).map {
            QuizEntity("q$it", "topic1", "Q$it?", "[]", "a", "easy", "exp")
        }

        val random = QuizEngine.getRandomQuizzes(quizzes, 3)
        assertEquals(3, random.size)
    }

    @Test
    fun `get random quizzes handles count larger than list size`() {
        val quizzes = (1..3).map {
            QuizEntity("q$it", "topic1", "Q$it?", "[]", "a", "easy", "exp")
        }

        val random = QuizEngine.getRandomQuizzes(quizzes, 10)
        assertEquals(3, random.size)
    }
}
