package com.interviewpreplab.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import com.interviewpreplab.features.flashcard.FlashcardScreen
import com.interviewpreplab.features.home.HomeScreen
import com.interviewpreplab.features.labs.LabsScreen
import com.interviewpreplab.features.profile.ProfileScreen
import com.interviewpreplab.features.syllabus.SyllabusScreen
import com.interviewpreplab.features.topics.Topic
import com.interviewpreplab.features.lessons.LessonScreen
import com.interviewpreplab.features.topics.topicList
import com.interviewpreplab.ui.components.FloatingNavBar
import com.interviewpreplab.ui.components.NavDestination

private const val ROUTE_MAIN = "main"
private const val ROUTE_TOPIC = "topic/{id}"
private const val ROUTE_FLASHCARDS = "flashcards/{id}"

private val destinations = listOf(
    NavDestination("Learn", Icons.Default.Home),
    NavDestination("Syllabus", Icons.Default.School),
    NavDestination("Labs", Icons.Default.Code),
    NavDestination("Profile", Icons.Default.Person),
)

@Composable
fun AppNav(nav: NavHostController = rememberNavController()) {
    fun openTopic(topic: Topic) {
        nav.navigate("topic/${topic.id}")
    }

    // Child lessons are pushed so Back returns to their parent. Prev/Next replaces the current lesson so Back returns to the list, not through every lesson.
    fun switchTopic(topic: Topic) {
        nav.navigate("topic/${topic.id}") { popUpTo(ROUTE_TOPIC) { inclusive = true } }
    }

    NavHost(
        navController = nav,
        startDestination = ROUTE_MAIN,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { fadeOut() }
    ) {
        composable(ROUTE_MAIN) {
            MainTabs(
                onTopic = ::openTopic,
                onTopicId = { id -> topicList.find { it.id == id }?.let(::openTopic) },
                onFlashcards = { nav.navigate("flashcards/${it.id}") }
            )
        }
        composable(ROUTE_TOPIC) { entry ->
            val topic = topicList.find { it.id == entry.arguments?.getString("id") }
            if (topic != null) {
                LessonScreen(topic = topic, onBack = { nav.popBackStack() }, onOpenTopic = { t -> if (t.parentId != null) openTopic(t) else switchTopic(t) })
            }
        }
        composable(ROUTE_FLASHCARDS) { entry ->
            FlashcardScreen(
                topicId = entry.arguments?.getString("id").orEmpty(),
                onBack = { nav.popBackStack() }
            )
        }
    }
}

@Composable
private fun MainTabs(
    onTopic: (Topic) -> Unit,
    onTopicId: (String) -> Unit,
    onFlashcards: (Topic) -> Unit
) {
    var tab by rememberSaveable { mutableIntStateOf(0) }
    val barClearance = 96.dp
    val padding = PaddingValues(top = 0.dp, bottom = barClearance)

    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        when (tab) {
            0 -> HomeScreen(padding, onTopic)
            1 -> SyllabusScreen(padding, onTopic)
            2 -> LabsScreen(padding, onFlashcards)
            else -> ProfileScreen(padding, onTopicId)
        }
        FloatingNavBar(
            destinations = destinations,
            selectedIndex = tab,
            onSelect = { tab = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
