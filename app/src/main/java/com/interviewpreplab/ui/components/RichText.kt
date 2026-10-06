package com.interviewpreplab.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

private val inlineMarkup = Regex("`([^`]+)`|\\*\\*([^*]+)\\*\\*")

/** Lesson text uses `code` and **bold** inline; everything else is plain text. */
fun richText(text: String, codeBackground: Color, codeColor: Color): AnnotatedString = buildAnnotatedString {
    var last = 0
    for (m in inlineMarkup.findAll(text)) {
        append(text.substring(last, m.range.first))
        m.groups[1]?.let {
            withStyle(SpanStyle(fontFamily = FontFamily.Monospace, background = codeBackground, color = codeColor)) { append(it.value) }
        }
        m.groups[2]?.let { withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(it.value) } }
        last = m.range.last + 1
    }
    append(text.substring(last))
}

@Composable
fun RichText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    val bg = MaterialTheme.colorScheme.surfaceVariant
    val codeColor = MaterialTheme.colorScheme.primary
    val annotated = remember(text, bg, codeColor) { richText(text, bg, codeColor) }
    Text(text = annotated, modifier = modifier, style = style, color = color)
}
