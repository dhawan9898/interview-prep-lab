# Phase 2: Prep Layer — Detailed Implementation Plan

**Objective:** Transform from visualization-only to a complete study platform with progress tracking, quizzes, and spaced-repetition flashcards.

**Estimated Time:** 2-3 weeks  
**Status:** 🚀 Starting

---

## 🎯 Phase 2 Overview

### What We're Adding

1. **Progress Tracking** — Track which topics user has studied
   - Last viewed topic
   - Time spent per topic
   - Completion percentage
   
2. **Quizzes** — Test understanding of each topic
   - Multiple choice questions (MCQ)
   - "Predict next frame" mode (visual)
   - Score tracking
   
3. **Spaced-Repetition Flashcards** — Adaptive learning
   - Algorithmically timed reviews
   - SM-2 algorithm (scientifically proven)
   - Retention tracking
   
4. **Bookmarks** — Mark important topics
   - Quick access to favorites
   - Organized by category
   
5. **Dashboard** — Overview of progress
   - Daily streak
   - Topics mastered
   - Study statistics
   - Recommended reviews (SRS)

6. **Search** — Find topics quickly
   - Search by name, category, type
   - Filter by difficulty

---

## 🏗️ Architecture Changes

### Database Schema (Room)

```kotlin
// Models for Room persistence
data class TopicProgress(
    val topicId: String,           // bubble-sort, stack, etc.
    val lastViewed: Long,           // timestamp
    val timeSpent: Long,            // milliseconds
    val completed: Boolean,
    val isFavorited: Boolean
)

data class Quiz(
    val id: String,
    val topicId: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int,
    val type: QuizType,            // MCQ, PREDICT_FRAME, etc.
    val difficulty: Int             // 1-5
)

data class UserQuizAttempt(
    val id: String,
    val quizId: String,
    val userAnswer: Int,
    val isCorrect: Boolean,
    val timestamp: Long,
    val timeSpent: Long
)

data class Flashcard(
    val id: String,
    val topicId: String,
    val front: String,             // Question
    val back: String,              // Answer
    val difficulty: Int,
    val nextReview: Long,           // When to show next
    val easeFactor: Double,         // SM-2 algorithm
    val interval: Int,              // Days until next review
    val repetitions: Int
)

data class DailyStreak(
    val date: String,              // YYYY-MM-DD
    val topicsStudied: Int,
    val totalMinutes: Long
)
```

### New UI Screens

1. **Home/Dashboard** (replace/enhance current)
   - Daily streak badge
   - Today's review count
   - Recent topics
   - Quick stats

2. **Progress Tab**
   - List of all topics with progress bars
   - Filter by category
   - Sort by progress/date

3. **Quiz Tab**
   - Browse all quizzes
   - Practice mode
   - Score history
   - Difficulty filter

4. **Flashcard Tab**
   - Daily reviews (SRS queue)
   - All flashcards browser
   - Statistics (retention rate)

5. **Settings**
   - Theme (already has, extend)
   - Study reminders
   - Daily goal
   - Data export

---

## 📁 Implementation Structure

### New Packages

```
app/src/main/java/com/interviewpreplab/
├── core/
│   ├── database/
│   │   ├── AppDatabase.kt          ← Room setup
│   │   ├── entities/               ← Data classes
│   │   ├── dao/                    ← Database access
│   │   └── repository/             ← Repository pattern
│   ├── model/                       ← (existing, expand)
│   └── ...
│
├── features/
│   ├── progress/
│   │   ├── ProgressViewModel.kt
│   │   ├── ProgressScreen.kt
│   │   └── models/
│   │
│   ├── quiz/
│   │   ├── QuizViewModel.kt
│   │   ├── QuizScreen.kt
│   │   ├── QuizEngine.kt           ← Quiz logic
│   │   └── models/
│   │
│   ├── flashcard/
│   │   ├── FlashcardViewModel.kt
│   │   ├── FlashcardScreen.kt
│   │   ├── SRSEngine.kt            ← Spaced-repetition logic
│   │   └── models/
│   │
│   ├── dashboard/                  ← (new, home screen)
│   │   ├── DashboardViewModel.kt
│   │   ├── DashboardScreen.kt
│   │   └── StatisticsWidget.kt
│   │
│   └── home/                        ← (existing, becomes dashboard)
│
└── utils/
    ├── DateUtils.kt
    ├── TimeUtils.kt
    └── SRSAlgorithm.kt
```

---

## 🔄 Implementation Phases

### Phase 2a: Database Foundation (3-4 days)
- [ ] Set up Room database
- [ ] Create all entities (Progress, Quiz, Flashcard, etc.)
- [ ] Create DAOs for CRUD operations
- [ ] Create Repository pattern for clean access
- [ ] Add dependency injection (Hilt)
- [ ] Unit tests for database operations

### Phase 2b: Progress Tracking (2-3 days)
- [ ] Track topic views (auto-save on topic open)
- [ ] Track time spent (measure playback duration)
- [ ] Create ProgressViewModel
- [ ] Build ProgressScreen (list with progress bars)
- [ ] Add progress persistence
- [ ] Display progress in Dashboard

### Phase 2c: Quiz System (3-4 days)
- [ ] Create quiz content (JSON files for each topic)
- [ ] Build QuizEngine (question selection, scoring)
- [ ] Create QuizScreen (MCQ and visual prediction modes)
- [ ] Store quiz attempts in DB
- [ ] Display quiz history and scores
- [ ] Add difficulty levels

### Phase 2d: Flashcards + SRS (3-4 days)
- [ ] Implement SM-2 spaced-repetition algorithm
- [ ] Create SRSEngine for scheduling
- [ ] Build FlashcardScreen (card flipping, response buttons)
- [ ] Create daily review queue
- [ ] Track retention rate
- [ ] Add flashcard statistics

### Phase 2e: Dashboard & Analytics (2-3 days)
- [ ] Create DashboardScreen replacing home
- [ ] Daily streak tracking
- [ ] Statistics widget (topics studied, time spent, etc.)
- [ ] Today's review recommendations
- [ ] Quick access buttons

### Phase 2f: Polish (1-2 days)
- [ ] Settings screen enhancements
- [ ] Data export (for backup)
- [ ] Search and filter functionality
- [ ] Dark mode support for new screens
- [ ] Accessibility (TalkBack)

---

## 📊 SM-2 Algorithm (Spaced Repetition)

The industry-standard algorithm for optimal learning:

```kotlin
class SRSAlgorithm {
    // SM-2: Quality of response (0-5, 5 = perfect)
    // q = quality of response
    // EF = ease factor (difficulty multiplier)
    // I = interval (days until next review)
    // n = number of repetitions
    
    fun calculateNextReview(
        quality: Int,           // 0-5
        easeFactor: Double,
        interval: Int,
        repetitions: Int
    ): Pair<Int, Double> {
        val newEF = when {
            quality < 3 -> (easeFactor - 0.2).coerceAtLeast(1.3)
            else -> easeFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02))
        }
        
        val newInterval = when {
            quality < 3 -> 1                    // Restart if forgotten
            repetitions == 0 -> 1               // First review: 1 day
            repetitions == 1 -> 3               // Second review: 3 days
            else -> (interval * newEF).toInt()  // Multiply by ease factor
        }
        
        return Pair(newInterval, newEF)
    }
}
```

---

## 🎯 Quiz Content Structure

```json
// Example: quizzes/bubble-sort.json
{
  "topicId": "bubble-sort",
  "quizzes": [
    {
      "id": "bs-1",
      "type": "MCQ",
      "difficulty": 1,
      "question": "What is the time complexity of Bubble Sort in the worst case?",
      "options": ["O(n)", "O(n log n)", "O(n²)", "O(2ⁿ)"],
      "correctAnswer": 2,
      "explanation": "Bubble sort makes n passes, each comparing up to n elements, so O(n²)"
    },
    {
      "id": "bs-2",
      "type": "PREDICT_FRAME",
      "difficulty": 2,
      "question": "Which frame comes next?",
      "currentFrameIndex": 5,
      "topicId": "bubble-sort",
      "explanation": "The next comparison should be between adjacent elements..."
    }
  ]
}
```

---

## 🗄️ Key Dependencies to Add

```kotlin
// build.gradle.kts

dependencies {
    // Room (database)
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    
    // DataStore (already added)
    
    // WorkManager (for background tasks like SRS scheduling)
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    
    // SerializableJson (for JSON quiz content)
    // (already have Gson)
}
```

---

## 📈 Progress Metrics Tracked

- **Per Topic:**
  - Last viewed timestamp
  - Total time spent
  - Quiz attempts & scores
  - Flashcard retention rate
  - Completion status

- **Overall:**
  - Daily streak
  - Total study time
  - Topics mastered (>80% on quizzes)
  - Average quiz score
  - Flashcard retention rate
  - Topics to review today (SRS queue)

---

## 🧪 Testing Strategy

### Unit Tests
- SRSAlgorithm calculations
- Quiz scoring logic
- Repository operations
- ViewModel state management

### UI Tests (Compose)
- Quiz answering flow
- Flashcard flipping
- Progress screen rendering
- Dashboard calculations

### Integration Tests
- End-to-end: view topic → take quiz → review flashcard
- Database persistence
- Time tracking accuracy

---

## 🚀 Success Criteria

Phase 2 is complete when:
- ✅ All user data persists in Room database
- ✅ Dashboard shows accurate progress metrics
- ✅ Quiz system tracks attempts and scores
- ✅ Flashcard SRS queue is working
- ✅ Daily streak is tracked correctly
- ✅ All new features have UI tests
- ✅ Version bumped to 1.1.0
- ✅ APK builds and installs without errors

---

## 📋 Implementation Checklist

### Week 1: Database & Progress
- [ ] Room setup
- [ ] All entities & DAOs
- [ ] Repository pattern
- [ ] Progress tracking MVP
- [ ] Basic tests

### Week 2: Quiz System
- [ ] Quiz content structure
- [ ] QuizEngine & scoring
- [ ] QuizScreen UI
- [ ] Quiz history storage
- [ ] Quiz tests

### Week 3: Flashcards & Dashboard
- [ ] SM-2 algorithm
- [ ] SRSEngine
- [ ] FlashcardScreen UI
- [ ] DashboardScreen with stats
- [ ] Polish & testing

---

## 🔗 Integration Points

Phase 2 integrates with Phase 1:
- When user opens a topic → track time spent (store in Progress)
- Add "Take Quiz" button on topic detail screen
- Add "Review Flashcards" button in navigation
- Track which topics user has visited

---

## 📦 Deliverables

- Complete Room database with all entities
- ProgressScreen showing per-topic stats
- QuizScreen with MCQ and visual modes
- FlashcardScreen with SRS scheduling
- DashboardScreen with streaks & stats
- 50+ new unit/UI tests
- Updated CLAUDE.md with database schema
- Version 1.1.0 release

---

## 🎓 What User Gets

After Phase 2:
✅ Data persists between sessions
✅ Can see their progress at a glance
✅ Test their understanding with quizzes
✅ Get scientifically-timed flashcard reviews
✅ Track daily streaks and study time
✅ Complete learning system (not just visualization)

---

## Next Steps

1. Set up Room database foundation
2. Create all entities and DAOs
3. Build Repository pattern
4. Implement progress tracking
5. Add quiz system
6. Implement SRS flashcards
7. Build dashboard UI
8. Testing and polish
9. Version bump to 1.1.0
10. Push to GitHub (auto-release)

---

**Starting with:** Database foundation (Room setup)  
**First commit:** "chore: initialize Room database for Phase 2"

Ready to begin! 🚀
