package com.interviewpreplab.features.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.interviewpreplab.features.progress.ProgressScreen

@Composable
fun ProfileScreen(contentPadding: PaddingValues, onTopicClick: (String) -> Unit) {
    ProgressScreen(onTopicClick = onTopicClick, modifier = Modifier.padding(bottom = contentPadding.calculateBottomPadding()))
}
