package com.example.classroom20.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Subject
import com.example.classroom20.data.User
import com.example.classroom20.ui.theme.PrimaryPurple
import java.util.UUID

@Composable
fun RoleSelectionScreen(
    onRoleSelected: (String) -> Unit
) {

    var showCodeDialog by remember { mutableStateOf(false) }
    var showSubjectDialog by remember { mutableStateOf(false) }
    var showJoinDialog by remember { mutableStateOf(false) }

    var selectedRole by remember { mutableStateOf("") }
    var accessCode by remember { mutableStateOf("") }

    var currentUser by remember {
        mutableStateOf<User?>(null)
    }

    var loading by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {

        FirebaseManager.getCurrentUser { user ->

            currentUser = user
            loading = false

            if (user != null && user.role.isNotEmpty()) {

                if (user.role == "teacher") {
                    onRoleSelected("teacher")
                } else {
                    onRoleSelected("student")
                }
            }
        }
    }

    if (loading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            CircularProgressIndicator(
                color = PrimaryPurple
            )
        }

        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFF8FAFF),
                        Color(0xFFEEF2FF),
                        Color.White
                    )
                )
            )
    ) {

        Box(
            modifier = Modifier
                .size(260.dp)
                .offset(x = (-90).dp, y = (-70).dp)
                .background(
                    Color(0xFF8B5CF6).copy(alpha = 0.12f),
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 80.dp, y = 80.dp)
                .background(
                    Color(0xFF2563EB).copy(alpha = 0.10f),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(70.dp))

            Text(
                text = "Classroom 2.0",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Odaberite način korištenja aplikacije i nastavite dalje.",
                fontSize = 15.sp,
                lineHeight = 24.sp,
                color = Color(0xFF6B7280)
            )

            Spacer(modifier = Modifier.height(40.dp))

            BetterRoleCard(
                title = "Profesor",
                subtitle = "Kreirajte predmete, kvizove i pratite napredak studenata.",
                icon = Icons.Default.School,
                gradient = listOf(
                    Color(0xFF7C3AED),
                    Color(0xFF5B21B6)
                ),
                onClick = {
                    selectedRole = "teacher"
                    showCodeDialog = true
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            BetterRoleCard(
                title = "Student",
                subtitle = "Pridružite se predmetima, rješavajte kvizove i pratite rezultate.",
                icon = Icons.Default.Person,
                gradient = listOf(
                    Color(0xFF2563EB),
                    Color(0xFF1D4ED8)
                ),
                onClick = {
                    selectedRole = "student"
                    showCodeDialog = true
                }
            )
        }
    }

    if (showCodeDialog) {

        AlertDialog(

            onDismissRequest = {
                showCodeDialog = false
            },

            shape = RoundedCornerShape(28.dp),

            containerColor = Color.White,

            title = {

                Text(
                    text = "Pristupni kod",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            },

            text = {

                Column {

                    Text(
                        text = "Unesite kod za nastavak.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = accessCode,
                        onValueChange = {
                            accessCode = it
                        },

                        label = {
                            Text("Kod")
                        },

                        shape = RoundedCornerShape(18.dp),

                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        val expectedCode =
                            if (selectedRole == "teacher")
                                "PROF2026"
                            else
                                "STUD2026"

                        if (accessCode == expectedCode) {

                            showCodeDialog = false

                            if (selectedRole == "teacher") {
                                showSubjectDialog = true
                            } else {
                                showJoinDialog = true
                            }

                        } else {

                            Toast.makeText(
                                context,
                                "Pogrešan kod!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    )
                ) {

                    Text("Nastavi")
                }
            }
        )
    }

    if (showSubjectDialog) {

        val subjectNames = remember {
            mutableStateListOf("")
        }

        AlertDialog(

            onDismissRequest = {
                showSubjectDialog = false
            },

            shape = RoundedCornerShape(28.dp),

            containerColor = Color.White,

            title = {

                Text(
                    text = "Kreiranje predmeta",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Column {

                    Text(
                        text = "Dodajte predmete koje predajete.",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier.heightIn(max = 260.dp)
                    ) {

                        itemsIndexed(subjectNames) { index, item ->

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                OutlinedTextField(

                                    value = item,

                                    onValueChange = {
                                        subjectNames[index] = it
                                    },

                                    label = {
                                        Text("Predmet ${index + 1}")
                                    },

                                    modifier = Modifier.weight(1f),

                                    shape = RoundedCornerShape(18.dp)
                                )

                                if (subjectNames.size > 1) {

                                    IconButton(
                                        onClick = {
                                            subjectNames.removeAt(index)
                                        }
                                    ) {

                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = Color.Red
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    OutlinedButton(

                        onClick = {
                            subjectNames.add("")
                        },

                        shape = RoundedCornerShape(16.dp)
                    ) {

                        Icon(
                            Icons.Default.Add,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text("Dodaj predmet")
                    }
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        val filtered =
                            subjectNames.filter {
                                it.isNotBlank()
                            }

                        if (filtered.isNotEmpty()) {

                            val teacherId =
                                currentUser?.uid ?: ""

                            val teacherName =
                                "${currentUser?.firstName} ${currentUser?.lastName}"

                            val subjects = filtered.map {

                                Subject(
                                    name = it,
                                    teacherId = teacherId,
                                    teacherName = teacherName,
                                    classCode = UUID.randomUUID()
                                        .toString()
                                        .take(6)
                                        .uppercase()
                                )
                            }

                            FirebaseManager.createSubjects(subjects) { success ->

                                if (success) {

                                    FirebaseManager.updateUserRole(
                                        teacherId,
                                        "teacher"
                                    ) {

                                        showSubjectDialog = false
                                        onRoleSelected("teacher")
                                    }
                                }
                            }
                        }
                    },

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    )
                ) {

                    Text("Završi")
                }
            }
        )
    }

    if (showJoinDialog) {

        var classCode by remember {
            mutableStateOf("")
        }

        AlertDialog(

            onDismissRequest = {
                showJoinDialog = false
            },

            shape = RoundedCornerShape(28.dp),

            containerColor = Color.White,

            title = {

                Text(
                    text = "Pridruži se predmetu",
                    fontWeight = FontWeight.Bold
                )
            },

            text = {

                Column {

                    Text(
                        text = "Unesite kod predmeta.",
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = classCode,
                        onValueChange = {
                            classCode = it
                        },

                        label = {
                            Text("Kod predmeta")
                        },

                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(18.dp)
                    )
                }
            },

            confirmButton = {

                Button(

                    onClick = {

                        FirebaseManager.joinSubject(
                            classCode
                        ) { success, error ->

                            if (success) {

                                FirebaseManager.updateUserRole(
                                    currentUser?.uid ?: "",
                                    "student"
                                ) {

                                    showJoinDialog = false
                                    onRoleSelected("student")
                                }

                            } else {

                                Toast.makeText(
                                    context,
                                    error ?: "Greška",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    )
                ) {

                    Text("Pridruži se")
                }
            }
        )
    }
}

@Composable
fun BetterRoleCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(34.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(gradient)
                )
                .padding(24.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 40.dp, y = (-30).dp)
                    .background(
                        Color.White.copy(alpha = 0.08f),
                        CircleShape
                    )
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Surface(
                        modifier = Modifier.size(64.dp),

                        shape = RoundedCornerShape(22.dp),

                        color = Color.White.copy(alpha = 0.15f)
                    ) {

                        Icon(
                            imageVector = icon,
                            contentDescription = null,

                            tint = Color.White,

                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(18.dp),

                        color = Color.White.copy(alpha = 0.15f)
                    ) {

                        Text(
                            text = "CLASSROOM",

                            color = Color.White,

                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 8.dp
                            ),

                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column {

                    Text(
                        text = title,

                        color = Color.White,

                        fontWeight = FontWeight.ExtraBold,

                        fontSize = 28.sp,

                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = subtitle,

                        color = Color.White.copy(alpha = 0.92f),

                        fontSize = 14.sp,

                        lineHeight = 22.sp,

                        maxLines = 3
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Nastavi dalje",

                        color = Color.White,

                        fontWeight = FontWeight.Bold,

                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        modifier = Modifier.size(34.dp),

                        shape = CircleShape,

                        color = Color.White.copy(alpha = 0.15f)
                    ) {

                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,

                            tint = Color.White,

                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}