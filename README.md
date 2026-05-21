# 🎓 ClassRoom 2.0 – Interactive Classroom Management Platform

<p align="center">
  <img src="screenshots/intro.jpg" width="220"/>
</p>

<p align="center">
Modern Android application for digital classroom management, QR attendance tracking, live quizzes, AI assistance, and interactive real-time learning.
</p>

---

# 🚀 About The Project

ClassRoom 2.0 is a modern Android application developed in Kotlin using Jetpack Compose, designed as a closed educational system for schools and universities.

The application allows professors to:
- manage classes digitally,
- generate QR attendance sessions,
- create live quizzes,
- monitor student performance,
- receive anonymous feedback,
- use AI-powered teaching assistance.

Students can:
- check attendance using QR scanning,
- participate in live quizzes,
- track grades and points,
- use AI learning assistance,
- manage enrolled subjects and assignments.

---

# 📱 Screenshots

## 🔐 Authentication & Onboarding

<p align="center">
  <img src="screenshots/intro.jpg" width="220"/>
  <img src="screenshots/loginpage.jpg" width="220"/>
  <img src="screenshots/registerPage.jpg" width="220"/>
  <img src="screenshots/roleSelection.jpg" width="220"/>
</p>

---

## 👨‍🏫 Dashboard Screens

<p align="center">
  <img src="screenshots/profesorDashBoard.jpg" width="220"/>
  <img src="screenshots/StudentDashboard.jpg" width="220"/>
  <img src="screenshots/dashboard2.jpg" width="220"/>
  <img src="screenshots/DashboardDark.jpg" width="220"/>
</p>

---

## 📸 QR Attendance & Random Picker

<p align="center">
  <img src="screenshots/QRprisustvo.jpg" width="220"/>
  <img src="screenshots/RandomPicker.jpg" width="220"/>
</p>

---

## 🏆 Live Quiz & Rankings

<p align="center">
  <img src="screenshots/LiveQuy.jpg" width="220"/>
  <img src="screenshots/RangList.jpg" width="220"/>
  <img src="screenshots/E-dnevnik.jpg" width="220"/>
</p>

---

## 💬 Feedback & Profile

<p align="center">
  <img src="screenshots/Feedback.jpg" width="220"/>
  <img src="screenshots/profilePage.jpg" width="220"/>
  <img src="screenshots/profileDark.jpg" width="220"/>
</p>

---

## 🤖 AI Assistant

<p align="center">
  <img src="screenshots/AlasistentLight.jpg" width="220"/>
</p>

---

# ✨ Main Features

## 🔐 Authentication & User Roles

During registration, users select their role using a unique institution code:

- Professor → `PROF2026`
- Student → `STUD2026`

The application automatically remembers the user session and supports automatic login on app restart.

---

## 📚 Subject Management

### Professors can:
- create multiple subjects,
- generate unique subject codes,
- delete subjects,
- manage students.

### Students can:
- join subjects using a code,
- add new subjects,
- view enrolled classes.

---

## 📸 Real-Time QR Attendance

Professors generate QR codes valid for 5 minutes.

Students:
- scan the QR code,
- confirm attendance,
- automatically receive attendance records.

The system uses:
- Google ML Kit,
- Firebase Firestore,
- session expiration validation.

---

## 🏆 Live Quizzes & E-Diary

Professors can:
- create quizzes,
- add questions and answers,
- monitor results in real time.

Students can:
- participate in quizzes,
- track points,
- view rankings and statistics.

### E-Diary includes:
- grades overview,
- quiz statistics,
- professor comments,
- activity history.

---

## 🤖 Groq AI Assistant

The application integrates the Groq API for AI-powered learning assistance.

The AI assistant helps students with:
- explaining concepts,
- answering questions,
- learning support in real time.

---

## 📬 Assignments, Feedback & Materials

### Assignments
Professors can:
- create assignments,
- add titles and descriptions,
- delete assignments.

### Anonymous Feedback
Students can anonymously send:
- compliments,
- suggestions,
- complaints.

### Materials
A dedicated materials section is already implemented in the UI and prepared for future Firebase Storage integration.

---

# 🌙 Dark Mode

The application fully supports:
- Light Mode
- Dark Mode

The entire interface automatically adapts to the selected theme.

---

# ⚠️ Current Development Status

## 🔐 Authentication
Google and Facebook authentication are currently not fully functional. The system currently relies on internal Email/Password registration.

## 📂 File Uploads
Features such as:
- material uploads,
- assignment submissions,
- profile picture saving

are currently implemented only visually due to the lack of Firebase Storage integration.

## 📚 Materials
The "Materials" tab is fully designed and prepared for future upgrades but currently does not fetch real documents from the server.

---

# 🛠️ Technologies & Tools

| Technology | Purpose |
|---|---|
| Kotlin | Programming Language |
| Jetpack Compose | Modern Android UI |
| Firebase Firestore | Cloud Database |
| Firebase Authentication | Authentication |
| Google ML Kit | QR Scanning |
| Groq API | AI Assistant |
| Navigation Compose | Navigation |
| Material 3 | UI Design System |
| Coil | Image Loading |

---

# 🧱 Project Structure

```plaintext
com.example.classroom20
│
├── screens
├── navigation
├── components
├── models
├── firebase
├── utils
├── ui.theme
└── managers
```

> The project currently does not fully implement MVVM architecture throughout the entire system. The codebase is organized using screens, helper classes, and Firebase managers to simplify maintenance and future scalability.

---

# ⚙️ Running The Project

## 1️⃣ Clone Repository

```bash
git clone https://github.com/AminaHeljaa/ClassRoom-2.0.git
```

---

## 2️⃣ Open in Android Studio

- Android Studio Ladybug or newer
- Open Project
- Wait for Gradle Sync

---

## 3️⃣ Firebase Configuration

You need to:
- create a Firebase project,
- register an Android application,
- download `google-services.json`,
- place the file inside the `app/` folder.

### Enable:
- Firebase Authentication
- Firestore Database

---

## 4️⃣ Run The App

- Start an emulator or physical Android device
- Click ▶ Run

### Registration Codes:
- Professor → `PROF2026`
- Student → `STUD2026`

---

# 🔮 Future Improvements

Planned features:
- Firebase Storage integration
- PDF & PowerPoint uploads
- Push notifications
- Full MVVM architecture
- Google/Facebook authentication
- Attendance analytics
- Advanced AI tutoring system

---

# 👩‍💻 Author

Developed by:

## Amina Helja

Android application developed using:
- Kotlin
- Jetpack Compose
- Firebase
- Groq AI Integration

---

