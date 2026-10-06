package com.interviewpreplab.features.lessons

import android.content.Context
import com.google.gson.Gson

// Gson bypasses constructors, so every collection is nullable and read through orEmpty().
data class Lesson(
    val summary: String? = null,
    val sections: List<LessonSection>? = null,
    val subtopics: List<Subtopic>? = null,
    val takeaways: List<String>? = null,
    val references: List<Reference>? = null,
    val practice: List<PracticeItem>? = null
)

data class LessonSection(
    val heading: String? = null,
    val body: String? = null,
    val bullets: List<String>? = null,
    val code: String? = null
)

data class Subtopic(
    val title: String? = null,
    val summary: String? = null,
    val sections: List<LessonSection>? = null,
    // Id of a full child lesson (a Topic whose parentId is this lesson) that goes deeper than this card.
    val topicId: String? = null
)

data class Reference(val label: String? = null, val url: String? = null)

data class PracticeItem(val title: String? = null, val difficulty: String? = null)

object LessonLoader {
    private val gson = Gson()

    fun load(context: Context, topicId: String): Lesson? = try {
        context.assets.open("lessons/$topicId.json").bufferedReader().use {
            gson.fromJson(it, Lesson::class.java)
        }
    } catch (e: java.io.FileNotFoundException) {
        null
    } catch (e: com.google.gson.JsonParseException) {
        null
    }
}
