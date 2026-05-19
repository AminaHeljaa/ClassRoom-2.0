package com.example.classroom20.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Homework
import com.example.classroom20.data.HomeworkSubmission
import com.example.classroom20.data.Subject
import com.example.classroom20.data.User
import com.example.classroom20.ui.theme.PrimaryPurple

@Composable
fun HomeworkScreen(
    activeSubject: Subject?,
    onBack: () -> Unit
) {

    var user by remember { mutableStateOf<User?>(null) }

    var homeworkList by remember {
        mutableStateOf<List<Homework>>(emptyList())
    }

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var selectedHomeworkSubmissions by remember {
        mutableStateOf<Homework?>(null)
    }

    LaunchedEffect(Unit) {

        FirebaseManager.getCurrentUser {
            user = it
        }
    }

    LaunchedEffect(activeSubject) {

        if (activeSubject != null) {

            FirebaseManager.getHomeworkForSubject(
                activeSubject.id
            ) {
                homeworkList = it
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
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

        if (selectedHomeworkSubmissions != null) {

            SubmissionsView(
                homework = selectedHomeworkSubmissions!!,
                onBack = {
                    selectedHomeworkSubmissions = null
                }
            )

        } else {

            Scaffold(

                containerColor = Color.Transparent,

                floatingActionButton = {

                    if (
                        user?.role == "teacher"
                        && activeSubject != null
                    ) {

                        FloatingActionButton(
                            onClick = {
                                showAddDialog = true
                            },

                            containerColor = PrimaryPurple,

                            contentColor = Color.White,

                            shape = RoundedCornerShape(22.dp)
                        ) {

                            Icon(
                                Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                    }
                }

            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 24.dp)
                ) {

                    AppHeader(
                        title = "Zadaće",
                        onBack = onBack
                    )

                    Text(
                        text = activeSubject?.name ?: "",

                        color = PrimaryPurple,

                        fontSize = 13.sp,

                        fontWeight = FontWeight.SemiBold,
                        
                        modifier = Modifier.padding(bottom = 26.dp)
                    )

                    if (homeworkList.isEmpty()) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Icon(
                                    Icons.Default.Assignment,
                                    contentDescription = null,

                                    tint = Color.LightGray,

                                    modifier = Modifier.size(70.dp)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Trenutno nema zadaća",

                                    color = Color.Gray,

                                    fontSize = 16.sp
                                )
                            }
                        }

                    } else {

                        LazyColumn(

                            verticalArrangement =
                                Arrangement.spacedBy(18.dp),

                            contentPadding = PaddingValues(
                                bottom = 120.dp
                            )
                        ) {

                            items(homeworkList) { hw ->

                                HomeworkCard(
                                    homework = hw,
                                    user = user,

                                    onDelete = {

                                        FirebaseManager.deleteHomework(
                                            hw.id
                                        ) { success ->

                                            if (success) {

                                                homeworkList =
                                                    homeworkList.filter {
                                                        it.id != hw.id
                                                    }
                                            }
                                        }
                                    },

                                    onViewSubmissions = {
                                        selectedHomeworkSubmissions = hw
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (
            showAddDialog
            && activeSubject != null
        ) {

            AddHomeworkDialog(

                subjectId = activeSubject.id,

                onDismiss = {
                    showAddDialog = false
                },

                onAdded = {

                    showAddDialog = false

                    FirebaseManager.getHomeworkForSubject(
                        activeSubject.id
                    ) {
                        homeworkList = it
                    }
                }
            )
        }
    }
}
}

@Composable
fun HomeworkCard(
    homework: Homework,
    user: User?,
    onDelete: () -> Unit,
    onViewSubmissions: () -> Unit
) {

    var isUploading by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val isTeacher = user?.role == "teacher"

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        if (uri != null) {

            isUploading = true

            FirebaseManager.uploadHomeworkImage(
                uri,
                homework.id,
                "${user?.firstName} ${user?.lastName}"
            ) { success, _ ->

                isUploading = false

                if (success) {

                    Toast.makeText(
                        context,
                        "Zadaća poslana!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    Toast.makeText(
                        context,
                        "Greška pri slanju",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isTeacher)
                    onViewSubmissions()
            },

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
                .padding(20.dp)
        ) {

            Column {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(
                        modifier = Modifier.size(56.dp),

                        shape = RoundedCornerShape(18.dp),

                        color = PrimaryPurple.copy(alpha = 0.10f)
                    ) {

                        Icon(
                            Icons.Default.Assignment,
                            contentDescription = null,

                            tint = PrimaryPurple,

                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = homework.title,

                            fontSize = 19.sp,

                            fontWeight = FontWeight.Bold,

                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = homework.description,

                            color = Color.Gray,

                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                val dateStr =
                    java.text.SimpleDateFormat(
                        "dd.MM.yyyy",
                        java.util.Locale.getDefault()
                    ).format(
                        java.util.Date(homework.dueDate)
                    )

                Surface(
                    shape = RoundedCornerShape(14.dp),

                    color = PrimaryPurple.copy(alpha = 0.10f)
                ) {

                    Text(
                        text = "Rok: $dateStr",

                        color = PrimaryPurple,

                        fontWeight = FontWeight.Bold,

                        fontSize = 12.sp,

                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 8.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (isTeacher) {

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(10.dp)
                    ) {

                        Button(
                            onClick = onViewSubmissions,

                            shape = RoundedCornerShape(14.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryPurple
                            )
                        ) {

                            Icon(
                                Icons.Default.Visibility,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("Predaje")
                        }

                        OutlinedButton(
                            onClick = onDelete,

                            shape = RoundedCornerShape(14.dp)
                        ) {

                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = Color.Red
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                "Obriši",
                                color = Color.Red
                            )
                        }
                    }

                } else {

                    if (isUploading) {

                        CircularProgressIndicator(
                            color = PrimaryPurple
                        )

                    } else {

                        Button(
                            onClick = {
                                launcher.launch("*/*")
                            },

                            shape = RoundedCornerShape(14.dp),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryPurple
                            )
                        ) {

                            Icon(
                                Icons.Default.UploadFile,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text("Pošalji zadaću")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddHomeworkDialog(
    subjectId: String,
    onDismiss: () -> Unit,
    onAdded: () -> Unit
) {

    var title by remember {
        mutableStateOf("")
    }

    var desc by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        shape = RoundedCornerShape(28.dp),

        title = {

            Text(
                text = "Nova zadaća",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column {

                OutlinedTextField(
                    value = title,

                    onValueChange = {
                        title = it
                    },

                    label = {
                        Text("Naslov")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = desc,

                    onValueChange = {
                        desc = it
                    },

                    label = {
                        Text("Opis")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(16.dp)
                )
            }
        },

        confirmButton = {

            Button(

                onClick = {

                    if (title.isNotBlank()) {

                        val hw = Homework(
                            subjectId = subjectId,
                            title = title,
                            description = desc,
                            dueDate =
                                System.currentTimeMillis() +
                                        86400000 * 7,

                            teacherId =
                                FirebaseManager.getCurrentUserUid()
                                    ?: ""
                        )

                        FirebaseManager.addHomework(hw) {

                            onAdded()
                        }
                    }
                },

                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPurple
                )
            ) {

                Text("Dodaj")
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

@Composable
fun SubmissionsView(
    homework: Homework,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var submissions by remember {
        mutableStateOf<List<HomeworkSubmission>>(emptyList())
    }

    LaunchedEffect(homework.id) {

        FirebaseManager.getHomeworkSubmissions(
            homework.id
        ) {
            submissions = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Predaje",

                fontSize = 26.sp,

                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (submissions.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Nema predaja.",

                    color = Color.Gray
                )
            }

        } else {

            LazyColumn(
                verticalArrangement =
                    Arrangement.spacedBy(14.dp)
            ) {

                items(submissions) { sub ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (sub.imageUrl.isNotEmpty()) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(sub.imageUrl))
                                    context.startActivity(intent)
                                }
                            },

                        shape = RoundedCornerShape(24.dp),

                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {

                        Row(
                            modifier = Modifier.padding(18.dp),

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Surface(
                                modifier = Modifier.size(50.dp),

                                shape = CircleShape,

                                color =
                                    PrimaryPurple.copy(alpha = 0.10f)
                            ) {

                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,

                                    tint = PrimaryPurple,

                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = sub.studentName,

                                    fontWeight = FontWeight.Bold,
                                    
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(
                                    modifier = Modifier.height(4.dp)
                                )

                                Text(
                                    text = "Predaja uspješna",

                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                                    fontSize = 12.sp
                                )
                            }

                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,

                                tint = PrimaryPurple
                            )
                        }
                    }
                }
            }
        }
    }
}