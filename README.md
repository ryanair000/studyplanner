# 📚 Student Copilot - Academic Study Planner

> A complete Android MVP with AI-powered study plan generation. Built with **Jetpack Compose**, **Room Database**, **Retrofit**, and **Netlify Functions**.

![GitHub](https://img.shields.io/badge/GitHub-ryanair000%2Fstudyplanner-blue)
![Status](https://img.shields.io/badge/Status-MVP-green)
![Android](https://img.shields.io/badge/Android-34%2B-green)
![API](https://img.shields.io/badge/API-Netlify%20Functions-blue)

---

## 🎯 Features

### 📱 Android App
- **Dashboard** - View upcoming assignments & exams with study plan generation
- **Courses** - Manage your enrolled courses
- **Assignments** - Track assignment deadlines and status
- **Exams** - Schedule and manage exam dates
- **Study Plan Generator** - AI-generated personalized study plans
- **Material Design 3** - Modern, responsive UI
- **Offline-first** - All data stored locally in Room Database
- **Bottom Navigation** - Easy access to all sections

### 🚀 API Backend
- **Netlify Functions** - Serverless TypeScript API
- **Study Plan Generation** - Smart task prioritization
- **CORS-enabled** - Works with mobile apps
- **Custom Domain** - `studyplanner.qybrrlabs.frica`

---

## 🏗️ Architecture

### Clean Architecture Layers

```
┌─────────────────────────────────────┐
│         UI Layer (Compose)          │
│  ├─ Screens (Dashboard, Courses...)  │
│  ├─ ViewModels (MVVM)               │
│  ├─ Components (Reusable UI)        │
│  └─ Navigation (Bottom Nav)         │
├─────────────────────────────────────┤
│      Domain Layer (Models)          │
│  ├─ Course                          │
│  ├─ Assignment                      │
│  └─ Exam                            │
├─────────────────────────────────────┤
│      Data Layer                     │
│  ├─ Local: Room Database            │
│  ├─ Remote: Retrofit + Netlify      │
│  └─ Repository: Data aggregation    │
└─────────────────────────────────────┘
```

### Tech Stack

| Component | Technology |
|-----------|-----------|
| **UI Framework** | Jetpack Compose |
| **Design System** | Material Design 3 |
| **Local Database** | Room (SQLite) |
| **HTTP Client** | Retrofit + OkHttp |
| **JSON Serialization** | Gson |
| **Async** | Coroutines + Flow |
| **Dependency Injection** | Manual (Factory Pattern) |
| **Navigation** | Navigation Compose |
| **Backend** | Netlify Functions |
| **Language** | Kotlin 100% |

---

## 📂 Project Structure

```
StudentCopilot/
├── app/
│   ├── src/main/java/com/example/studentcopilot/
│   │   ├── MainActivity.kt                    (Entry point)
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── entity/                   (Room entities)
│   │   │   │   ├── dao/                      (Database access)
│   │   │   │   └── database/AppDatabase.kt   (DB singleton)
│   │   │   ├── remote/
│   │   │   │   ├── api/ApiService.kt         (Retrofit interface)
│   │   │   │   ├── api/ApiClient.kt          (HTTP client setup)
│   │   │   │   └── dto/                      (API models)
│   │   │   └── repository/AppRepository.kt   (Data layer)
│   │   ├── domain/
│   │   │   └── model/                        (Kotlin data classes)
│   │   ├── ui/
│   │   │   ├── screens/                      (4 main screens)
│   │   │   ├── viewmodel/                    (MVVM ViewModels)
│   │   │   ├── components/Components.kt      (Reusable UI)
│   │   │   ├── navigation/Navigation.kt      (Routing)
│   │   │   └── theme/Theme.kt                (Material Design 3)
│   │   └── util/DateUtil.kt                  (Utilities)
│   └── src/main/res/                         (Android resources)
├── functions/
│   └── study-plan.ts                         (Netlify Function)
├── netlify.toml                              (Netlify config)
├── package.json                              (Node dependencies)
├── build.gradle.kts                          (Gradle build)
└── README.md                                 (This file)
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Flamingo or later
- Java 11+
- Gradle 8.0+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/ryanair000/studyplanner.git
   cd StudyPlanner
   ```

2. **Open in Android Studio**
   ```bash
   # Android Studio will auto-detect and sync Gradle
   ```

3. **Build the app**
   ```bash
   # Via Android Studio: Build → Make Project
   # Or via CLI:
   ./gradlew build
   ```

4. **Run on emulator/device**
   ```bash
   ./gradlew installDebug
   ```

---

## 📡 API Documentation

### Endpoint: Study Plan Generation

**URL**: `POST https://studyplanner.qybrrlabs.frica/api/study-plan`

**Request Body**:
```json
{
  "assignments": [
    {
      "id": 1,
      "title": "Chapter 5 Exercises",
      "course_name": "Calculus I",
      "due_date": 1704067200000
    }
  ],
  "exams": [
    {
      "id": 1,
      "title": "Midterm Exam",
      "course_name": "Calculus I",
      "exam_date": 1704153600000
    }
  ]
}
```

**Response**:
```json
{
  "study_plan": "📚 YOUR PERSONALIZED STUDY PLAN\n\n🔴 URGENT (Due within 7 days):\n  • [ASSIGNMENT] Chapter 5 Exercises (Calculus I)\n    Due in 2 days\n\n💡 STUDY TIPS:\n  • Tackle urgent tasks first\n  ...",
  "status": "success"
}
```

### Netlify Function

Located at: `functions/study-plan.ts`

**Features**:
- Analyzes assignments and exams
- Prioritizes tasks by deadline
- Generates personalized study plan
- Returns markdown-formatted text

---

## 📱 App Screens

### Dashboard
- Displays upcoming tasks (next 14 days)
- Shows nearest deadline
- Summary statistics
- Study plan generator button
- Displays generated study plan with tips

### Courses
- List of all courses
- Add new course dialog
- Tap to view course details

### Assignments
- View all assignments with due dates
- Add new assignment with date picker
- Course selection dropdown
- Status indicators (overdue, completed)

### Exams
- Exam schedule view
- Add exam with date picker
- Course association
- Countdown to exam date

---

## 🔧 Configuration

### API Endpoint
Edit `app/src/main/java/com/example/studentcopilot/data/remote/api/ApiClient.kt`:

```kotlin
private const val BASE_URL = "https://studyplanner.qybrrlabs.frica/"
```

### Database
- **Name**: `student_copilot_database`
- **Type**: Room (SQLite)
- **Location**: Device internal storage

### Permissions
Required in `AndroidManifest.xml`:
- `android.permission.INTERNET` - API calls

---

## 📊 Database Schema

### courses
```sql
CREATE TABLE courses (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name TEXT NOT NULL
)
```

### assignments
```sql
CREATE TABLE assignments (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  courseId INTEGER NOT NULL,
  dueDate INTEGER NOT NULL,
  completed BOOLEAN DEFAULT 0,
  FOREIGN KEY(courseId) REFERENCES courses(id) ON DELETE CASCADE
)
```

### exams
```sql
CREATE TABLE exams (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  title TEXT NOT NULL,
  courseId INTEGER NOT NULL,
  examDate INTEGER NOT NULL,
  FOREIGN KEY(courseId) REFERENCES courses(id) ON DELETE CASCADE
)
```

---

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumentation Tests
```bash
./gradlew connectedAndroidTest
```

### API Testing
```bash
curl -X POST https://studyplanner.qybrrlabs.frica/api/study-plan \
  -H "Content-Type: application/json" \
  -d '{
    "assignments": [{"id": 1, "title": "HW1", "course_name": "Math", "due_date": 1704067200000}],
    "exams": []
  }'
```

---

## 🚀 Deployment

### Mobile App
1. Build signed APK: `./gradlew bundleRelease`
2. Upload to Google Play Store or Firebase App Distribution
3. Create release tag: `git tag -a v1.0 -m "Initial release"`

### Backend API
Deploys automatically when you push to the repository:

```bash
git push origin main
# Netlify automatically builds and deploys
```

View deployment status: https://app.netlify.com/projects/luminous-pithivier-ef07aa

---

## 📝 Dependencies

### Android (build.gradle.kts)
- **androidx.compose:compose-bom:2023.10.01**
- **androidx.room:room-runtime:2.6.1**
- **androidx.navigation:navigation-compose:2.7.4**
- **com.squareup.retrofit2:retrofit:2.10.0**
- **com.squareup.okhttp3:okhttp:4.11.0**
- **com.google.code.gson:gson:2.10.1**

### Node.js (package.json)
- **@netlify/functions:^2.4.1**

---

## 🐛 Troubleshooting

### App crashes on startup
- Clear app cache: Settings → Apps → Student Copilot → Clear Cache
- Ensure Internet permission is granted
- Check API endpoint is reachable

### API returns 404
- Verify custom domain is set up: `studyplanner.qybrrlabs.frica`
- Check Netlify deployment status
- Ensure redirect in `netlify.toml` is correct

### Database migration errors
- Delete app and reinstall
- Clear Gradle cache: `./gradlew clean`

### Study plan takes long to generate
- Check network connection
- Monitor API logs at Netlify dashboard

---

## 📚 Resources

- [Android Developer Docs](https://developer.android.com)
- [Jetpack Compose Guide](https://developer.android.com/jetpack/compose)
- [Room Database Tutorial](https://developer.android.com/training/data-storage/room)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Netlify Functions](https://docs.netlify.com/functions/overview/)

---

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

---

## 👤 Author

**StudentCopilot MVP** - Built with Claude AI

- 📱 Android App: 30 Kotlin files
- 🚀 API Backend: Netlify Functions
- 🎨 UI Design: Material Design 3
- 📊 Database: Room + SQLite

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit changes: `git commit -m 'Add amazing feature'`
4. Push to branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 💬 Support

For issues and questions:
- GitHub Issues: https://github.com/ryanair000/studyplanner/issues
- GitHub Discussions: https://github.com/ryanair000/studyplanner/discussions

---

## 🔗 Links

- **GitHub Repository**: https://github.com/ryanair000/studyplanner
- **API Endpoint**: https://studyplanner.qybrrlabs.frica
- **Netlify Admin**: https://app.netlify.com/projects/luminous-pithivier-ef07aa
- **Documentation**: See [API_SETUP.md](API_SETUP.md)

---

<div align="center">

**Made with ❤️ by Claude AI**

Built on March 30, 2026

</div>
