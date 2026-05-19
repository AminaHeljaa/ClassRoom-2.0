package com.example.classroom20.util

import androidx.compose.runtime.*

object Strings {
    private val en = mapOf(
        "welcome" to "Welcome Back!",
        "signin_continue" to "Sign in to continue",
        "email" to "Email",
        "password" to "Password",
        "forgot_password" to "Forgot password?",
        "signin" to "Sign In",
        "dont_have_account" to "Don't have an account?",
        "register" to "Register",
        "choose_role" to "Choose Your Role",
        "teacher" to "I'm a Teacher",
        "student" to "I'm a Student",
        "qr_attendance" to "QR Attendance",
        "live_quiz" to "Live Quiz",
        "homework" to "Homework",
        "leaderboard" to "Leaderboard",
        "random_picker" to "Random Picker",
        "ai_assistant" to "AI Assistant",
        "generate_qr" to "Generate QR Code",
        "scan_qr" to "Scan QR Code",
        "next" to "Next",
        "done" to "Done",
        "pick_again" to "Pick Again",
        "ask_anything" to "Ask anything...",
        "language" to "Language",
        "profile" to "Profile",
        "logout" to "Logout"
    )

    private val bs = mapOf(
        "welcome" to "Dobrodošli nazad!",
        "signin_continue" to "Prijavite se za nastavak",
        "email" to "E-mail",
        "password" to "Lozinka",
        "forgot_password" to "Zaboravili ste lozinku?",
        "signin" to "Prijavi se",
        "dont_have_account" to "Nemate račun?",
        "register" to "Registruj se",
        "choose_role" to "Odaberite ulogu",
        "teacher" to "Ja sam Profesor",
        "student" to "Ja sam Student",
        "qr_attendance" to "QR Prisutnost",
        "live_quiz" to "Kviz uživo",
        "homework" to "Zadaća",
        "leaderboard" to "Rang lista",
        "random_picker" to "Nasumični odabir",
        "ai_assistant" to "AI Asistent",
        "generate_qr" to "Generiši QR Kod",
        "scan_qr" to "Skeniraj QR Kod",
        "next" to "Dalje",
        "done" to "Gotovo",
        "pick_again" to "Biraj ponovo",
        "ask_anything" to "Pitaj bilo šta...",
        "language" to "Jezik",
        "profile" to "Profil",
        "logout" to "Odjava"
    )

    var currentLanguage by mutableStateOf("en")

    fun get(key: String): String {
        return if (currentLanguage == "bs") bs[key] ?: key else en[key] ?: key
    }
}

@Composable
fun t(key: String): String {
    return Strings.get(key)
}
