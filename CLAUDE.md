# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with this Interview Prep Lab Android repository.

## What this is

A native Android app (Kotlin + Jetpack Compose) that teaches C, DSA and networking concepts through detailed written
lessons with bundled illustrations. It is fully offline: all content ships as JSON and image assets. There are no
animations or playback engine; those were removed on purpose.

## Development

```bash
./gradlew installDebug          # run on emulator or device
adb shell am start -n com.interviewpreplab/.MainActivity
./gradlew test                  # unit tests, including the lesson-notes lint
./gradlew connectedAndroidTest  # Compose/UI tests
```

## Structure

```
app/src/main/
  assets/lessons/<topic-id>.json         lesson notes (one per topic)
  assets/lesson-images/<topic-id>/NN.*   illustrations referenced by `figures`
  java/com/interviewpreplab/
    features/topics/TopicData.kt         topicList + courses (single source of topics)
    features/lessons/Lesson.kt           lesson model + loader (Gson, all collections nullable)
    features/lessons/LessonScreen.kt     renders a lesson
    features/{home,syllabus,labs,flashcard,quiz,progress,profile}/   other screens
    core/database/                       Room: progress, quizzes, flashcards
    ui/components/                       RichText (`code` and **bold**), AssetImage, cards
    navigation/AppNav.kt
docs/NOTES_STYLE.md                      the one notes format (read before writing notes)
docs/IMAGE_SOURCES.md                    where each bundled image came from
reference-data/                          local, git-ignored source material (GfG, RFCs, Wikipedia)
```

## Lesson notes

- Every lesson follows `docs/NOTES_STYLE.md`: fixed section headings in a fixed order, 6-12 subtopics, 5-8 takeaways,
  6-10 practice items, 1,500-4,500 words. `LessonAssetsTest` enforces it, plus that figure files exist and child
  links resolve. Run `./gradlew test` after any notes change.
- Write in your own words from `reference-data/`; never paste source text. It is copyrighted study material.
- Large topics can link child lessons via `Subtopic.topicId` and `Topic.parentId`. Child lessons are not listed in the
  course, they open from their parent.
- Images are optional `figures` entries (max 12 per lesson). They are third-party artwork bundled for personal use;
  see `docs/IMAGE_SOURCES.md` before publishing the app.

## Adding a topic

1. Add a `Topic(...)` to `topicList` in `TopicData.kt` (and a course category if new).
2. Add `assets/lessons/<id>.json` following `docs/NOTES_STYLE.md`.
3. Optionally add `assets/lesson-images/<id>/` files and list them under `figures`.
4. `./gradlew test`.

## Key decisions

- **Offline-first**: all content in assets/, no backend, no network permission.
- **Notes over animation**: depth and consistency of the written lessons is the product.
- **One format**: every lesson uses the same section layout; do not vary it per topic.
- Lesson completion is manual (Mark complete button); progress is stored in Room.

## Roadmap

P2 Room progress/quizzes/flashcards (in place), P3-P5 more C/kernel/networking content (Linux kernel course is empty
and has no reference data yet), P6 polish and ship (accessibility, dark mode, tablet QA, image licensing).
