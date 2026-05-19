package com.example.classroom20.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.School
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
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Subject
import com.example.classroom20.data.User
import com.example.classroom20.ui.theme.PrimaryPurple
import java.util.UUID

@Composable
fun SubjectsScreen() {

    var user by remember { mutableStateOf<User?>(null) }

    var subjects by remember {
        mutableStateOf<List<Subject>>(emptyList())
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val refreshSubjects = {

        if (user != null) {

            if (user?.role == "teacher") {

                FirebaseManager.getSubjectsForTeacher(user!!.uid) {
                    subjects = it
                }

            } else {

                FirebaseManager.getSubjectsForStudent(user!!.joinedSubjects) {
                    subjects = it
                }
            }
        }
    }

    LaunchedEffect(Unit) {

        FirebaseManager.getCurrentUser { fetchedUser ->

            user = fetchedUser

            refreshSubjects()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Scaffold(

            containerColor = Color.Transparent,

            floatingActionButton = {

                FloatingActionButton(

                    onClick = { showAddDialog = true },

                    containerColor = PrimaryPurple,

                    contentColor = Color.White,

                    shape = RoundedCornerShape(22.dp)
                ) {

                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                // GORNJI KRUG
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .align(Alignment.TopStart)
                        .offset(x = (-120).dp, y = (-120).dp)
                        .background(
                            PrimaryPurple.copy(alpha = 0.05f),
                            CircleShape
                        )
                )

                // DONJI KRUG
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
                ) {

                    AppHeader(
                        title = if (user?.role == "teacher")
                            stringResource(R.string.subjects_teacher)
                        else
                            stringResource(R.string.subjects_student)
                    )

                    Text(
                        text = stringResource(R.string.sync_active),

                        color = PrimaryPurple,

                        fontSize = 13.sp,

                        fontWeight = FontWeight.Medium,
                        
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    if (subjects.isEmpty()) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = stringResource(R.string.no_subjects),

                                color = Color.Gray,

                                fontSize = 15.sp
                            )
                        }

                    } else {

                        LazyColumn(

                            verticalArrangement = Arrangement.spacedBy(16.dp),

                            contentPadding = PaddingValues(
                                top = 6.dp,
                                bottom = 180.dp
                            )
                        ) {

                            items(subjects) { subject ->

                                SubjectCard(
                                    subject,
                                    user?.role == "teacher"
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog) {

            if (user?.role == "teacher") {

                var name by remember {
                    mutableStateOf("")
                }

                AlertDialog(

                    onDismissRequest = {
                        showAddDialog = false
                    },

                    shape = RoundedCornerShape(28.dp),

                    title = {
                        Text(
                            stringResource(R.string.add_subject_title),
                            fontWeight = FontWeight.Bold
                        )
                    },

                    text = {

                        OutlinedTextField(
                            value = name,

                            onValueChange = {
                                name = it
                            },

                            label = {
                                Text(stringResource(R.string.subject_name_label))
                            },

                            modifier = Modifier.fillMaxWidth(),

                            shape = RoundedCornerShape(16.dp),

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    },

                    confirmButton = {

                        Button(

                            onClick = {

                                if (name.isNotBlank()) {

                                    val subj = Subject(

                                        name = name,

                                        teacherId = user!!.uid,

                                        teacherName =
                                            "${user!!.firstName} ${user!!.lastName}",

                                        classCode =
                                            UUID.randomUUID()
                                                .toString()
                                                .take(6)
                                                .uppercase()
                                    )

                                    FirebaseManager.createSubject(subj) {

                                        showAddDialog = false
                                    }
                                }
                            },

                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryPurple
                            ),

                            shape = RoundedCornerShape(14.dp)
                        ) {

                            Text(stringResource(R.string.create_label))
                        }
                    }
                )

            } else {

                var code by remember {
                    mutableStateOf("")
                }

                AlertDialog(

                    onDismissRequest = {
                        showAddDialog = false
                    },

                    shape = RoundedCornerShape(28.dp),

                    title = {
                        Text(
                            stringResource(R.string.join_class_title),
                            fontWeight = FontWeight.Bold
                        )
                    },

                    text = {

                        Column {
                            Text(
                                text = stringResource(R.string.enroll_code),
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            OutlinedTextField(
                                value = code,

                                onValueChange = {
                                    code = it
                                },

                                label = {
                                    Text(stringResource(R.string.class_code_label))
                                },

                                modifier = Modifier.fillMaxWidth(),

                                shape = RoundedCornerShape(16.dp),

                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    },

                    confirmButton = {

                        Button(

                            onClick = {

                                if (code.isNotBlank()) {

                                    FirebaseManager.joinSubject(
                                        code
                                    ) { success, err ->

                                        if (success) {

                                            showAddDialog = false

                                        } else {

                                            Toast.makeText(
                                                context,
                                                err ?: context.getString(R.string.error_msg),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            },

                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryPurple
                            ),

                            shape = RoundedCornerShape(14.dp)
                        ) {

                            Text(stringResource(R.string.join_label))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SubjectCard(
    subject: Subject,
    isTeacher: Boolean
) {
    val context = LocalContext.current

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

        Box(

            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            PrimaryPurple.copy(alpha = 0.06f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(18.dp)
        ) {

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(

                        modifier = Modifier.size(54.dp),

                        shape = RoundedCornerShape(18.dp),

                        color = PrimaryPurple.copy(alpha = 0.10f)
                    ) {

                        Icon(
                            Icons.Default.School,
                            contentDescription = null,

                            tint = PrimaryPurple,

                            modifier = Modifier.padding(13.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = subject.name,

                            color = MaterialTheme.colorScheme.onSurface,

                            fontWeight = FontWeight.Bold,

                            fontSize = 19.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text =
                                if (isTeacher)
                                    "Kod: ${subject.classCode}"
                                else
                                    "Profesor: ${subject.teacherName}",

                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                            fontSize = 13.sp
                        )
                    }

                    if (isTeacher) {

                        val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
                        val annotatedString = androidx.compose.ui.text.AnnotatedString(subject.classCode)

                        Surface(

                            modifier = Modifier.size(42.dp),

                            shape = CircleShape,

                            color = PrimaryPurple.copy(alpha = 0.10f)
                        ) {

                            IconButton(
                                onClick = {
                                    clipboardManager.setText(annotatedString)
                                    Toast.makeText(context, "Kod kopiran!", Toast.LENGTH_SHORT).show()
                                }
                            ) {

                                Icon(
                                    Icons.Default.ContentCopy,
                                    contentDescription = "Copy",

                                    tint = PrimaryPurple,

                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    SmallTag("Aktivan")
                    SmallTag("E-dnevnik")
                }
            }
        }
    }
}

@Composable
fun SmallTag(text: String) {

    Surface(

        shape = RoundedCornerShape(12.dp),

        color = PrimaryPurple.copy(alpha = 0.10f)
    ) {

        Text(
            text = text,

            color = PrimaryPurple,

            fontSize = 11.sp,

            fontWeight = FontWeight.SemiBold,

            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
        )
    }
}