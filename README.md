# Interview Prep Lab

Your single source for interview prep: DSA, C, Linux kernel, and networking (L2/L3) concepts with detailed step-by-step animations explaining every operation.

**Native Android app • Fully offline • Frame-based playback • 60fps smooth animations**

## Features

### Current (Phase 0)
- ✅ Core frame-based playback engine (play/pause/step/scrub/speed control)
- ✅ Bubble Sort visualization (BarsScene renderer)
- ✅ Material Design 3 UI
- ✅ Jetpack Compose architecture

### Planned (Phases 1–6)
- DSA: 31 topics (16 structures, 8 sorts, 3 searches, 2 techniques, 4 foundations)
- C: pointers, memory layout, malloc/free bugs, struct padding, UB, bit tricks
- Networking L2: Ethernet, MAC learning, VLAN, STP, ARP
- Networking L3: IPv4/IPv6, subnetting, CIDR, ICMP, routing, NAT, OSPF, BGP
- Linux kernel: scheduler, virtual memory, syscalls, interrupts, netfilter, locking
- Interactive: quizzes, flashcards w/ spaced repetition, bookmarks, progress tracking

## Quick Start

### Prerequisites
- Android Studio 2023.1+
- JDK 17
- Android SDK 34 (API level 34)

### Build & Run
```bash
git clone https://github.com/dhawan9898/interview-prep-lab.git
cd interview-prep-lab
./gradlew installDebug
adb shell am start -n com.interviewpreplab/.MainActivity
```

### Test
```bash
# Unit tests (algorithm runners)
./gradlew test

# UI tests (Compose renderers)
./gradlew connectedAndroidTest
```

## Architecture

**Frame-based playback** (same idea as the web version):
1. Algorithm runner generates immutable `Frame` objects (narration, phase, scene data)
2. `PlayerViewModel` manages playback state (current frame, speed, play/pause)
3. Renderer (e.g., `BarsRenderer`) draws the scene on Compose Canvas
4. UI controls (play button, scrubber) drive the player

```
Frame(narr, phase, codeLine, stats, scene)
  ↓
Player (play/pause/step/scrub)
  ↓
Renderer (Canvas drawing of scene)
  ↓
UI (Compose Material 3)
```

This architecture is **reused across all domains**: sorts, trees, graphs, protocols, kernel concepts. Add a new topic = write a runner + use an existing (or new) renderer.

## Project Structure

```
interview-prep-lab/
├── app/
│   ├── src/main/java/com/interviewpreplab/
│   │   ├── core/
│   │   │   ├── model/Frame.kt (Scene types: BarsScene, TreeScene, etc.)
│   │   │   ├── player/PlayerViewModel.kt
│   │   │   └── ui/*Renderer.kt (Canvas renderers, one per scene type)
│   │   ├── features/
│   │   │   ├── sorts/ (BubbleSortRunner, ...)
│   │   │   ├── searches/
│   │   │   ├── data_structures/
│   │   │   ├── kernel/ (Phase 5)
│   │   │   └── networking/ (Phase 4)
│   │   └── MainActivity.kt
│   ├── src/main/res/
│   │   └── values/, xml/
│   └── build.gradle.kts
├── CLAUDE.md (for AI assistants; detailed architecture & how to extend)
├── README.md (this file)
└── plan.md (overall development roadmap)
```

## How to Add a New Topic

1. **Write a runner** (e.g., `InsertionSortRunner.kt`):
   ```kotlin
   object InsertionSortRunner {
       fun run(array: List<Int>): List<Frame> { ... }  // generates frames
   }
   ```

2. **Test the runner**:
   ```bash
   ./gradlew test -k InsertionSort
   ```

3. **Add to UI** (e.g., in topic list or playground):
   ```kotlin
   val frames = InsertionSortRunner.run(myArray)
   playerVM.loadFrames(frames)
   ```

4. **Use existing renderer** (or build a new one if needed):
   - Sorts use `BarsRenderer` (already exists)
   - Trees use `TreeRenderer` (TODO in Phase 1)
   - Protocols use `PacketFlowRenderer` (TODO in Phase 4)

See CLAUDE.md for detailed patterns and examples.

## Development Roadmap

| Phase | Focus | Timeline | Status |
|---|---|---|---|
| P0 | Core player + Bubble Sort proof-of-concept | 1–2 weeks | ✅ Done |
| P1 | DSA renderers + 31 topics | 3–4 weeks | 🔄 Next |
| P2 | Progress tracking + quizzes + flashcards | 2 weeks | 📋 Planned |
| P3 | C memory concepts | 1.5 weeks | 📋 Planned |
| P4 | Networking (L2/L3/L4) | 2.5 weeks | 📋 Planned |
| P5 | Linux kernel | 2.5 weeks | 📋 Planned |
| P6 | Polish + ship | 1 week | 📋 Planned |

## Design Principles

1. **One algorithm → N frames**: pre-generate detailed step-by-step explanations; UI just plays them back
2. **Reusable player**: all topics (sorts, trees, protocols, kernel) use the same playback engine
3. **Offline-first**: all content bundled as assets; no network calls in v1
4. **Smooth animations**: Canvas + Compose for 60fps playback on mobile/tablet
5. **Narration teaches**: every frame's text explains the "why", not just the "what"

## Contributing

- Follow Kotlin conventions (ktlint, Android style guide)
- Every algorithm runner must have unit tests
- Every renderer must render correctly on phone (375dp) and tablet (600dp+)
- Narration: second-person voice ("Out of order, so swap them"), clear and educational

## License

MIT

## Contact

Built as your **all-in-one interview prep companion**. Questions? Open an issue.

---

**Current Status**: Phase 0 complete (core engine + Bubble Sort). Phase 1 (DSA port) starts next.
