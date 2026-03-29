# 🧪 Student Copilot - Testing Guide

Complete testing instructions for the Android app and API.

---

## 📋 Table of Contents

1. [Setup & Prerequisites](#setup--prerequisites)
2. [Build the App](#build-the-app)
3. [Run the App](#run-the-app)
4. [Manual Feature Testing](#manual-feature-testing)
5. [API Testing](#api-testing)
6. [Database Testing](#database-testing)
7. [Unit Tests](#unit-tests)
8. [Debugging](#debugging)
9. [Common Issues & Fixes](#common-issues--fixes)

---

## Setup & Prerequisites

### System Requirements
- **Android Studio**: Flamingo or later
- **Java**: Version 11 or higher
- **Gradle**: 8.0+
- **Android SDK**: API 34
- **Emulator/Device**: API 28+

### Verify Installation
```bash
# Check Java version
java -version
# Output should show Java 11+

# Check Gradle wrapper
cd StudentCopilot
./gradlew --version
# Output should show Gradle 8.0+
```

### Clone Repository
```bash
git clone https://github.com/ryanair000/studyplanner.git
cd studyplanner
```

---

## Build the App

### Option 1: Android Studio GUI (Recommended)

1. **Open Project**
   - Launch Android Studio
   - File → Open
   - Select `StudyPlanner` directory
   - Wait for Gradle sync (may take 2-3 minutes)

2. **Build**
   - Top menu: Build → Make Project
   - Wait for "Build Successful" message

3. **Check Build Output**
   - Should see no red errors
   - Warnings are OK

### Option 2: Command Line

```bash
cd StudyPlanner

# Build debug APK
./gradlew build

# Build release APK (requires signing)
./gradlew assembleRelease

# Clean build
./gradlew clean build

# View build tasks
./gradlew tasks
```

### Build Output
```
✅ BUILD SUCCESSFUL
Build succeeded
- Compiled: 30 Kotlin files
- Generated: app-debug.apk
- Location: app/build/outputs/apk/debug/
```

---

## Run the App

### Option 1: Android Studio Emulator

1. **Create Virtual Device**
   - Tools → Device Manager
   - Create Device
   - Select "Pixel 6" (recommended)
   - Select API 34
   - Click "Next" → "Finish"

2. **Start Emulator**
   - Click ▶ (play button) next to device
   - Wait 2-3 minutes for boot

3. **Run App**
   - Top menu: Run → Run 'app'
   - Or press Shift + F10
   - Wait for app to launch (~30 seconds)

### Option 2: Physical Device

1. **Enable Developer Mode**
   - Settings → About Phone
   - Tap "Build Number" 7 times
   - Go back to Settings
   - Developer Options → USB Debugging (ON)

2. **Connect via USB**
   - Connect phone to computer
   - Allow USB debugging prompt

3. **Run from Android Studio**
   - Click Run icon
   - Select your device
   - Click OK

### Option 3: Command Line

```bash
# List available devices
./gradlew devices

# Install and run debug APK
./gradlew installDebug

# Install specific device
adb -s <device_id> install app/build/outputs/apk/debug/app-debug.apk
```

### Expected Output
```
Installation successful ✅
App launches and shows:
  Dashboard screen with "No upcoming assignments or exams"
```

---

## Manual Feature Testing

### ✅ Test 1: Add a Course

**Steps**:
1. Launch app → "Courses" tab
2. Tap floating "+" button
3. Enter course name: "Calculus I"
4. Click "Add"
5. Verify course appears in list

**Expected Result**:
```
✅ Course "Calculus I" appears in list
```

**Failure Indicators**:
- ❌ Dialog doesn't open
- ❌ Course not saved
- ❌ App crashes

---

### ✅ Test 2: Add Multiple Courses

**Steps**:
1. Courses tab → "+" button
2. Add "Physics 101" → Click Add
3. Add "English Comp" → Click Add
4. Add "History 201" → Click Add
5. Verify all 4 courses in list

**Expected Result**:
```
✅ All 4 courses visible:
   - Calculus I
   - Physics 101
   - English Comp
   - History 201
```

---

### ✅ Test 3: Add Assignment

**Steps**:
1. Assignments tab → "+" button
2. Title: "Chapter 5 Exercises"
3. Course: Select "Calculus I"
4. Date: Pick date 3 days from now
5. Click "Add"
6. Verify assignment in list

**Expected Result**:
```
✅ Assignment visible:
   "Chapter 5 Exercises" (Calculus I)
   Due: [3 days from now]
```

---

### ✅ Test 4: Add Assignment with Different Date

**Steps**:
1. Assignments → "+"
2. Title: "Physics Lab Report"
3. Course: "Physics 101"
4. Date: Pick date 7 days from now
5. Add

**Expected Result**:
```
✅ Two assignments visible in list
   Sorted by due date (soonest first)
```

---

### ✅ Test 5: Add Exam

**Steps**:
1. Exams tab → "+" button
2. Title: "Calculus Midterm"
3. Course: "Calculus I"
4. Date: Pick date 14 days from now
5. Click "Add"

**Expected Result**:
```
✅ Exam visible:
   "Calculus Midterm" (Calculus I)
   Exam: [date 14 days away]
```

---

### ✅ Test 6: Dashboard View

**Steps**:
1. Click "Dashboard" tab
2. Observe summary card

**Expected Result**:
```
✅ Shows:
   Upcoming Tasks: "2 assignments • 1 exams due soon"
   Nearest: [closest deadline task]
```

---

### ✅ Test 7: Generate Study Plan

**Steps**:
1. Dashboard tab
2. Click "Generate Study Plan" button
3. Wait 2-3 seconds for API response
4. Verify plan appears in card below

**Expected Result**:
```
✅ Study plan text appears:
   📚 YOUR PERSONALIZED STUDY PLAN

   🔴 URGENT (Due within 7 days):
     • [ASSIGNMENT] Chapter 5 Exercises (Calculus I)
       Due in 3 days

   📅 UPCOMING (Next 14-30 days):
     • [EXAM] Calculus Midterm (Calculus I)
       Due in 14 days

   💡 STUDY TIPS:
     • Tackle urgent tasks first
```

---

### ✅ Test 8: Navigation Between Tabs

**Steps**:
1. Dashboard tab → click "Courses" → verify shows courses
2. Courses → click "Assignments" → verify shows assignments
3. Assignments → click "Exams" → verify shows exams
4. Exams → click "Dashboard" → verify shows dashboard

**Expected Result**:
```
✅ All tabs navigate correctly
✅ Data persists when switching tabs
```

---

### ✅ Test 9: Empty States

**Steps**:
1. Uninstall app: adb uninstall com.example.studentcopilot
2. Reinstall: ./gradlew installDebug
3. Launch app
4. Check each tab

**Expected Result**:
```
✅ Dashboard: "No upcoming assignments or exams..."
✅ Courses: "No courses yet..."
✅ Assignments: "No assignments yet..."
✅ Exams: "No exams yet..."
```

---

### ✅ Test 10: Date Picker

**Steps**:
1. Assignments → "+"
2. Click "Pick Date"
3. Dialog shows: Day / Month / Year fields
4. Change values:
   - Day: 15
   - Month: 4
   - Year: 2026
5. Click "OK"

**Expected Result**:
```
✅ Date field updates to: "Apr 15, 2026"
```

---

### ✅ Test 11: Form Validation

**Steps**:
1. Assignments → "+"
2. Try clicking "Add" without entering data
3. Button should be disabled (grayed out)
4. Enter title
5. Try clicking "Add" without course
6. Button should still be disabled
7. Select course
8. Button becomes enabled

**Expected Result**:
```
✅ Add button only enabled when:
   - Title is filled
   - Course is selected
```

---

### ✅ Test 12: Close Dialogs

**Steps**:
1. Assignments → "+"
2. Click X button (top right)
3. Dialog closes without adding

**Expected Result**:
```
✅ Dialog closes without saving
✅ No new assignment created
```

---

## API Testing

### Test API Endpoint Directly

#### Using cURL

```bash
# Test Study Plan Generation
curl -X POST https://studyplanner.qybrrlabs.frica/api/study-plan \
  -H "Content-Type: application/json" \
  -d '{
    "assignments": [
      {
        "id": 1,
        "title": "Math Homework",
        "course_name": "Calculus I",
        "due_date": 1704067200000
      }
    ],
    "exams": [
      {
        "id": 1,
        "title": "Physics Exam",
        "course_name": "Physics 101",
        "exam_date": 1704153600000
      }
    ]
  }'
```

#### Expected Response
```json
{
  "study_plan": "📚 YOUR PERSONALIZED STUDY PLAN\n\n🔴 URGENT (Due within 7 days):\n  • [ASSIGNMENT] Math Homework (Calculus I)\n    Due in 2 days\n\n📅 UPCOMING (Next 14-30 days):\n  • [EXAM] Physics Exam (Physics 101)\n    Due in 21 days\n\n💡 STUDY TIPS:\n  • Tackle urgent tasks first\n  • Break large assignments into smaller chunks\n  • Study for exams a little bit every day\n  • Stay organized and consistent!",
  "status": "success"
}
```

#### Using Postman

1. **Create Request**
   - Method: POST
   - URL: `https://studyplanner.qybrrlabs.frica/api/study-plan`

2. **Headers**
   - Content-Type: application/json

3. **Body** (raw JSON)
   ```json
   {
     "assignments": [...],
     "exams": [...]
   }
   ```

4. **Send** → Check response

### Test Empty Payload

```bash
curl -X POST https://studyplanner.qybrrlabs.frica/api/study-plan \
  -H "Content-Type: application/json" \
  -d '{"assignments": [], "exams": []}'
```

**Expected Response**:
```json
{
  "study_plan": "No upcoming tasks to plan for. Great job staying on top of your coursework!",
  "status": "success"
}
```

---

## Database Testing

### View Database in Android Studio

1. **Device File Explorer**
   - View → Tool Windows → Device File Explorer
   - Navigate: `/data/data/com.example.studentcopilot/databases/`
   - Right-click `student_copilot_database`
   - "Save As" to save locally

2. **Open with SQLite Browser**
   - Download: https://sqlitebrowser.org/
   - Open saved database
   - View all tables and data

### Check Database Tables

```sql
-- View all courses
SELECT * FROM courses;

-- View all assignments
SELECT * FROM assignments;

-- View all exams
SELECT * FROM exams;

-- Check assignment count
SELECT COUNT(*) as assignment_count FROM assignments;
```

### Verify Data Persistence

1. Add course "Test Course"
2. Verify it appears in database
3. Close and restart app
4. Verify "Test Course" still there

---

## Unit Tests

### Run All Tests

```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests "*.CourseTest"

# View test results
# HTML report: app/build/reports/tests/testDebugUnitTest/index.html
```

### Run Instrumentation Tests

```bash
# Connect device/emulator first
./gradlew connectedAndroidTest

# Run specific test
./gradlew connectedAndroidTest --tests "*.DashboardTest"
```

### Create Simple Unit Test

Create file: `app/src/test/java/com/example/studentcopilot/DateUtilTest.kt`

```kotlin
package com.example.studentcopilot

import com.example.studentcopilot.util.DateUtil
import org.junit.Test
import org.junit.Assert.*

class DateUtilTest {
    @Test
    fun testFormatDate() {
        val timestamp = 1704067200000L
        val result = DateUtil.formatDate(timestamp)
        assertNotNull(result)
        assertFalse(result.contains("Invalid"))
    }

    @Test
    fun testIsOverdue() {
        val pastTime = System.currentTimeMillis() - 86400000 // 1 day ago
        val futureTime = System.currentTimeMillis() + 86400000 // 1 day from now

        assertTrue(DateUtil.isOverdue(pastTime))
        assertFalse(DateUtil.isOverdue(futureTime))
    }
}
```

Run test:
```bash
./gradlew test --tests "*.DateUtilTest"
```

---

## Debugging

### Enable Logcat

1. **Android Studio**
   - View → Tool Windows → Logcat
   - Filter by your app: `com.example.studentcopilot`

2. **Filter Logs**
   ```
   # Show only errors
   Level: Error

   # Show specific tag
   Tag: ROOM
   ```

### Add Debug Logs

```kotlin
// In any class
import android.util.Log

// Add to code
Log.d("DashboardViewModel", "Study plan clicked")
Log.e("API", "Error: ${exception.message}")
```

View in Logcat:
```
D/DashboardViewModel: Study plan clicked
E/API: Error: Network timeout
```

### Breakpoint Debugging

1. **Set Breakpoint**
   - Click line number in editor
   - Red dot appears

2. **Run in Debug Mode**
   - Run → Debug 'app'
   - Or Shift + F9

3. **Debug Controls**
   - Step Over (F10)
   - Step Into (F11)
   - Resume Program (F8)

4. **Inspect Variables**
   - Hover over variable
   - Or use Variables panel

---

## Performance Testing

### Monitor Memory Usage

1. **Device Monitor**
   - View → Tool Windows → Profiler
   - Select Memory tab
   - Watch usage while:
     - Adding courses
     - Adding assignments
     - Generating study plans

2. **Expected**
   - Memory: 50-150 MB
   - No memory leaks
   - Stable when idle

### Check Frame Rate

1. **Enable Debug GPU Overdraw**
   - Device Settings → Developer Options
   - Debug GPU Overdraw: Show overdraw areas

2. **Open App**
   - Green areas: Good
   - Red areas: Overdraw (slow rendering)

---

## Common Issues & Fixes

### Issue 1: App Crashes on Launch

**Symptoms**: Red error message, app closes immediately

**Solutions**:
```bash
# 1. Clean build
./gradlew clean build

# 2. Clear app data
adb shell pm clear com.example.studentcopilot

# 3. Reinstall
./gradlew installDebug

# 4. Check Logcat
# View → Tool Windows → Logcat
# Look for red error messages
```

---

### Issue 2: Study Plan Returns Empty

**Symptoms**: Button clicked but no plan appears

**Solutions**:
1. Check internet connection
2. Verify API is live:
   ```bash
   curl https://studyplanner.qybrrlabs.frica/api/study-plan
   ```
3. Check Logcat for network errors
4. Ensure you have assignments/exams added

---

### Issue 3: Date Picker Doesn't Work

**Symptoms**: Can't pick date, numbers won't change

**Solutions**:
1. Clear app cache
2. Try different date values
3. Check Logcat for validation errors

---

### Issue 4: App Too Slow

**Symptoms**: Slow navigation, lag when typing

**Solutions**:
1. Check Profiler (see Performance Testing)
2. Reduce number of items in database
3. Clear app data and start fresh

---

### Issue 5: Database Not Updating

**Symptoms**: Add course but doesn't appear after restart

**Solutions**:
```bash
# Check database is being written
adb shell

# In shell
cd /data/data/com.example.studentcopilot/databases/
ls -la

# Should show: student_copilot_database
```

---

## Testing Checklist

### Functionality
- [ ] Add course → appears in list
- [ ] Add assignment → appears in list
- [ ] Add exam → appears in list
- [ ] Generate study plan → shows text
- [ ] Navigate all tabs → data persists
- [ ] Empty states show correctly
- [ ] Dialogs open/close properly
- [ ] Form validation works

### API
- [ ] Study plan endpoint responds
- [ ] Empty payload handled
- [ ] CORS headers present
- [ ] Response format correct

### Database
- [ ] Data saves to database
- [ ] Data persists after restart
- [ ] Multiple items stored
- [ ] No duplicate entries

### UI/UX
- [ ] All buttons clickable
- [ ] Text readable
- [ ] Colors correct
- [ ] Navigation intuitive
- [ ] No layout issues

### Performance
- [ ] App launches in < 3 seconds
- [ ] Navigation smooth (no lag)
- [ ] Memory usage < 150 MB
- [ ] No crashes

---

## Test Report Template

```
Test Date: [Date]
Build: [Version]
Device: [Model/Emulator]
Android: [API Level]

PASSED:
- [ ] Test 1: Add Course
- [ ] Test 2: Add Assignment
- [ ] Test 3: Add Exam
- [ ] Test 4: Generate Study Plan

FAILED:
- [ ] [Issue description]
- [ ] [Error message from Logcat]

NOTES:
[Any observations or unusual behavior]
```

---

## Quick Test Commands

```bash
# Build
./gradlew build

# Run tests
./gradlew test

# Install app
./gradlew installDebug

# View logs
adb logcat com.example.studentcopilot:V *:S

# Clear app data
adb shell pm clear com.example.studentcopilot

# Uninstall
adb uninstall com.example.studentcopilot
```

---

<div align="center">

**Happy Testing! 🧪**

For help, check logcat or review [README.md](README.md)

</div>
