package com.example.classroom20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom20.R
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Subject
import com.example.classroom20.data.User
import com.example.classroom20.ui.theme.PrimaryPurple
import com.example.classroom20.ui.theme.ThemeSettings

data class FeatureItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val roleRequired: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onFeatureClick: (String, Subject?) -> Unit = { _, _ -> },
    onLogout: () -> Unit = {},
    initialActiveSubject: Subject? = null
) {

    var selectedTab by remember { mutableIntStateOf(0) }

    var currentUser by remember {
        mutableStateOf<User?>(null)
    }

    var subjects by remember {
        mutableStateOf<List<Subject>>(emptyList())
    }

    var activeSubject by remember {
        mutableStateOf<Subject?>(initialActiveSubject)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        FirebaseManager.getCurrentUser { user ->

            currentUser = user

            if (user != null) {

                if (user.role == "teacher") {

                    FirebaseManager.getSubjectsForTeacher(user.uid) {

                        subjects = it

                        if (activeSubject == null && it.isNotEmpty()) {
                            activeSubject = it[0]
                        }
                    }

                } else {

                    FirebaseManager.getSubjectsForStudent(user.joinedSubjects) {

                        subjects = it

                        if (activeSubject == null && it.isNotEmpty()) {
                            activeSubject = it[0]
                        }
                    }
                }
            }
        }
    }

    val features = listOf(

        FeatureItem(
            "QR Prisustvo",
            "Skeniraj QR kod",
            Icons.Default.QrCode,
            Color(0xFF8B5CF6)
        ),

        FeatureItem(
            "Kviz uživo",
            "Pokreni kviz",
            Icons.Default.Quiz,
            Color(0xFFFFB84D)
        ),

        FeatureItem(
            "E-Dnevnik",
            "Ocjene i studenti",
            Icons.Default.MenuBook,
            Color(0xFF4FC3F7)
        ),

        FeatureItem(
            "Zadaća",
            "Pregled zadataka",
            Icons.Default.Assignment,
            Color(0xFF4ADE80)
        ),

        FeatureItem(
            "Feedback",
            "Komentari studenata",
            Icons.Default.Feedback,
            Color(0xFFFB7185)
        ),

        FeatureItem(
            "Materijali",
            "Skripte i prezentacije",
            Icons.Default.LibraryBooks,
            Color(0xFFEC4899)
        ),

        FeatureItem(
            "AI Asistent",
            "Gemini pomoćnik",
            Icons.Default.SmartToy,
            Color(0xFF6366F1)
        ),

        FeatureItem(
            "Rang lista",
            "Top studenti",
            Icons.Default.EmojiEvents,
            Color(0xFFFACC15)
        ),

        FeatureItem(
            "Nasumični odabir",
            "Izaberi studenta",
            Icons.Default.Casino,
            Color(0xFFA855F7),
            "teacher"
        )

    ).filter {
        it.roleRequired == null || it.roleRequired == currentUser?.role
    }

    val backgroundBrush =
        Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.surface
            )
        )

    Scaffold(

        containerColor = Color.Transparent,

        bottomBar = {

            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,

                tonalElevation = 8.dp,

                modifier = Modifier.clip(
                    RoundedCornerShape(
                        topStart = 28.dp,
                        topEnd = 28.dp
                    )
                )
            ) {

                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },

                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("Home")
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryPurple,
                        indicatorColor = PrimaryPurple.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },

                    icon = {
                        Icon(
                            Icons.Default.Class,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("Predmeti")
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryPurple,
                        indicatorColor = PrimaryPurple.copy(alpha = 0.15f)
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },

                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null
                        )
                    },

                    label = {
                        Text("Profil")
                    },

                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryPurple,
                        indicatorColor = PrimaryPurple.copy(alpha = 0.15f)
                    )
                )
            }
        }

    ) { padding ->

        when (selectedTab) {

            0 -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundBrush)
                        .padding(padding)
                ) {

                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .offset(x = (-90).dp, y = (-60).dp)
                            .background(
                                Color(0xFF8B5CF6).copy(alpha = 0.14f),
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
                        modifier = Modifier.fillMaxSize()
                    ) {

                        Spacer(modifier = Modifier.height(18.dp))

                        // AKTIVNI PREDMET OSTANE FIXED
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                        ) {

                            if (subjects.isNotEmpty()) {

                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = {
                                        expanded = !expanded
                                    }
                                ) {

                                    OutlinedTextField(
                                        value = activeSubject?.name ?: "",
                                        onValueChange = {},
                                        readOnly = true,

                                        label = {
                                            Text(stringResource(R.string.active_subject))
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

                                        subjects.forEach { subject ->

                                            DropdownMenuItem(

                                                text = {
                                                    Text(subject.name)
                                                },

                                                onClick = {

                                                    activeSubject = subject
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // OSTATK EKRANA SCROLLABLE
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 120.dp
                            )
                        ) {

                            item {

                                DashboardHeader(
                                    user = currentUser,
                                    subject = activeSubject
                                )

                                Spacer(modifier = Modifier.height(28.dp))

                                Text(
                                    text = stringResource(R.string.functionalities),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,

                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Spacer(modifier = Modifier.height(18.dp))
                            }

                            item {

                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    features.chunked(2).forEach { rowFeatures ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(bottom = 18.dp),
                                            horizontalArrangement = Arrangement.spacedBy(18.dp)
                                        ) {
                                            rowFeatures.forEach { feature ->
                                                DashboardCard(
                                                    feature = feature,
                                                    onClick = {
                                                        onFeatureClick(
                                                            feature.title,
                                                            activeSubject
                                                        )
                                                    },
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(170.dp)
                                                )
                                            }
                                            if (rowFeatures.size == 1) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                Box(modifier = Modifier.padding(padding)) {
                    SubjectsScreen()
                }
            }

            2 -> {
                Box(modifier = Modifier.padding(padding)) {
                    ProfileScreen(onLogout)
                }
            }
        }
    }
}

@Composable
fun DashboardHeader(
    user: User?,
    subject: Subject?
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),

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
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF7C3AED),
                            Color(0xFF5B21B6),
                            Color(0xFF2563EB)
                        )
                    )
                )
                .padding(22.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 50.dp, y = (-30).dp)
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

                    Column {

                        Text(
                            text = stringResource(R.string.welcome_wave),
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text =
                                if (user?.role == "teacher")
                                    "prof. ${user.firstName}"
                                else
                                    user?.firstName ?: "",

                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 28.sp
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .size(46.dp)
                            .clickable {
                                ThemeSettings.isDarkTheme =
                                    !ThemeSettings.isDarkTheme
                            },

                        shape = CircleShape,

                        color = Color.White.copy(alpha = 0.15f)
                    ) {

                        Icon(
                            imageVector =
                                if (ThemeSettings.isDarkTheme)
                                    Icons.Default.LightMode
                                else
                                    Icons.Default.DarkMode,

                            contentDescription = null,

                            tint = Color.White,

                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Column {

                    Text(
                        text = subject?.name ?: stringResource(R.string.no_subjects),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text =
                            if (user?.role == "teacher")
                                "${stringResource(R.string.class_code_label)}: ${subject?.classCode}"
                            else
                                "${stringResource(R.string.teacher)}: ${subject?.teacherName}",

                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )
                }

                Row {

                    SmallInfoCard(
                        icon = Icons.Default.MenuBook,
                        title = "Predmet"
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    SmallInfoCard(
                        icon = Icons.Default.Star,
                        title =
                            if (user?.role == "teacher")
                                "Profesor"
                            else
                                "Student"
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    feature: FeatureItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(26.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            feature.color.copy(alpha = 0.18f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-20).dp)
                    .background(
                        feature.color.copy(alpha = 0.08f),
                        CircleShape
                    )
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Surface(
                    modifier = Modifier.size(50.dp),

                    shape = RoundedCornerShape(16.dp),

                    color = feature.color.copy(alpha = 0.15f)
                ) {

                    Icon(
                        imageVector = feature.icon,
                        contentDescription = null,

                        tint = feature.color,

                        modifier = Modifier.padding(11.dp)
                    )
                }

                Column {

                    Text(
                        text = feature.title,

                        fontWeight = FontWeight.Bold,

                        fontSize = 17.sp,

                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = feature.description,

                        fontSize = 11.sp,

                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun SmallInfoCard(
    icon: ImageVector,
    title: String
) {

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.14f)
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 14.dp,
                vertical = 10.dp
            ),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,

                tint = Color.White,

                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,

                color = Color.White,

                fontWeight = FontWeight.Bold,

                fontSize = 12.sp
            )
        }
    }
}