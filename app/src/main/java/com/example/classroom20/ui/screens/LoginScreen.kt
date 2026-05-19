package com.example.classroom20.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.classroom20.R
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.ui.theme.PrimaryPurple
import com.example.classroom20.ui.theme.PurpleGradient
import com.example.classroom20.ui.theme.ThemeSettings
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    val backgroundBrush =
        if (ThemeSettings.isDarkTheme) {

            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0F172A),
                    Color(0xFF111827),
                    Color(0xFF1E293B)
                )
            )

        } else {

            Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFF8FAFF),
                    Color(0xFFEEF2FF),
                    Color.White
                )
            )
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {

        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(x = (-60).dp, y = (-40).dp)
                .background(
                    Color(0xFF8B5CF6).copy(alpha = 0.18f),
                    CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 80.dp, y = 100.dp)
                .background(
                    Color(0xFF2563EB).copy(alpha = 0.12f),
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

            Spacer(modifier = Modifier.height(80.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = stringResource(R.string.welcome),
                    color =
                        if (ThemeSettings.isDarkTheme)
                            Color.White
                        else
                            Color(0xFF111827),

                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = stringResource(R.string.signin_continue),
                    color =
                        if (ThemeSettings.isDarkTheme)
                            Color.LightGray
                        else
                            Color.Gray,

                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(42.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(34.dp),

                colors = CardDefaults.cardColors(
                    containerColor =
                        if (ThemeSettings.isDarkTheme)
                            Color(0xFF1E293B).copy(alpha = 0.95f)
                        else
                            Color.White.copy(alpha = 0.95f)
                ),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    Text(
                        text = stringResource(R.string.signin),

                        color =
                            if (ThemeSettings.isDarkTheme)
                                Color.White
                            else
                                Color(0xFF111827),

                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // EMAIL

                    OutlinedTextField(
                        value = email,

                        onValueChange = {
                            email = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        label = {
                            Text(stringResource(R.string.email))
                        },

                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = PrimaryPurple
                            )
                        },

                        shape = RoundedCornerShape(20.dp),

                        singleLine = true,

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,

                            unfocusedBorderColor =
                                if (ThemeSettings.isDarkTheme)
                                    Color.DarkGray
                                else
                                    Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // PASSWORD

                    OutlinedTextField(
                        value = password,

                        onValueChange = {
                            password = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        label = {
                            Text(stringResource(R.string.password))
                        },

                        leadingIcon = {

                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = PrimaryPurple
                            )
                        },

                        trailingIcon = {

                            IconButton(
                                onClick = {
                                    passwordVisible = !passwordVisible
                                }
                            ) {

                                Icon(
                                    imageVector =
                                        if (passwordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,

                                    contentDescription = null,
                                    tint = PrimaryPurple
                                )
                            }
                        },

                        visualTransformation =
                            if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),

                        shape = RoundedCornerShape(20.dp),

                        singleLine = true,

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurple,

                            unfocusedBorderColor =
                                if (ThemeSettings.isDarkTheme)
                                    Color.DarkGray
                                else
                                    Color.LightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    // LOGIN BUTTON

                    Button(
                        onClick = {

                            if (email.isNotBlank() && password.isNotBlank()) {

                                isLoading = true

                                FirebaseManager.loginUser(
                                    email,
                                    password
                                ) { success, _ ->

                                    isLoading = false

                                    if (success) {

                                        onLoginSuccess()

                                    } else {

                                        showErrorDialog = true
                                    }
                                }
                            }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),

                        shape = RoundedCornerShape(20.dp),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),

                        contentPadding = PaddingValues()
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        PurpleGradient
                                    )
                                ),

                            contentAlignment = Alignment.Center
                        ) {

                            if (isLoading) {

                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp
                                )

                            } else {

                                Text(
                                    text = stringResource(R.string.signin),

                                    color = Color.White,

                                    fontWeight = FontWeight.Bold,

                                    fontSize = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        HorizontalDivider(
                            modifier = Modifier.weight(1f),

                            color =
                                if (ThemeSettings.isDarkTheme)
                                    Color.DarkGray
                                else
                                    Color.LightGray
                        )

                        Text(
                            text = "  ili nastavi sa  ",

                            color =
                                if (ThemeSettings.isDarkTheme)
                                    Color.LightGray
                                else
                                    Color.Gray,

                            fontSize = 13.sp
                        )

                        HorizontalDivider(
                            modifier = Modifier.weight(1f),

                            color =
                                if (ThemeSettings.isDarkTheme)
                                    Color.DarkGray
                                else
                                    Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // GOOGLE + FACEBOOK

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        SocialButton(
                            text = "Google",
                            emoji = "G",
                            modifier = Modifier.weight(1f),

                            onClick = {
                                scope.launch {
                                    try {
                                        val googleIdOption = GetGoogleIdOption.Builder()
                                            .setFilterByAuthorizedAccounts(false)
                                            .setServerClientId(context.getString(R.string.default_web_client_id))
                                            .setAutoSelectEnabled(true)
                                            .build()

                                        val request = GetCredentialRequest.Builder()
                                            .addCredentialOption(googleIdOption)
                                            .build()

                                        val result = credentialManager.getCredential(context, request)
                                        
                                        FirebaseManager.signInWithGoogleCredential(
                                            result.credential
                                        ) { success ->
                                            if (success) {
                                                onLoginSuccess()
                                            } else {
                                                showErrorDialog = true
                                            }
                                        }
                                    } catch (e: Exception) {
                                        showErrorDialog = true
                                    }
                                }
                            }
                        )

                        SocialButton(
                            text = "Facebook",
                            emoji = "f",
                            modifier = Modifier.weight(1f),

                            onClick = {

                                FirebaseManager.signInWithFacebook(
                                    context
                                ) { success ->

                                    if (success) {

                                        onLoginSuccess()

                                    } else {

                                        showErrorDialog = true
                                    }
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.dont_have_account),

                    color =
                        if (ThemeSettings.isDarkTheme)
                            Color.LightGray
                        else
                            Color.Gray
                )

                Text(
                    text = " " + stringResource(R.string.register),

                    color = PrimaryPurple,

                    fontWeight = FontWeight.Bold,

                    modifier = Modifier.clickable(
                        indication = null,

                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        onRegisterClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // ERROR DIALOG

        if (showErrorDialog) {

            ModernErrorDialog(
                title = "Greška pri prijavi",

                message = "Prijava nije uspjela. Pokušajte ponovo.",

                onDismiss = {
                    showErrorDialog = false
                },

                onAction = {

                    showErrorDialog = false
                    onRegisterClick()
                },

                actionText = "Registrujte se"
            )
        }
    }
}

@Composable
fun ModernErrorDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onAction: () -> Unit = {},
    actionText: String = ""
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onDismiss,
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        "Pokušaj ponovo",
                        color = if (ThemeSettings.isDarkTheme) Color.LightGray else Color.Gray,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }

                if (actionText.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onAction,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Text(
                            actionText,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        },
        icon = {
            Surface(
                modifier = Modifier.size(64.dp),
                color = Color.Red.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Icon(
                    Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.padding(16.dp).size(32.dp)
                )
            }
        },
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 14.sp,
                color = if (ThemeSettings.isDarkTheme) Color.LightGray else Color.Gray
            )
        },
        shape = RoundedCornerShape(28.dp),
        containerColor = if (ThemeSettings.isDarkTheme) Color(0xFF1E293B) else Color.White
    )
}

@Composable
fun SocialButton(
    text: String,
    emoji: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    OutlinedButton(

        onClick = onClick,

        modifier = modifier.height(58.dp),

        shape = RoundedCornerShape(18.dp),

        contentPadding = PaddingValues(horizontal = 8.dp),

        border = BorderStroke(
            1.dp,

            if (ThemeSettings.isDarkTheme)
                Color.DarkGray
            else
                Color.LightGray
        ),

        colors = ButtonDefaults.outlinedButtonColors(

            containerColor =
                if (ThemeSettings.isDarkTheme)
                    Color(0xFF111827)
                else
                    Color.White
        )
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Surface(
                modifier = Modifier.size(24.dp),

                shape = CircleShape,

                color = PrimaryPurple.copy(alpha = 0.12f)
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = emoji,

                        color = PrimaryPurple,

                        fontWeight = FontWeight.ExtraBold,

                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = text,

                color =
                    if (ThemeSettings.isDarkTheme)
                        Color.White
                    else
                        Color(0xFF111827),

                fontWeight = FontWeight.SemiBold,

                fontSize = 14.sp,

                maxLines = 1
            )
        }
    }
}
