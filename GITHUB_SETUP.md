# GitHub Setup Guide for Interview Prep Lab

Follow these steps to push your code to GitHub and enable automated APK builds.

## Step 1: Create GitHub Repository

1. Go to https://github.com/new
2. Enter repository name: `interview-prep-lab`
3. Description: "Android app for DSA interview prep with interactive animations"
4. Choose **Public** (so anyone can download APKs)
5. Click **Create repository**

## Step 2: Configure Local Repository

Run these commands in your terminal:

```bash
cd /home/dhawank/interview-prep-lab

# Set your GitHub username and email
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Add remote repository (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/interview-prep-lab.git

# Rename branch to main (if not already)
git branch -M main

# Push all commits to GitHub
git push -u origin main
```

**Important:** Replace `YOUR_USERNAME` with your actual GitHub username.

## Step 3: Verify on GitHub

1. Go to https://github.com/YOUR_USERNAME/interview-prep-lab
2. You should see all your code files and commits
3. Navigate to **.github/workflows** folder — verify both workflow files are there

## Step 4: Enable GitHub Actions

1. On your GitHub repo, click **Settings** tab
2. In left sidebar, click **Actions → General**
3. Under "Actions permissions", select: **"Allow all actions and reusable workflows"**
4. Scroll down and click **Save**

## Step 5: Trigger First Build

Now push a small change to trigger the CI/CD:

```bash
cd /home/dhawank/interview-prep-lab

# Make a small change (e.g., update README)
echo "# Interview Prep Lab" > TEST.md

# Commit and push
git add TEST.md
git commit -m "test: trigger first CI/CD build"
git push origin main
```

Or simply wait — the next regular push to main will trigger the workflow.

## Step 6: Monitor Build

1. Go to your GitHub repo
2. Click **Actions** tab
3. You should see a workflow run starting
4. Click on the run to see live build logs
5. Wait for it to complete (usually 5-10 minutes)

**Expected outcome:**
- ✅ Build succeeds
- ✅ Version number incremented
- ✅ APK created
- ✅ GitHub Release created with APK attached

## Step 7: Access Your APK

### From Releases Page
1. Go to https://github.com/YOUR_USERNAME/interview-prep-lab/releases
2. You'll see the latest release (e.g., "Interview Prep Lab v1.0.1")
3. Download the APK file

### From Direct Link
```bash
# Download latest APK
curl -L -O https://github.com/YOUR_USERNAME/interview-prep-lab/releases/download/v1.0.1/InterviewPrepLab-1.0.1.apk
```

## Step 8: Install on Android Device

### First Time Setup

**On Android Device:**
1. Go to **Settings → Security**
2. Enable **"Unknown sources"** (allows installing from APK files)
3. Go back to home screen

**On Computer:**
1. Download APK from releases
2. Connect Android device via USB
3. Run:
   ```bash
   adb install InterviewPrepLab-1.0.1.apk
   ```

**Or manually:**
1. Download APK to phone (via browser or email)
2. Tap the APK file
3. Click **Install**
4. Open "Interview Prep Lab" from app drawer

### Update Existing Installation

Simply:
1. Download new APK (from next release)
2. Tap it
3. Android recognizes it as update → **Install**

Done! No need to uninstall old version.

## Step 9: Continuous Deployment

From now on:

```bash
cd /home/dhawank/interview-prep-lab

# Make changes to code
# ... edit files ...

# Commit and push (as usual)
git add .
git commit -m "feat: add new feature"
git push origin main
```

**Automatically:**
- ✅ GitHub Actions builds APK
- ✅ Version bumps from 1.0.1 → 1.0.2
- ✅ New Release created
- ✅ Available for download on Releases page

## Troubleshooting

### Build Fails in Actions
1. Click **Actions** tab
2. Click the failed run
3. Scroll down to see error messages
4. Common issues:
   - Java version mismatch (should be 17)
   - Gradle cache (usually auto-fixes on retry)
   - Missing dependencies (check build.gradle.kts)

**Fix:** Make a small commit and push again to retry

### Can't Push to GitHub
1. Check remote is set correctly:
   ```bash
   git remote -v
   # Should show: origin https://github.com/YOUR_USERNAME/interview-prep-lab.git
   ```

2. If wrong, update it:
   ```bash
   git remote set-url origin https://github.com/YOUR_USERNAME/interview-prep-lab.git
   ```

3. Try pushing again:
   ```bash
   git push origin main
   ```

### Workflows Not Triggering
1. Make sure you enabled Actions (see Step 4)
2. Make sure your push was to **main** branch
3. Check that workflow files exist in `.github/workflows/`
4. Force a retry by pushing a commit:
   ```bash
   git commit --allow-empty -m "trigger workflow"
   git push origin main
   ```

## Commands Reference

### Check Current Version
```bash
grep 'versionName' app/build.gradle.kts
grep 'versionCode' app/build.gradle.kts
```

### View Recent Commits
```bash
git log --oneline -10
```

### Pull Latest Changes (if working on multiple machines)
```bash
git pull origin main
```

### View All Releases
```bash
# On GitHub: https://github.com/YOUR_USERNAME/interview-prep-lab/releases
# Or via terminal:
git tag  # Lists all version tags
```

## Next Steps

1. **Share your app:** Send release link to friends/team
   - https://github.com/YOUR_USERNAME/interview-prep-lab/releases

2. **Monitor downloads:** GitHub shows download counts for each release

3. **Gather feedback:** Users can open Issues if they find bugs

4. **Continue development:**
   - Phase 2: Add progress tracking & quizzes
   - Phase 3: Add C programming concepts
   - Phase 4: Add networking protocols
   - Phase 5: Add Linux kernel concepts

---

## Example: Your First Release

After setup, you'll have:

```
📦 Interview Prep Lab v1.0.1
├─ 📱 InterviewPrepLab-1.0.1.apk (5-8 MB)
└─ 📝 Release Notes
   ├─ Features
   ├─ Installation instructions
   └─ Topics covered
```

Users can download the APK directly — no Play Store needed! 🎉

---

## Summary

✅ Create GitHub repo  
✅ Push code  
✅ Enable Actions  
✅ Build APK automatically  
✅ Download from Releases  
✅ Install on phone  
✅ Enjoy! 🚀

Questions? Check DEPLOYMENT.md for detailed CI/CD explanation.
