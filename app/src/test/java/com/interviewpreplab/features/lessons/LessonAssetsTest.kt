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
