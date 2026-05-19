package com.example.classroom20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Subject
import com.example.classroom20.data.User
import com.example.classroom20.ui.theme.PrimaryPurple
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RandomPickerScreen(
    activeSubject: Subject?,
    onBack: () -> Unit
) {

    var studentList by remember {
        mutableStateOf<List<User>>(emptyList())
    }

    var selectedStudent by remember {
        mutableStateOf<User?>(null)
    }

    var isPicking by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(activeSubject) {

        if (activeSubject != null) {

            FirebaseManager.getStudentsForSubject(
                activeSubject.id
            ) {
                studentList = it
            }
        }
    }

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
                            Color(0xFFF8F5FF),
                            Color.White,
                            Color(0xFFF3F0FF)
                        )
                    )
                }
            )
    ) {

        // Gornji dekorativni krug
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-90).dp, y = (-90).dp)
                .background(
                    PrimaryPurple.copy(alpha = 0.08f),
                    CircleShape
                )
        )

        // Donji dekorativni krug
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 100.dp)
                .background(
                    Color(0xFF60A5FA).copy(alpha = 0.07f),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(56.dp))

                // HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Surface(
                        modifier = Modifier.size(44.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp
                    ) {

                        IconButton(
                            onClick = onBack
                        ) {

                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = "Nasumični Odabir",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            Spacer(modifier = Modifier.height(26.dp))

            if (activeSubject == null) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Odaberite predmet na Dashboardu.",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }

            } else {

                // SUBJECT CARD
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Aktivni predmet",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = activeSubject.name,
                            color = PrimaryPurple,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Ukupno studenata: ${studentList.size}",
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(34.dp))

                // RANDOM PICKER CIRCLE
                Surface(
                    modifier = Modifier.size(240.dp),
                    shape = CircleShape,
                    shadowElevation = 16.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        PrimaryPurple.copy(alpha = 0.15f),
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            ),

                        contentAlignment = Alignment.Center
                    ) {

                        if (isPicking) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                CircularProgressIndicator(
                                    color = PrimaryPurple,
                                    strokeWidth = 5.dp
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = "Biram studenta...",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                            }

                        } else {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Surface(
                                    modifier = Modifier.size(90.dp),
                                    shape = CircleShape,
                                    color = PrimaryPurple.copy(alpha = 0.12f)
                                ) {

                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        tint = PrimaryPurple,
                                        modifier = Modifier
                                            .padding(20.dp)
                                            .fillMaxSize()
                                    )
                                }

                                Spacer(modifier = Modifier.height(18.dp))

                                Text(
                                    text =
                                        if (selectedStudent != null)
                                            "${selectedStudent!!.firstName} ${selectedStudent!!.lastName}"
                                        else
                                            "Spremni za odabir",

                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,

                                    textAlign = TextAlign.Center,

                                    modifier = Modifier.padding(horizontal = 20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // BUTTON
                Button(
                    onClick = {

                        if (studentList.isNotEmpty()) {

                            isPicking = true

                            scope.launch {

                                delay(2200)

                                selectedStudent =
                                    studentList.random()

                                isPicking = false
                            }
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),

                    enabled = !isPicking && studentList.isNotEmpty(),

                    shape = RoundedCornerShape(22.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    )
                ) {

                    Icon(
                        Icons.Default.Casino,
                        contentDescription = null,
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Pokreni Odabir",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (studentList.isEmpty()) {

                    Text(
                        text = "Nema studenata na ovom predmetu.",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}