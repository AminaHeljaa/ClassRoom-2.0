package com.example.classroom20.ui.screens

import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom20.R
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Quiz
import com.example.classroom20.data.QuizResult
import com.example.classroom20.data.Subject
import com.example.classroom20.data.User
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.ui.theme.PrimaryPurple
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveQuizScreen(activeSubject: Subject? = null, onBack: () -> Unit) {
    var user by remember { mutableStateOf<User?>(null) }
    var subjects by remember { mutableStateOf<List<Subject>>(emptyList()) }
    var selectedSubject by remember { mutableStateOf<Subject?>(activeSubject) }
    var activeQuiz by remember { mutableStateOf<Quiz?>(null) }
    var expanded by remember { mutableStateOf(false) }
    
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableIntStateOf(-1) }
    var score by remember { mutableIntStateOf(0) }
    var isQuizFinished by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableLongStateOf(0L) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        FirebaseManager.getCurrentUser { fetchedUser ->
            user = fetchedUser
            if (fetchedUser != null) {
                FirebaseManager.getSubjectsForStudent(fetchedUser.joinedSubjects) { subjects = it }
            }
        }
    }

    LaunchedEffect(selectedSubject) {
        if (selectedSubject != null) {
            android.util.Log.d("QUIZ_DEBUG", "Student izabrao predmet: ${selectedSubject!!.name} (${selectedSubject!!.id})")
            FirebaseManager.getActiveQuiz(selectedSubject!!.id) { quiz ->
                if (quiz != null) {
                    android.util.Log.d("QUIZ_DEBUG", "Pronađen aktivan kviz: ${quiz.title}")
                    FirebaseManager.checkIfQuizCompleted(quiz.id) { isCompleted ->
                        if (!isCompleted) {
                            activeQuiz = quiz
                            timeLeft = quiz.durationSeconds.toLong()
                            currentQuestionIndex = 0
                            selectedOption = -1
                            score = 0
                            isQuizFinished = false
                        } else {
                            android.util.Log.d("QUIZ_DEBUG", "Kviz je već završen za ovog studenta.")
                            activeQuiz = null
                            Toast.makeText(context, "Kviz je već završen za ovaj predmet.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    android.util.Log.d("QUIZ_DEBUG", "Nema aktivnog kviza za ovaj predmet.")
                    activeQuiz = null
                    if (selectedSubject != null) {
                        Toast.makeText(context, "Trenutno nema aktivnog kviza.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            activeQuiz = null
        }
    }

    DisposableEffect(activeQuiz) {
        var timer: CountDownTimer? = null
        if (activeQuiz != null) {
            timer = object : CountDownTimer(timeLeft * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeft = millisUntilFinished / 1000
                }
                override fun onFinish() {
                    timeLeft = 0
                    isQuizFinished = true
                }
            }.start()
        }
        onDispose { timer?.cancel() }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
            
            AppHeader(
                title = stringResource(R.string.live_quiz_title),
                onBack = onBack
            )

            if (activeQuiz == null) {
                Spacer(modifier = Modifier.height(16.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedSubject?.name ?: stringResource(R.string.select_subject_placeholder),
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded, 
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                    ) {
                        subjects.forEach { subject ->
                            DropdownMenuItem(
                                text = { 
                                    Text(
                                        subject.name,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ) 
                                },
                                onClick = {
                                    selectedSubject = subject
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(R.string.no_active_quiz_msg), 
                        color = Color.Gray, 
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                if (isQuizFinished) {
                    QuizResultView(score, activeQuiz!!.questions.size, onBack) {
                        if (!isSubmitting) {
                            isSubmitting = true
                            val res = QuizResult(
                                studentUid = user?.uid ?: "",
                                studentName = "${user?.firstName} ${user?.lastName}",
                                quizId = activeQuiz!!.id,
                                subjectId = activeQuiz!!.subjectId,
                                score = score,
                                totalQuestions = activeQuiz!!.questions.size,
                                timeTakenSeconds = activeQuiz!!.durationSeconds - timeLeft.toInt()
                            )
                            FirebaseManager.submitQuizResultAndGrade(
                                result = res,
                                teacherName = selectedSubject?.teacherName ?: "Profesor",
                                subjectName = selectedSubject?.name ?: "Predmet"
                            ) { success ->
                                isSubmitting = false
                                if (success) {
                                    Toast.makeText(context, "Rezultat spremljen i ocjena upisana!", Toast.LENGTH_SHORT).show()
                                    onBack()
                                }
                            }
                        }
                    }
                } else {
                    QuizContent(
                        quiz = activeQuiz!!,
                        currentIndex = currentQuestionIndex,
                        selectedOption = selectedOption,
                        timeLeft = timeLeft.toInt(),
                        onOptionSelect = { selectedOption = it },
                        onNext = {
                            if (selectedOption == activeQuiz!!.questions[currentQuestionIndex].correctAnswerIndex) {
                                score++
                            }
                            if (currentQuestionIndex < activeQuiz!!.questions.size - 1) {
                                currentQuestionIndex++
                                selectedOption = -1
                            } else {
                                isQuizFinished = true
                                // Automatsko podnošenje smo prebacili isključivo na dugme "Spremi i završi" 
                                // kako bismo izbjegli dupliranje zapisa.
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun QuizContent(quiz: Quiz, currentIndex: Int, selectedOption: Int, timeLeft: Int, onOptionSelect: (Int) -> Unit, onNext: () -> Unit) {
    val question = quiz.questions[currentIndex]
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Vrijeme: $timeString", color = if (timeLeft < 10) Color.Red else PrimaryPurple, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text("Pitanje ${currentIndex + 1}/${quiz.questions.size}", color = Color.Gray)
        }
        LinearProgressIndicator(
            progress = { timeLeft.toFloat() / quiz.durationSeconds },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(10.dp),
            color = PrimaryPurple,
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            question.text, 
            fontSize = 22.sp, 
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(32.dp))
        question.options.forEachIndexed { index, option ->
            if (option.isNotBlank()) {
                QuizOptionItem(option, index == selectedOption) { onOptionSelect(index) }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onNext, 
            modifier = Modifier.fillMaxWidth().height(60.dp), 
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple), 
            enabled = selectedOption != -1, 
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(stringResource(R.string.next), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun QuizOptionItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        color = if (isSelected) PrimaryPurple.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, PrimaryPurple) else null,
        shadowElevation = 1.dp
    ) {
        Text(text, modifier = Modifier.padding(20.dp), color = if (isSelected) PrimaryPurple else MaterialTheme.colorScheme.onSurface, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Composable
fun QuizResultView(score: Int, total: Int, onBack: () -> Unit, onSave: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(
            stringResource(R.string.quiz_finished), 
            fontSize = 28.sp, 
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(stringResource(R.string.your_score_label), color = Color.Gray)
        Text("$score / $total", fontSize = 64.sp, fontWeight = FontWeight.ExtraBold, color = PrimaryPurple)
        Spacer(modifier = Modifier.height(48.dp))
        Button(onClick = onSave, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(16.dp)) { 
            Text(stringResource(R.string.save_finish_label), fontWeight = FontWeight.Bold) 
        }
        TextButton(onClick = onBack, modifier = Modifier.padding(top = 16.dp)) {
            Text(stringResource(R.string.back_no_save_label), color = Color.Gray)
        }
    }
}
