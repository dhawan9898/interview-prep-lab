# Lesson notes style (one format for every lesson)

Source of truth for `app/src/main/assets/lessons/<topic-id>.json`. `LessonAssetsTest` enforces the structure.
Write in your own words from `reference-data/` (do not copy source text). Second person where natural
("Out of order, so swap them."). Inline markup is only `` `code` `` and `**bold**`; there are no tables, so
use bullets of the form `**Term**: explanation`.

## JSON shape
`summary`, `sections[]`, `subtopics[]`, `takeaways[]`, `practice[]`, `references[]`.
A section is `{heading, body?, bullets?, code?}`. A subtopic is `{title, summary, sections[], topicId?}`.
`topicId` links to a child lesson (a topic in `topicList` with `parentId` = this lesson's id).

## Sections: fixed headings, fixed order
Omit a heading that truly does not apply; never rename it or reorder it. A section with no heading continues the one before it (for extra code samples); it can never come first.

1. `Overview`: what it is, the problem it solves, where it sits in the bigger picture.
2. `Core Concepts`: definitions and terminology, each as a bullet.
3. `How It Works`: step-by-step mechanism, with a worked example and code (C for C/DSA, header/message layout for networking).
4. `Variants & Types`: variations, related forms, alternatives and when to pick each.
5. `Complexity & Costs`: time/space, sizes, limits, performance trade-offs (numbers, not adjectives).
6. `Common Pitfalls & Gotchas`: bugs, misconceptions, security issues.
7. `Real-World Use`: where it appears in production systems.
8. `Interview Notes`: the questions asked and the one-line answers.

## Other parts
- `summary`: 3 to 4 sentences. What, why it matters, where used.
- `subtopics`: 6 to 12. Each has a title, a one-line summary, and 1 to 3 sections (the heading is optional). Unique titles. A subtopic that outgrows ~400 words becomes a child lesson and gets `topicId`.
- `takeaways`: 5 to 8 one-line facts.
- `practice`: 6 to 10 items, `difficulty` is `Easy`, `Medium` or `Hard`, mix of all three.
- `references`: at least 2. Source page URLs from `reference-data/manifest.json`, plus RFC or textbook where relevant.
- Length: about 1,500 to 4,000 words per lesson file. Split into child lessons past that.
