package com.interviewpreplab.features.quiz

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QuizScreen(
    topicId: String,
    questions: List<QuizQuestion>,
    viewModel: QuizViewModel = hiltViewModel(),
    onComplete: (stats: QuizStats) -> Unit = {},
    onBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    // Initialize quiz on first composition
    androidx.compose.runtime.LaunchedEffect(topicId, questions) {
        if (questions.isNotEmpty() && uiState.totalQuestions == 0) {
            viewModel.startQuiz(questions)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Quiz") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, "Back")
                }
            }
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.isComplete && uiState.stats != null) {
            QuizResultScreen(
                stats = uiState.stats!!,
                onRetry = { viewModel.retryQuiz() },
                onBack = onBack
            )
            onComplete(uiState.stats!!)
        } else if (uiState.currentQuestion != null) {
            QuizQuestionScreen(
                question = uiState.currentQuestion!!,
                questionIndex = uiState.questionIndex,
                totalQuestions = uiState.totalQuestions,
                selectedAnswer = uiState.selectedAnswer,
                isAnswered = uiState.isAnswered,
                isCorrect = uiState.isCorrect,
                explanation = uiState.currentQuestion!!.explanation,
                onSelectAnswer = { viewModel.selectAnswer(it) },
                onSubmit = { viewModel.submitAnswer(topicId) },
                onNext = { viewModel.nextQuestion() },
                onPrevious = { viewModel.previousQuestion() },
                onSkip = { viewModel.skipQuestion() },
                canGoBack = uiState.questionIndex > 0
            )
        }

        uiState.error?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(16.dp)
            ) {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun QuizQuestionScreen(
    question: QuizQuestion,
    questionIndex: Int,
    totalQuestions: Int,
    selectedAnswer: String?,
    isAnswered: Boolean,
    isCorrect: Boolean?,
    explanation: String,
    onSelectAnswer: (String) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSkip: () -> Unit,
    canGoBack: Boolean
) {
    val progress = (questionIndex.toFloat() + 1) / totalQuestions

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
        )

        // Question counter
        Text(
            text = "Question ${questionIndex + 1} of $totalQuestions",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Difficulty badge
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = when (question.difficulty.lowercase()) {
                        "easy" -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                        "medium" -> Color(0xFFFFC107).copy(alpha = 0.1f)
                        "hard" -> Color(0xFFF44336).copy(alpha = 0.1f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(
                text = question.difficulty.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = when (question.difficulty.lowercase()) {
                    "easy" -> Color(0xFF4CAF50)
                    "medium" -> Color(0xFFFFC107)
                    "hard" -> Color(0xFFF44336)
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = QuizEngine.getQuizTypeLabel(question.quizType),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Question text
        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Answer options
        if (!isAnswered) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                question.options.forEach { option ->
                    AnswerOption(
                        text = option,
                        isSelected = selectedAnswer == option,
                        onClick = { onSelectAnswer(option) }
                    )
                }
            }
        } else {
            // Show results
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                question.options.forEach { option ->
                    ResultOption(
                        text = option,
                        isCorrect = option == question.correctAnswer,
                        isSelected = option == selectedAnswer,
                        isAnswered = true
                    )
                }
            }

            // Explanation
            if (explanation.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Explanation",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = explanation,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action buttons
        if (!isAnswered) {
            Button(
                onClick = onSubmit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = selectedAnswer != null
            ) {
                Text("Submit Answer")
            }

            OutlinedButton(
                onClick = onSkip,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Skip Question")
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (canGoBack) {
                    OutlinedButton(
                        onClick = onPrevious,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text("Previous")
                    }
                }

                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text("Next")
                    Spacer(modifier = Modifier.padding(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }
}

@Composable
private fun AnswerOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = { onClick() }
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ResultOption(
    text: String,
    isCorrect: Boolean,
    isSelected: Boolean,
    isAnswered: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = when {
                    isCorrect && isAnswered -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                    isSelected && !isCorrect -> Color(0xFFF44336).copy(alpha = 0.1f)
                    else -> MaterialTheme.colorScheme.surface
                },
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected && isAnswered,
                onClick = {}
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            if (isCorrect) {
                Text(
                    text = "✓",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun QuizResultScreen(
    stats: QuizStats,
    onRetry: () -> Unit,
    onBack: () -> Unit
) {
    val accuracy = String.format("%.1f", stats.accuracy)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Score circle
        Box(
            modifier = Modifier
                .background(
                    color = when {
                        stats.accuracy >= 80 -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                        stats.accuracy >= 60 -> Color(0xFFFFC107).copy(alpha = 0.1f)
                        else -> Color(0xFFF44336).copy(alpha = 0.1f)
                    },
                    shape = RoundedCornerShape(200.dp)
                )
                .padding(48.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$accuracy%",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        stats.accuracy >= 80 -> Color(0xFF4CAF50)
                        stats.accuracy >= 60 -> Color(0xFFFFC107)
                        else -> Color(0xFFF44336)
                    }
                )

                Text(
                    text = "Accuracy",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Stats cards
        StatRow(
            label = "Correct Answers",
            value = "${stats.correctAnswers}/${stats.totalQuizzes}"
        )

        StatRow(
            label = "Questions Answered",
            value = stats.totalQuizzes.toString()
        )

        // Difficulty breakdown
        if (stats.byDifficulty.isNotEmpty()) {
            Text(
                text = "Performance by Difficulty",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            stats.byDifficulty.forEach { (difficulty, diffStats) ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = difficulty.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${diffStats.correct}/${diffStats.total} correct",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "${String.format("%.1f", diffStats.accuracy)}%",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Action buttons
        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Retake Quiz")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Back to Topic")
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
