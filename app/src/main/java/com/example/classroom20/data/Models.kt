package com.example.classroom20.data

import java.io.Serializable

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    var role: String = "", // "student" or "teacher"
    val profileImageUrl: String = "",
    val joinedSubjects: List<String> = emptyList(),
    val language: String = "bs"
) : Serializable

data class Subject(
    val id: String = "",
    val name: String = "",
    val teacherId: String = "",
    val teacherName: String = "",
    val classCode: String = "",
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

data class Homework(
    val id: String = "",
    val subjectId: String = "",
    val title: String = "",
    val description: String = "",
    val dueDate: Long = 0,
    val teacherId: String = ""
) : Serializable

data class HomeworkSubmission(
    val id: String = "",
    val homeworkId: String = "",
    val studentUid: String = "",
    val studentName: String = "",
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val feedback: String = ""
) : Serializable

data class Grade(
    val id: String = "",
    val studentUid: String = "",
    val subjectId: String = "",
    val subjectName: String = "",
    val value: Int = 0,
    val teacherName: String = "",
    val date: String = "",
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

data class Quiz(
    val id: String = "",
    val subjectId: String = "",
    val teacherId: String = "",
    val title: String = "",
    val questions: List<Question> = emptyList(),
    val durationSeconds: Int = 60,
    val startTime: Long = 0,
    val isActive: Boolean = false
) : Serializable

data class Question(
    val text: String = "",
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int = 0
) : Serializable

data class QuizResult(
    val studentUid: String = "",
    val studentName: String = "",
    val quizId: String = "",
    val subjectId: String = "",
    val score: Int = 0,
    val totalQuestions: Int = 0,
    val timeTakenSeconds: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

data class Feedback(
    val teacherUid: String = "",
    val teacherName: String = "",
    val type: String = "Pohvala",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

data class AttendanceRecord(
    val studentUid: String = "",
    val subjectId: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

data class Material(
    val id: String = "",
    val subjectId: String = "",
    val title: String = "",
    val fileUrl: String = "",
    val fileName: String = "",
    val timestamp: Long = System.currentTimeMillis()
) : Serializable
