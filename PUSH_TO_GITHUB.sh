#!/bin/bash

# Interview Prep Lab - Push to GitHub
# Run this script on your machine (where you have internet access)

# Navigate to project directory
cd /path/to/interview-prep-lab  # Change this to your actual path

# Ensure main branch exists
git branch -M main

# Add GitHub remote
git remote remove origin 2>/dev/null  # Remove if exists
git remote add origin https://github.com/dhawan9898/interview-prep-lab.git

# Push to GitHub
echo "Pushing code to GitHub..."
git push -u origin main

# Check if push succeeded
if [ $? -eq 0 ]; then
    echo "✅ Code pushed successfully!"
    echo ""
    echo "📍 Next steps:"
    echo "1. Go to: https://github.com/dhawan9898/interview-prep-lab"
    echo "2. Click Settings → Actions → General"
    echo "3. Select 'Allow all actions and reusable workflows'"
    echo "4. Click Save"
    echo ""
    echo "5. Your first APK will build automatically!"
    echo "6. Download from: Releases tab"
else
    echo "❌ Push failed. Check your internet connection and GitHub credentials."
fi
