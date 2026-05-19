package com.example.classroom20.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
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
import androidx.compose.ui.res.stringResource
import com.example.classroom20.R
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.QuizResult
import com.example.classroom20.data.Subject
import com.example.classroom20.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    activeSubject: Subject?,
    onBack: () -> Unit
) {

    var results by remember {
        mutableStateOf<List<QuizResult>>(emptyList())
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(activeSubject) {

        if (activeSubject != null) {

            FirebaseManager.getQuizResults(activeSubject.id) { list ->

                results = list
                isLoading = false
            }

        } else {

            isLoading = false
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

            // GORNJI KRUG
            Box(
                modifier = Modifier
                    .size(190.dp)
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
                    .offset(x = 90.dp, y = 90.dp)
                    .background(
                        Color(0xFF60A5FA).copy(alpha = 0.06f),
                        CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {

                AppHeader(
                    title = "Rang lista",
                    onBack = onBack
                )

                Text(
                    text = "Top rezultati studenata",

                    color = PrimaryPurple,

                    fontSize = 13.sp,

                    fontWeight = FontWeight.Medium,
                    
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

                            color = Color.Gray,

                            textAlign = TextAlign.Center
                        )
                    }

                } else {

                    // SUBJECT CARD
                    Card(
                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(28.dp),

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

                            Surface(
                                modifier = Modifier.size(56.dp),

                                shape = RoundedCornerShape(18.dp),

                                color = PrimaryPurple.copy(alpha = 0.12f)
                            ) {

                                Icon(
                                    Icons.Default.EmojiEvents,
                                    contentDescription = null,

                                    tint = PrimaryPurple,

                                    modifier = Modifier.padding(14.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {

                                Text(
                                    text = activeSubject.name,

                                    color = MaterialTheme.colorScheme.onSurface,

                                    fontWeight = FontWeight.Bold,

                                    fontSize = 19.sp
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Rezultati kvizova i bodovi",

                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),

                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    if (isLoading) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator(
                                color = PrimaryPurple
                            )
                        }

                    } else if (results.isEmpty()) {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = stringResource(
                                    R.string.no_results_subject
                                ),

                                color = Color.Gray
                            )
                        }

                    } else {

                        LazyColumn(

                            verticalArrangement = Arrangement.spacedBy(16.dp),

                            contentPadding = PaddingValues(
                                bottom = 120.dp
                            )
                        ) {

                            itemsIndexed(results) { index, res ->

                                LeaderboardItem(
                                    index + 1,
                                    res
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(
    rank: Int,
    result: QuizResult
) {

    val isLowScore =
        result.score <= (result.totalQuestions / 3)

    val cardColor =
        if (isLowScore)
            Color(0xFFFFF1F2)
        else
            MaterialTheme.colorScheme.surface

    val accentColor =
        when (rank) {

            1 -> Color(0xFFFFC107)
            2 -> Color(0xFF94A3B8)
            3 -> Color(0xFFC08457)

            else ->
                if (isLowScore)
                    Color(0xFFEF4444)
                else
                    PrimaryPurple
        }

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),

        border =
            if (isLowScore)
                BorderStroke(
                    1.5.dp,
                    Color(0xFFFFCDD2)
                )
            else null,

        elevation = CardDefaults.cardElevation(
            defaultElevation = 7.dp
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(18.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // RANK
                Surface(
                    modifier = Modifier.size(52.dp),

                    shape = CircleShape,

                    color = accentColor.copy(alpha = 0.12f)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = "#$rank",

                            color = accentColor,

                            fontWeight = FontWeight.ExtraBold,

                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // USER ICON
                Surface(
                    modifier = Modifier.size(50.dp),

                    shape = RoundedCornerShape(16.dp),

                    color = accentColor.copy(alpha = 0.10f)
                ) {

                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,

                        tint = accentColor,

                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // INFO
                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = result.studentName,
                        color = MaterialTheme.colorScheme.onSurface,

                        fontWeight = FontWeight.Bold,

                        fontSize = 17.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Vrijeme: ${result.timeTakenSeconds}s",

                        color = Color.Gray,

                        fontSize = 12.sp
                    )
                }

                // SCORE
                Column(
                    horizontalAlignment = Alignment.End
                ) {

                    Text(
                        text =
                            "${result.score}/${result.totalQuestions}",

                        color =
                            if (isLowScore)
                                Color(0xFFDC2626)
                            else
                                Color(0xFF16A34A),

                        fontWeight = FontWeight.Black,

                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text =
                            if (isLowScore)
                                "Slab rezultat"
                            else
                                "Odlično",

                        color = Color.Gray,

                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}