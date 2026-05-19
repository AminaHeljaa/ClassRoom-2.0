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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.data.Feedback
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.User
import com.example.classroom20.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(onBack: () -> Unit) {

    var user by remember {
        mutableStateOf<User?>(null)
    }

    var teachers by remember {
        mutableStateOf<List<User>>(emptyList())
    }

    var teacherFeedback by remember {
        mutableStateOf<List<Feedback>>(emptyList())
    }

    LaunchedEffect(Unit) {

        FirebaseManager.getCurrentUser { fetchedUser ->

            user = fetchedUser

            if (fetchedUser?.role == "teacher") {

                FirebaseManager.getFeedbackForTeacher(
                    fetchedUser.uid
                ) {
                    teacherFeedback = it
                }

            } else {

                FirebaseManager.getAllTeachers {
                    teachers = it
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .size(240.dp)
                    .offset(x = (-70).dp, y = (-40).dp)
                    .background(
                        Color(0xFF8B5CF6).copy(alpha = 0.08f),
                        CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(220.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 70.dp, y = 70.dp)
                    .background(
                        Color(0xFF2563EB).copy(alpha = 0.05f),
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
                        "Moji Feedback-ovi"
                    else
                        "Anonimni Feedback",
                    onBack = onBack
                )

                Text(
                    text =
                        if (user?.role == "teacher")
                            "Pregled studentskih komentara"
                        else
                            "Pošalji anonimnu poruku profesoru",

                    color = Color.Gray,

                    fontSize = 13.sp,
                    
                    modifier = Modifier.padding(bottom = 22.dp)
                )

                if (user?.role == "teacher") {
                    TeacherFeedbackView(teacherFeedback)
                } else {
                    StudentFeedbackView(teachers)
                }
            }
        }
    }
}

@Composable
fun TeacherFeedbackView(
    feedbackList: List<Feedback>
) {

    if (feedbackList.isEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Još nema feedback poruka.",
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

            items(feedbackList) { fb ->

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

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(
                                        Brush.linearGradient(
                                            colors =
                                                if (fb.type == "Pohvala")
                                                    listOf(
                                                        Color(0xFF22C55E),
                                                        Color(0xFF16A34A)
                                                    )
                                                else
                                                    listOf(
                                                        Color(0xFFEF4444),
                                                        Color(0xFFDC2626)
                                                    )
                                        ),
                                        CircleShape
                                    ),

                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector =
                                        if (fb.type == "Pohvala")
                                            Icons.Default.Favorite
                                        else
                                            Icons.Default.Warning,

                                    contentDescription = null,

                                    tint = Color.White,

                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = fb.type,

                                    fontWeight = FontWeight.Bold,

                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = "Anonimni student",

                                    color = Color.Gray,

                                    fontSize = 12.sp
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(16.dp),

                                color =
                                    if (fb.type == "Pohvala")
                                        Color(0xFFEAFBF1)
                                    else
                                        Color(0xFFFFEEEE)
                            ) {

                                Text(
                                    text = fb.type,

                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    ),

                                    color =
                                        if (fb.type == "Pohvala")
                                            Color(0xFF16A34A)
                                        else
                                            Color.Red,

                                    fontWeight = FontWeight.Bold,

                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = fb.content,

                            color = MaterialTheme.colorScheme.onSurface,

                            lineHeight = 22.sp,

                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFeedbackView(
    teachers: List<User>
) {

    var selectedTeacher by remember {
        mutableStateOf<User?>(null)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var feedbackType by remember {
        mutableStateOf("Pohvala")
    }

    var content by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(18.dp),

        contentPadding = PaddingValues(
            bottom = 120.dp
        )
    ) {

        item {

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(28.dp),

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

                    Text(
                        text = "Profesor",

                        color = Color.Gray,

                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {

                        OutlinedTextField(
                            value =
                                if (selectedTeacher != null)
                                    "prof. ${selectedTeacher!!.firstName} ${selectedTeacher!!.lastName}"
                                else
                                    "",

                            onValueChange = {},

                            readOnly = true,

                            placeholder = {
                                Text("Odaberi profesora")
                            },

                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },

                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),

                            shape = RoundedCornerShape(18.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,

                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            teachers.forEach { teacher ->

                                DropdownMenuItem(

                                    text = {
                                        Text(
                                            "prof. ${teacher.firstName} ${teacher.lastName}"
                                        )
                                    },

                                    onClick = {

                                        selectedTeacher = teacher
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Vrsta feedback-a",

                        color = Color.Gray,

                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        FilterChip(
                            selected = feedbackType == "Pohvala",

                            onClick = {
                                feedbackType = "Pohvala"
                            },

                            label = {
                                Text("Pohvala")
                            },

                            leadingIcon = {

                                Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )

                        FilterChip(
                            selected = feedbackType == "Žalba",

                            onClick = {
                                feedbackType = "Žalba"
                            },

                            label = {
                                Text("Žalba")
                            },

                            leadingIcon = {

                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = content,

                        onValueChange = {
                            content = it
                        },

                        label = {
                            Text("Vaša anonimna poruka")
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),

                        shape = RoundedCornerShape(20.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(

                        onClick = {

                            if (selectedTeacher != null &&
                                content.isNotBlank()
                            ) {

                                val fb = Feedback(
                                    teacherUid = selectedTeacher!!.uid,

                                    teacherName =
                                        "prof. ${selectedTeacher!!.firstName}",

                                    type = feedbackType,

                                    content = content
                                )

                                FirebaseManager.submitFeedback(
                                    fb
                                ) { success ->

                                    if (success) {

                                        Toast.makeText(
                                            context,
                                            "Feedback uspješno poslan!",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        content = ""
                                    }
                                }

                            } else {

                                Toast.makeText(
                                    context,
                                    "Popunite sva polja",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),

                        shape = RoundedCornerShape(20.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple
                        )
                    ) {

                        Icon(
                            Icons.Default.Send,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = "Pošalji anonimno",

                            fontWeight = FontWeight.Bold,

                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}
