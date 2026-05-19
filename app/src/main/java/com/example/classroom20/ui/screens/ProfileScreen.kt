package com.example.classroom20.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.classroom20.R
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.User
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.ui.theme.PrimaryPurple
import com.example.classroom20.ui.theme.ThemeSettings

@Composable
fun ProfileScreen(
    onLogout: () -> Unit
) {

    var user by remember {
        mutableStateOf<User?>(null)
    }

    var isUploading by remember {
        mutableStateOf(false)
    }

    var isEditing by remember {
        mutableStateOf(false)
    }

    var firstName by remember {
        mutableStateOf("")
    }

    var lastName by remember {
        mutableStateOf("")
    }

    var notificationsEnabled by remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {

        FirebaseManager.getCurrentUser { fetchedUser ->

            user = fetchedUser

            if (fetchedUser != null) {

                firstName = fetchedUser.firstName
                lastName = fetchedUser.lastName
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        if (uri != null) {

            isUploading = true

            FirebaseManager.uploadProfileImage(uri) { success, _ ->

                isUploading = false

                if (success) {

                    Toast.makeText(
                        context,
                        "Profilna slika ažurirana!",
                        Toast.LENGTH_SHORT
                    ).show()

                    FirebaseManager.getCurrentUser {
                        user = it
                    }

                } else {

                    Toast.makeText(
                        context,
                        "Greška pri uploadu slike",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // dekoracija gore lijevo
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = (-80).dp, y = (-80).dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    CircleShape
                )
        )

        // dekoracija dole desno
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 100.dp)
                .background(
                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AppHeader(title = stringResource(R.string.profile))

            Spacer(modifier = Modifier.height(30.dp))

            // PROFILE IMAGE
            Box(
                contentAlignment = Alignment.BottomEnd
            ) {

                Surface(
                    modifier = Modifier
                        .size(130.dp)
                        .clickable {
                            launcher.launch("image/*")
                        },

                    shape = CircleShape,

                    shadowElevation = 12.dp,

                    color = MaterialTheme.colorScheme.surface
                ) {

                    if (user?.profileImageUrl?.isNotEmpty() == true) {

                        AsyncImage(
                            model = user?.profileImageUrl,

                            contentDescription = null,

                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),

                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            PrimaryPurple,
                                            Color(0xFF8B5CF6)
                                        )
                                    )
                                ),

                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.Person,

                                contentDescription = null,

                                tint = Color.White,

                                modifier = Modifier.size(58.dp)
                            )
                        }
                    }

                    if (isUploading) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.Black.copy(alpha = 0.4f)
                                ),

                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator(
                                color = Color.White
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier.size(40.dp),

                    shape = CircleShape,

                    color = PrimaryPurple,

                    shadowElevation = 6.dp
                ) {

                    Icon(
                        Icons.Default.CameraAlt,

                        contentDescription = null,

                        tint = Color.White,

                        modifier = Modifier.padding(9.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isEditing) {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(
                        value = firstName,

                        onValueChange = {
                            firstName = it
                        },

                        label = {
                            Text(stringResource(R.string.first_name))
                        },

                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = lastName,

                        onValueChange = {
                            lastName = it
                        },

                        label = {
                            Text(stringResource(R.string.last_name))
                        },

                        modifier = Modifier.fillMaxWidth(),

                        shape = RoundedCornerShape(18.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(

                        onClick = {

                            FirebaseManager.updateUserProfile(
                                firstName,
                                lastName
                            ) { success ->

                                if (success) {

                                    isEditing = false

                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.profile_updated),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else {

                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.error_msg),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),

                        shape = RoundedCornerShape(18.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryPurple
                        )
                    ) {

                        Icon(
                            Icons.Default.Save,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            stringResource(R.string.save_changes),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            } else {

                Text(
                    text = "${user?.firstName ?: ""} ${user?.lastName ?: ""}",

                    fontSize = 28.sp,

                    fontWeight = FontWeight.ExtraBold,

                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = user?.email ?: "",

                    fontSize = 14.sp,

                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(50.dp),

                    color = PrimaryPurple.copy(alpha = 0.12f)
                ) {

                    Text(
                        text =
                            if (user?.role == "teacher")
                                stringResource(R.string.teacher).uppercase()
                            else
                                stringResource(R.string.student).uppercase(),

                        color = PrimaryPurple,

                        fontWeight = FontWeight.Bold,

                        fontSize = 12.sp,

                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 8.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        isEditing = true
                    }
                ) {

                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = PrimaryPurple
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = stringResource(R.string.edit_profile),
                        color = PrimaryPurple
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // SETTINGS CARDS

            ProfileMenuItem(
                icon =
                    if (ThemeSettings.isDarkTheme)
                        Icons.Default.DarkMode
                    else
                        Icons.Default.LightMode,

                title = stringResource(R.string.app_theme),

                subtitle =
                    if (ThemeSettings.isDarkTheme)
                        stringResource(R.string.dark_mode_on)
                    else
                        stringResource(R.string.light_mode_on),

                onClick = {

                    ThemeSettings.isDarkTheme =
                        !ThemeSettings.isDarkTheme
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            ProfileMenuItem(
                icon = Icons.Default.Language,

                title = stringResource(R.string.app_language),

                subtitle =
                    if (user?.language == "en")
                        "English"
                    else
                        "Bosanski",

                onClick = {

                    val nextLang =
                        if (user?.language == "en")
                            "bs"
                        else
                            "en"

                    FirebaseManager.updateUserLanguage(
                        nextLang
                    ) {

                        Toast.makeText(
                            context,
                            context.getString(R.string.language_changed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            ProfileMenuItem(
                icon = Icons.Default.Notifications,

                title = stringResource(R.string.notifications),

                subtitle =
                    if (notificationsEnabled)
                        stringResource(R.string.notifications_on)
                    else
                        stringResource(R.string.notifications_off),

                onClick = {

                    notificationsEnabled =
                        !notificationsEnabled
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            ProfileMenuItem(
                icon = Icons.Default.Lock,

                title = stringResource(R.string.privacy_security),

                subtitle = stringResource(R.string.protection),

                onClick = {

                    Toast.makeText(
                        context,
                        context.getString(R.string.soon_available),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            ProfileMenuItem(
                icon = Icons.Default.Info,

                title = stringResource(R.string.about_app),

                subtitle = stringResource(R.string.app_version),

                onClick = {
                    // info
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ODJAVA

            ProfileMenuItem(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                title = stringResource(R.string.logout),
                subtitle = stringResource(R.string.logout),
                color = Color(0xFFEF4444),
                onClick = {
                    FirebaseManager.logoutUser()
                    Toast.makeText(context, context.getString(R.string.logout_confirm), Toast.LENGTH_SHORT).show()
                    onLogout()
                }
            )

            Spacer(modifier = Modifier.height(160.dp))
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.primary
) {

    Surface(

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(24.dp),

        shadowElevation = 6.dp,

        color = MaterialTheme.colorScheme.surface
    ) {

        Row(

            modifier = Modifier.padding(18.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(

                modifier = Modifier.size(52.dp),

                shape = RoundedCornerShape(18.dp),

                color = color.copy(alpha = 0.10f)
            ) {

                Icon(
                    icon,

                    contentDescription = null,

                    tint = color,

                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,

                    fontWeight = FontWeight.Bold,

                    fontSize = 16.sp,

                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = subtitle,

                    fontSize = 12.sp,

                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,

                contentDescription = null,

                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}
