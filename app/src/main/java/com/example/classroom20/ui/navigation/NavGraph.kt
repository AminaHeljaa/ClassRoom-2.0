package com.example.classroom20.ui.navigation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Quiz
import com.example.classroom20.data.Subject
import com.example.classroom20.data.User
import com.example.classroom20.ui.screens.*

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Login : Screen("login")
    object Register : Screen("register")
    object RoleSelection : Screen("role_selection")
    object Dashboard : Screen("dashboard")
    object QRAttendance : Screen("qr_attendance")
    object AttendanceConfirmed : Screen("attendance_confirmed")
    object LiveQuiz : Screen("live_quiz")
    object RandomPicker : Screen("random_picker")
    object AIAssistant : Screen("ai_assistant")
    object Leaderboard : Screen("leaderboard")
    object Homework : Screen("homework")
    object Grades : Screen("grades")
    object Feedback : Screen("feedback")
    object CreateQuiz : Screen("create_quiz")
    object Materials : Screen("materials")
}

@Composable
fun ClassroomNavGraph(navController: NavHostController) {
    var activeLiveQuiz by remember { mutableStateOf<Quiz?>(null) }
    var currentUser by remember { mutableStateOf<User?>(null) }
    var userSubjects by remember { mutableStateOf<List<Subject>>(emptyList()) }

    LaunchedEffect(Unit) {
        FirebaseManager.getCurrentUser { user ->
            currentUser = user
            if (user?.role == "student" && user.joinedSubjects.isNotEmpty()) {
                FirebaseManager.listenForActiveQuizzes(user.joinedSubjects) { quiz ->
                    // Only show alert if we are not already in a quiz or create quiz screen
                    val currentRoute = navController.currentDestination?.route
                    if (quiz != null && currentRoute != Screen.LiveQuiz.route && currentRoute != Screen.CreateQuiz.route) {
                        activeLiveQuiz = quiz
                        // Fetch subjects to find the one for the quiz
                        FirebaseManager.getSubjectsForStudent(user.joinedSubjects) { userSubjects = it }
                    }
                }
            }
        }
    }

    if (activeLiveQuiz != null) {
        AlertDialog(
            onDismissRequest = { activeLiveQuiz = null },
            title = { Text("Novi Kviz Uživo!") },
            text = { Text("Profesor je pokrenuo kviz: ${activeLiveQuiz?.title}. Želite li se pridružiti?") },
            confirmButton = {
                Button(onClick = {
                    val quiz = activeLiveQuiz
                    activeLiveQuiz = null
                    val subject = userSubjects.find { it.id == quiz?.subjectId }
                    navController.previousBackStackEntry?.savedStateHandle?.set("activeSubject", subject)
                    navController.navigate(Screen.LiveQuiz.route)
                }) {
                    Text("Pridruži se")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeLiveQuiz = null }) {
                    Text("Zanemari")
                }
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Intro.route
    ) {
        composable(Screen.Intro.route) {
            IntroScreen(
                onTimeout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.RoleSelection.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.RoleSelection.route) {
            RoleSelectionScreen(
                onRoleSelected = { _ ->
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.RoleSelection.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Dashboard.route) {
            val activeSubject = it.savedStateHandle.get<Subject>("activeSubject")
            DashboardScreen(
                onFeatureClick = { featureTitle, currentActiveSubject ->
                    it.savedStateHandle.set("activeSubject", currentActiveSubject)
                    when (featureTitle) {
                        "QR Prisustvo" -> navController.navigate(Screen.QRAttendance.route)
                        "Kviz uživo" -> {
                            FirebaseManager.getCurrentUser { user ->
                                if (user?.role == "teacher") navController.navigate(Screen.CreateQuiz.route)
                                else navController.navigate(Screen.LiveQuiz.route)
                            }
                        }
                        "Nasumični odabir" -> navController.navigate(Screen.RandomPicker.route)
                        "AI Asistent" -> navController.navigate(Screen.AIAssistant.route)
                        "Rang lista" -> navController.navigate(Screen.Leaderboard.route)
                        "Zadaća" -> navController.navigate(Screen.Homework.route)
                        "E-Dnevnik" -> navController.navigate(Screen.Grades.route)
                        "Feedback" -> navController.navigate(Screen.Feedback.route)
                        "Materijali" -> navController.navigate(Screen.Materials.route)
                    }
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                initialActiveSubject = activeSubject
            )
        }
        composable(Screen.QRAttendance.route) {
            val activeSubject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("activeSubject")
            QRAttendanceScreen(
                activeSubject = activeSubject,
                onBack = { navController.popBackStack() },
                onScanSuccess = { scannedSubject -> 
                    navController.currentBackStackEntry?.savedStateHandle?.set("scannedSubject", scannedSubject)
                    navController.navigate(Screen.AttendanceConfirmed.route) 
                }
            )
        }
        composable(Screen.AttendanceConfirmed.route) {
            val subject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("scannedSubject")
            AttendanceConfirmedScreen(
                subject = subject,
                onDone = { navController.popBackStack(Screen.Dashboard.route, inclusive = false) }
            )
        }
        composable(Screen.LiveQuiz.route) {
            val activeSubject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("activeSubject")
            LiveQuizScreen(
                activeSubject = activeSubject,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.CreateQuiz.route) {
            val activeSubject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("activeSubject")
            CreateQuizScreen(
                activeSubject = activeSubject,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.RandomPicker.route) {
            val activeSubject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("activeSubject")
            RandomPickerScreen(
                activeSubject = activeSubject,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AIAssistant.route) {
            AIAssistantScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Leaderboard.route) {
            val activeSubject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("activeSubject")
            LeaderboardScreen(activeSubject = activeSubject, onBack = { navController.popBackStack() })
        }
        composable(Screen.Homework.route) {
            val activeSubject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("activeSubject")
            HomeworkScreen(activeSubject = activeSubject, onBack = { navController.popBackStack() })
        }
        composable(Screen.Grades.route) {
            val activeSubject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("activeSubject")
            GradesScreen(activeSubject = activeSubject, onBack = { navController.popBackStack() })
        }
        composable(Screen.Feedback.route) {
            FeedbackScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Materials.route) {
            val activeSubject = navController.previousBackStackEntry?.savedStateHandle?.get<Subject>("activeSubject")
            MaterialsScreen(
                activeSubject = activeSubject,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
