package com.interviewpreplab.features.lessons

import com.google.gson.Gson
import com.interviewpreplab.features.topics.topicList
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LessonAssetsTest {
    private val dir = File("src/main/assets/lessons")
    private val gson = Gson()

    private fun load(id: String): Lesson = gson.fromJson(File(dir, "$id.json").readText(), Lesson::class.java)

    @Test
    fun everyTopicHasALessonFile() {
        val missing = topicList.filter { !File(dir, "${it.id}.json").exists() }.map { it.id }
        assertTrue(missing.isEmpty(), "Topics without lesson JSON: $missing")
    }

    @Test
    fun noOrphanLessonFiles() {
        val ids = topicList.map { it.id }.toSet()
        val orphans = dir.listFiles().orEmpty().map { it.nameWithoutExtension }.filter { it !in ids }
        assertTrue(orphans.isEmpty(), "Lesson files with no topic: $orphans")
    }

    @Test
    fun lessonsHaveRequiredContent() {
        topicList.forEach { topic ->
            val lesson = load(topic.id)
            assertTrue(!lesson.summary.isNullOrBlank(), "${topic.id}: summary missing")
            assertTrue(lesson.sections.orEmpty().size >= 2, "${topic.id}: needs at least 2 sections")
            assertTrue(lesson.subtopics.orEmpty().size >= 2, "${topic.id}: needs at least 2 subtopics")
            assertTrue(lesson.takeaways.orEmpty().isNotEmpty(), "${topic.id}: takeaways missing")
            assertTrue(lesson.references.orEmpty().isNotEmpty(), "${topic.id}: references missing")
            assertTrue(lesson.practice.orEmpty().size >= 3, "${topic.id}: needs at least 3 practice problems")
            assertTrue(lesson.practice.orEmpty().all { it.difficulty in setOf("Easy", "Medium", "Hard") }, "${topic.id}: bad difficulty")
        }
    }

    // Same fixed heading order for every lesson (docs/NOTES_STYLE.md); a heading may be omitted, never renamed.
    private val headingOrder = listOf(
        "Overview", "Core Concepts", "How It Works", "Variants & Types",
        "Complexity & Costs", "Common Pitfalls & Gotchas", "Real-World Use", "Interview Notes"
    )

    private fun words(l: Lesson): Int {
        fun sec(s: LessonSection) = listOfNotNull(s.heading, s.body, s.code).plus(s.bullets.orEmpty())
        val parts = listOfNotNull(l.summary) + l.sections.orEmpty().flatMap(::sec) +
            l.subtopics.orEmpty().flatMap { listOfNotNull(it.title, it.summary) + it.sections.orEmpty().flatMap(::sec) } +
            l.takeaways.orEmpty()
        return parts.sumOf { it.split(Regex("\\s+")).size }
    }

    @Test
    fun notesFollowTheSingleFormat() {
        topicList.forEach { topic ->
            val lesson = load(topic.id)
            val headings = lesson.sections.orEmpty().map { it.heading }
            assertTrue(headings.all { it in headingOrder }, "${topic.id}: unknown section heading in $headings")
            val idx = headings.map { headingOrder.indexOf(it) }
            assertEquals(idx.sorted(), idx, "${topic.id}: section headings out of order: $headings")
            assertEquals(headings.size, headings.toSet().size, "${topic.id}: duplicate section heading")
            assertTrue("Overview" in headings && "How It Works" in headings, "${topic.id}: Overview and How It Works are required")
            assertTrue(lesson.subtopics.orEmpty().size in 6..12, "${topic.id}: needs 6-12 subtopics")
            assertTrue(lesson.takeaways.orEmpty().size in 5..8, "${topic.id}: needs 5-8 takeaways")
            assertTrue(lesson.practice.orEmpty().size in 6..10, "${topic.id}: needs 6-10 practice items")
            assertTrue(lesson.references.orEmpty().size >= 2, "${topic.id}: needs 2+ references")
            val w = words(lesson)
            assertTrue(w in 1500..4500, "${topic.id}: ${w} words, expected 1500-4500 (split into child lessons past that)")
        }
    }

    @Test
    fun childLessonLinksResolve() {
        val ids = topicList.map { it.id }.toSet()
        topicList.forEach { topic ->
            topic.parentId?.let { assertTrue(it in ids && it != topic.id, "${topic.id}: bad parentId $it") }
            load(topic.id).subtopics.orEmpty().mapNotNull { it.topicId }.forEach { child ->
                val c = topicList.find { it.id == child }
                assertTrue(c != null && c.parentId == topic.id, "${topic.id}: subtopic links to $child which is not its child")
            }
        }
    }

    @Test
    fun subtopicTitlesAreUniquePerLesson() {
        topicList.forEach { topic ->
            val titles = load(topic.id).subtopics.orEmpty().map { it.title }
            assertEquals(titles.size, titles.toSet().size, "${topic.id}: duplicate subtopic titles")
        }
    }
}
