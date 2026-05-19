package com.example.classroom20.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom20.R
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Question
import com.example.classroom20.data.Quiz
import com.example.classroom20.data.Subject
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuizScreen(
    activeSubject: Subject?,
    onBack: () -> Unit
) {

    var quizTitle by remember {
        mutableStateOf("")
    }

    var durationMinutes by remember {
        mutableStateOf("10")
    }

    val questions = remember {

        mutableStateListOf(
            Question(
                text = "",
                options = listOf("", "", "", ""),
                correctAnswerIndex = 0
            )
        )
    }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isSystemInDarkTheme()) {
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8FAFF),
                            Color(0xFFF3F4FF),
                            Color.White
                        )
                    )
                }
            )
    ) {

        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = (-100).dp, y = (-100).dp)
                .background(
                    PrimaryPurple.copy(alpha = 0.05f),
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 80.dp, y = 80.dp)
                .background(
                    Color(0xFF60A5FA).copy(alpha = 0.07f),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            AppHeader(
                title = "Kreiraj kviz",
                onBack = onBack
            )

            Text(
                text = activeSubject?.name ?: "",

                color = PrimaryPurple,

                fontSize = 13.sp,

                fontWeight = FontWeight.SemiBold,

                modifier = Modifier.padding(bottom = 26.dp)
            )

            if (activeSubject == null) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = stringResource(
                            R.string.select_subject_dashboard
                        ),

                        color = Color.Gray
                    )
                }

            } else {

                QuizInfoCard()

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = quizTitle,

                    onValueChange = {
                        quizTitle = it
                    },

                    label = {
                        Text("Naziv kviza")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    leadingIcon = {

                        Icon(
                            Icons.Default.Quiz,
                            contentDescription = null
                        )
                    },

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = durationMinutes,

                    onValueChange = {
                        durationMinutes = it
                    },

                    label = {
                        Text("Trajanje u minutama")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    leadingIcon = {

                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null
                        )
                    },

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Pitanja",

                    fontSize = 22.sp,

                    fontWeight = FontWeight.ExtraBold,

                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                questions.forEachIndexed { index, question ->

                    QuestionEditorItem(
                        index = index,
                        question = question,

                        onUpdate = { updated ->
                            questions[index] = updated
                        },

                        onRemove = {

                            if (questions.size > 1) {

                                questions.removeAt(index)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))
                }

                Button(

                    onClick = {

                        questions.add(
                            Question(
                                text = "",
                                options = listOf("", "", "", ""),
                                correctAnswerIndex = 0
                            )
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                            PrimaryPurple.copy(alpha = 0.10f),

                        contentColor = PrimaryPurple
                    ),

                    shape = RoundedCornerShape(18.dp),

                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp
                    )
                ) {

                    Icon(
                        Icons.Default.Add,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Dodaj pitanje",

                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(

                    onClick = {

                        if (
                            quizTitle.isNotBlank()
                            && questions.all {
                                it.text.isNotBlank()
                            }
                        ) {

                            val quiz = Quiz(
                                subjectId = activeSubject.id,

                                teacherId =
                                    FirebaseManager.getCurrentUserUid()
                                        ?: "",

                                title = quizTitle,

                                questions = questions.toList(),

                                isActive = true,

                                durationSeconds =
                                    (durationMinutes.toIntOrNull()
                                        ?: 10) * 60,

                                startTime =
                                    System.currentTimeMillis()
                            )

                            FirebaseManager.createQuiz(
                                quiz
                            ) { success ->

                                if (success) {

                                    Toast.makeText(
                                        context,
                                        "Kviz uspješno objavljen!",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    onBack()

                                } else {

                                    Toast.makeText(
                                        context,
                                        "Greška pri objavi.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                        } else {

                            Toast.makeText(
                                context,
                                "Popunite sva polja.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    ),

                    shape = RoundedCornerShape(20.dp)
                ) {

                    Icon(
                        Icons.Default.Publish,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Objavi kviz",

                        color = Color.White,

                        fontWeight = FontWeight.Bold,

                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
fun QuizInfoCard() {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(
            containerColor = PrimaryPurple
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(54.dp),

                    shape = RoundedCornerShape(16.dp),

                    color = Color.White.copy(alpha = 0.15f)
                ) {

                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,

                        tint = Color.White,

                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {

                    Text(
                        text = "Pametni kviz",

                        color = Color.White,

                        fontWeight = FontWeight.Bold,

                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Dodaj pitanja i objavi kviz studentima.",

                        color = Color.White.copy(alpha = 0.85f),

                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionEditorItem(
    index: Int,
    question: Question,
    onUpdate: (Question) -> Unit,
    onRemove: () -> Unit
) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(46.dp),

                    shape = CircleShape,

                    color = PrimaryPurple.copy(alpha = 0.10f)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "${index + 1}",

                            color = PrimaryPurple,

                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "Pitanje ${index + 1}",

                    fontSize = 20.sp,

                    fontWeight = FontWeight.Bold,

                    modifier = Modifier.weight(1f),

                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(
                    onClick = onRemove
                ) {

                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,

                        tint = Color.Red.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedTextField(
                value = question.text,

                onValueChange = {

                    onUpdate(
                        question.copy(text = it)
                    )
                },

                label = {
                    Text("Tekst pitanja")
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(18.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            question.options.forEachIndexed { optIndex, option ->

                Surface(

                    shape = RoundedCornerShape(18.dp),

                    color = if (isSystemInDarkTheme())
                        MaterialTheme.colorScheme.background
                    else
                        Color(0xFFF8FAFF),

                    tonalElevation = 1.dp,

                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier.padding(12.dp),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        RadioButton(

                            selected =
                                question.correctAnswerIndex ==
                                        optIndex,

                            onClick = {

                                onUpdate(
                                    question.copy(
                                        correctAnswerIndex =
                                            optIndex
                                    )
                                )
                            },

                            colors = RadioButtonDefaults.colors(
                                selectedColor = PrimaryPurple
                            )
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        OutlinedTextField(

                            value = option,

                            onValueChange = {

                                val newOpts =
                                    question.options.toMutableList()

                                newOpts[optIndex] = it

                                onUpdate(
                                    question.copy(
                                        options = newOpts
                                    )
                                )
                            },

                            label = {

                                Text(
                                    "Odgovor ${('A' + optIndex)}"
                                )
                            },

                            modifier = Modifier.weight(1f),

                            shape = RoundedCornerShape(14.dp),

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}