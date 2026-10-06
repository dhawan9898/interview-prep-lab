package com.interviewpreplab.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RichTextTest {
    private fun parse(s: String) = richText(s, Color.Gray, Color.Blue)

    @Test
    fun stripsMarkupFromVisibleText() {
        assertEquals("Call malloc then free it.", parse("Call `malloc` then **free** it.").text)
    }

    @Test
    fun codeSpansAreMonospaceAndBoldSpansAreBold() {
        val a = parse("use `ptr` and **always** check")
        assertTrue(a.spanStyles.any { it.item.fontFamily == FontFamily.Monospace })
        assertTrue(a.spanStyles.any { it.item.fontWeight == FontWeight.Bold })
    }

    @Test
    fun plainTextAndUnmatchedBackticksAreUnchanged() {
        assertEquals("no markup here", parse("no markup here").text)
        assertEquals("a ` b", parse("a ` b").text)
    }
}
