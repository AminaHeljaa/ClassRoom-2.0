package com.example.classroom20.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.classroom20.R
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Grade
import com.example.classroom20.data.QuizResult
import com.example.classroom20.data.Subject
import com.example.classroom20.data.User
import com.example.classroom20.ui.theme.PrimaryPurple
import com.example.classroom20.ui.theme.ThemeSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesScreen(
    activeSubject: Subject?,
    onBack: () -> Unit
) {

    var user by remember { mutableStateOf<User?>(null) }

    var studentsInSubject by remember {
        mutableStateOf<List<User>>(emptyList())
    }

    var studentGrades by remember {
        mutableStateOf<List<Grade>>(emptyList())
    }

    var quizResults by remember {
        mutableStateOf<List<QuizResult>>(emptyList())
    }

    var showAddGradeDialog by remember {
        mutableStateOf<User?>(null)
    }

    LaunchedEffect(Unit) {

        FirebaseManager.getCurrentUser { fetchedUser ->

            user = fetchedUser

            if (fetchedUser?.role == "student") {

                FirebaseManager.getGradesForStudent(
                    fetchedUser.uid
                ) {
                    studentGrades = it
                }
            }
        }
    }

    LaunchedEffect(activeSubject, user) {

        if (activeSubject != null && user != null) {

            if (user?.role == "teacher") {

                FirebaseManager.getStudentsForSubject(
                    activeSubject.id
                ) {
                    studentsInSubject = it
                }

                FirebaseManager.getQuizResults(
                    activeSubject.id
                ) {
                    quizResults = it
                }
            }
        }
    }

    val refreshData = {

        if (activeSubject != null &&
            user?.role == "teacher"
        ) {

            FirebaseManager.getStudentsForSubject(
                activeSubject.id
            ) {
                studentsInSubject = it
            }

            FirebaseManager.getQuizResults(
                activeSubject.id
            ) {
                quizResults = it
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (ThemeSettings.isDarkTheme) {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F172A),
                            Color(0xFF1E293B)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8FAFF),
                            Color(0xFFF2F5FF),
                            Color.White
                        )
                    )
                }
            )
    ) {

        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-70).dp, y = (-40).dp)
                .background(
                    Color(0xFF8B5CF6).copy(alpha = if (ThemeSettings.isDarkTheme) 0.15f else 0.08f),
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 70.dp, y = 70.dp)
                .background(
                    Color(0xFF2563EB).copy(alpha = if (ThemeSettings.isDarkTheme) 0.12f else 0.05f),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            AppHeader(
                title = stringResource(R.string.e_diary),
                onBack = onBack
            )

            Text(
                text = stringResource(R.string.e_diary_desc),
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            if (user?.role == "teacher" &&
                activeSubject != null
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),

                    shape = RoundedCornerShape(28.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),

                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    )
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF7C3AED),
                                        Color(0xFF2563EB)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {

                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 30.dp, y = (-20).dp)
                                .background(
                                    Color.White.copy(alpha = 0.08f),
                                    CircleShape
                                )
                        )

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {

                            Column {

                                Text(
                                    text = "Aktivni predmet",

                                    color = Color.White.copy(alpha = 0.75f),

                                    fontSize = 16.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = activeSubject.name,

                                    color = Color.White,

                                    fontSize = 30.sp,

                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),

                                color = Color.White.copy(alpha = 0.14f)
                            ) {

                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 10.dp
                                    ),

                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        Icons.Default.Groups,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Text(
                                        text = "${studentsInSubject.size} studenata",

                                        color = Color.White,

                                        fontWeight = FontWeight.Bold,

                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                TeacherStudentsList(
                    students = studentsInSubject,
                    quizResults = quizResults,
                    onAddGrade = {
                        showAddGradeDialog = it
                    }
                )

            } else {

                StudentGradesList(studentGrades)
            }
        }

        if (showAddGradeDialog != null &&
            activeSubject != null
        ) {

            AddGradeDialog(
                student = showAddGradeDialog!!,
                subject = activeSubject,

                onDismiss = {
                    showAddGradeDialog = null
                },

                onGradeAdded = {

                    showAddGradeDialog = null
                    refreshData()
                }
            )
        }
    }
}

@Composable
fun TeacherStudentsList(
    students: List<User>,
    quizResults: List<QuizResult>,
    onAddGrade: (User) -> Unit
) {

    if (students.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                "Nema prijavljenih studenata.",
                color = Color.Gray
            )
        }

    } else {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp),

            contentPadding = PaddingValues(
                bottom = 120.dp
            )
        ) {

            items(students) { student ->

                val studentQuizzes =
                    quizResults.filter {
                        it.studentUid == student.uid
                    }

                val totalPoints =
                    studentQuizzes.sumOf {
                        it.score
                    }

                val latestScore =
                    studentQuizzes.firstOrNull()?.score

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(26.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    ),

                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(62.dp)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF7C3AED),
                                                Color(0xFF2563EB)
                                            )
                                        ),
                                        CircleShape
                                    ),

                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,

                                    tint = Color.White,

                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = "${student.firstName} ${student.lastName}",

                                    fontWeight = FontWeight.ExtraBold,

                                    fontSize = 18.sp,

                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = student.email,

                                    color = Color.Gray,

                                    fontSize = 12.sp
                                )
                            }

                            Surface(
                                modifier = Modifier.size(60.dp),

                                shape = RoundedCornerShape(20.dp),

                                color = Color(0xFF7C3AED),

                                shadowElevation = 6.dp
                            ) {

                                IconButton(
                                    onClick = {
                                        onAddGrade(student)
                                    }
                                ) {

                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null,

                                        tint = Color.White,

                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),

                            horizontalArrangement =
                                Arrangement.SpaceBetween
                        ) {

                            ModernStatCard(
                                title = "Kvizovi",
                                value = studentQuizzes.size.toString(),
                                background = Color(0xFFF4ECFF),
                                textColor = Color(0xFF7C3AED)
                            )

                            ModernStatCard(
                                title = "Bodovi",
                                value = totalPoints.toString(),
                                background = Color(0xFFEAFBF1),
                                textColor = Color(0xFF22C55E)
                            )

                            ModernStatCard(
                                title = "Zadnji",
                                value = latestScore?.toString() ?: "-",
                                background = Color(0xFFFFF7E8),
                                textColor = Color(0xFFF59E0B)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernStatCard(
    title: String,
    value: String,
    background: Color,
    textColor: Color
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(background)
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {

        Text(
            text = title,

            color = Color.Gray,

            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = value,

            color = textColor,

            fontWeight = FontWeight.ExtraBold,

            fontSize = 22.sp
        )
    }
}

@Composable
fun StudentGradesList(
    grades: List<Grade>
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        items(grades) { grade ->

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(24.dp),

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {

                Row(
                    modifier = Modifier.padding(18.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            grade.subjectName,

                            fontWeight = FontWeight.Bold,

                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            grade.date,

                            color = Color.Gray,

                            fontSize = 12.sp
                        )

                        if (grade.comment.isNotEmpty()) {

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = grade.comment,

                                color = PrimaryPurple,

                                fontStyle = FontStyle.Italic,

                                fontSize = 12.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(58.dp)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF7C3AED),
                                        Color(0xFF2563EB)
                                    )
                                ),
                                CircleShape
                            ),

                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = grade.value.toString(),

                            color = Color.White,

                            fontWeight = FontWeight.ExtraBold,

                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddGradeDialog(
    student: User,
    subject: Subject,
    onDismiss: () -> Unit,
    onGradeAdded: () -> Unit
) {

    var gradeValue by remember {
        mutableStateOf("")
    }

    var comment by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf(
            java.text.SimpleDateFormat(
                "dd.MM.yyyy",
                java.util.Locale.getDefault()
            ).format(java.util.Date())
        )
    }

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,

        shape = RoundedCornerShape(24.dp),

        title = {

            Text(
                "Upiši ocjenu",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column {

                Text(
                    "${student.firstName} ${student.lastName}",
                    color = PrimaryPurple
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = gradeValue,
                    onValueChange = {
                        gradeValue = it
                    },

                    label = {
                        Text("Ocjena 1-5")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = comment,
                    onValueChange = {
                        comment = it
                    },

                    label = {
                        Text("Komentar")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        date = it
                    },

                    label = {
                        Text("Datum")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(16.dp)
                )
            }
        },

        confirmButton = {

            Button(
                onClick = {

                    val value =
                        gradeValue.toIntOrNull()

                    if (value != null &&
                        value in 1..5
                    ) {

                        val grade = Grade(
                            studentUid = student.uid,
                            subjectId = subject.id,
                            subjectName = subject.name,
                            value = value,
                            date = date,
                            comment = comment,
                            teacherName = subject.teacherName
                        )

                        FirebaseManager.addGrade(
                            grade
                        ) { success ->

                            if (success) {

                                Toast.makeText(
                                    context,
                                    "Ocjena uspješno upisana!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                onGradeAdded()
                            }
                        }
                    }
                },

                shape = RoundedCornerShape(16.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple
                )
            ) {

                Text("Spremi")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Otkaži")
            }
        }
    )
}