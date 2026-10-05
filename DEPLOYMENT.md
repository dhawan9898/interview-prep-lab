# Deployment & CI/CD Guide

This document explains how to deploy Interview Prep Lab to GitHub and automatically build APKs on each push.

## Overview

The project includes **automated CI/CD** that:
- ✅ Builds APK on every push to `main`
- ✅ Auto-increments version number (major.minor.patch)
- ✅ Auto-increments version code
- ✅ Creates GitHub Release with APK attached
- ✅ Generates release notes automatically
- ✅ Updates existing app installations (install APK to upgrade)

## Setup Instructions

### 1. Create GitHub Repository

```bash
# From your local machine
cd ~/interview-prep-lab

# Initialize git (if not already done)
git init
git add .
git commit -m "Initial commit: Interview Prep Lab Phase 1 complete"

# Add remote and push
git remote add origin https://github.com/YOUR_USERNAME/interview-prep-lab.git
git branch -M main
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username.

### 2. Enable GitHub Actions

In your repository:
1. Go to **Settings → Actions → General**
2. Under "Actions permissions", select **"Allow all actions and reusable workflows"**
3. Click **Save**

### 3. Verify Workflow Files

Check that these files exist in `.github/workflows/`:
- `build-release-simple.yml` — Main CI/CD pipeline (no signing required)
- `build-release.yml` — Optional advanced pipeline with APK signing

We'll use the **simple** version first (no signing secrets needed).

### 4. First Push

Once everything is set up, any push to `main` will trigger the workflow:

```bash
git push origin main
```

Go to **Actions** tab in your GitHub repo to watch the build.

---

## How It Works

### Automatic Version Bumping

Each push to `main`:
1. Reads current version from `app/build.gradle.kts`
2. Increments patch version: `1.0.0` → `1.0.1`
3. Increments version code: `1` → `2`
4. Commits version bump back to main
5. Creates a Git tag: `v1.0.1`

### Build Process

```
Push to main
    ↓
GitHub Actions triggers
    ↓
Checkout code
    ↓
Setup Java 17
    ↓
Build APK (debug or release)
    ↓
Create GitHub Release
    ↓
Attach APK to release
    ↓
Upload to artifacts (30-day retention)
```

### Release Generation

Automatically creates a release with:
- Release name: `Interview Prep Lab v1.0.1`
- Tag: `v1.0.1`
- APK file: `InterviewPrepLab-1.0.1.apk`
- Release notes (auto-generated)

---

## Installation & Updates

### First Installation
1. Go to **Releases** page: `https://github.com/YOUR_USERNAME/interview-prep-lab/releases`
2. Download the latest APK
3. On Android device: **Settings → Security → Enable "Unknown sources"**
4. Tap APK to install
5. Open "Interview Prep Lab" from app drawer

### Updating to New Version
Simply download the new APK and tap it. Android automatically recognizes it as an update (same app package ID) and installs over the existing version.

**No data loss** — all user data persists through updates (once Phase 2 is added).

---

## Version Format

Current version: **1.0.0** (Major.Minor.Patch)

### Incrementing Strategy

**Automatic (on every push to main):**
- Patch: `1.0.0` → `1.0.1` (bug fixes, small features)

**Manual (when needed, edit `app/build.gradle.kts`):**
- Minor: `1.0.1` → `1.1.0` (Phase 2, progress tracking)
- Major: `1.1.0` → `2.0.0` (Phase 3, C concepts)

---

## Current Build Configuration

### gradle.kts Format

```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        versionCode = 1           // Incremented by CI/CD
        versionName = "1.0.0"     // Incremented by CI/CD
    }
}
```

The CI/CD workflow automatically updates these values before building.

---

## Accessing Releases

### Web
Go to: `https://github.com/YOUR_USERNAME/interview-prep-lab/releases`

### Programmatically
```bash
# Download latest APK
curl -L -o InterviewPrepLab.apk \
  https://github.com/YOUR_USERNAME/interview-prep-lab/releases/download/$(curl -s https://api.github.com/repos/YOUR_USERNAME/interview-prep-lab/releases/latest | grep tag_name | cut -d'"' -f4)/InterviewPrepLab-*.apk
```

---

## CI/CD Workflow Reference

### File: `.github/workflows/build-release-simple.yml`

**Triggers:**
- On every push to `main`
- Manual trigger via `workflow_dispatch`

**Steps:**
1. **Checkout** — Clone repository
2. **Setup JDK 17** — Android build environment
3. **Get version** — Read current version
4. **Increment version** — Bump patch + code
5. **Build APK** — `./gradlew assembleRelease` (or debug fallback)
6. **Commit version** — Push version bump back to main
7. **Create Release** — GitHub release with APK
8. **Upload artifacts** — For workflow artifacts (30 days)

---

## Troubleshooting

### Build Fails
1. Check **Actions** tab for error log
2. Common issues:
   - Java version mismatch (should be 17)
   - Gradle cache (automatically handled, usually not an issue)
   - APK size limit (unlikely with current code)

### Version Bump Not Working
1. Ensure GitHub token has write permissions (usually automatic)
2. Check that `app/build.gradle.kts` follows the expected format
3. Manual fix: edit file locally and commit

### APK Not Created
1. Check build log for Kotlin/Java compilation errors
2. Ensure `build.gradle.kts` is valid
3. Debug APK fallback should succeed (uses `assembleDebug` if `assembleRelease` fails)

---

## Next Steps

### Phase 2 (Progress Tracking)
When adding persistence (Room database):
1. Increment to `1.1.0` manually in `app/build.gradle.kts`
2. Subsequent pushes will increment patch: `1.1.0` → `1.1.1` → etc.

### Adding APK Signing (Optional)
For production releases:
1. Generate signing key (see advanced setup)
2. Add secrets to GitHub (Settings → Secrets → Actions)
3. Switch to `build-release.yml` workflow

---

## Release Notes Format

Each release auto-generates:
```
# Interview Prep Lab vX.Y.Z

Version Code: N
Release Date: YYYY-MM-DD HH:MM:SS

## Download
[InterviewPrepLab-X.Y.Z.apk](...)

## Installation Instructions
[Steps to install]

## Features
[Full feature list]

## Topics
[All DSA topics]
```

You can edit release notes manually on GitHub if needed.

---

## Summary

You now have:
✅ Automated builds on every push to `main`
✅ Auto-incrementing version numbers
✅ GitHub Releases with APK downloads
✅ Easy updates for users (APK → install)
✅ All without leaving your development environment

Push code → GitHub handles the rest! 🚀
