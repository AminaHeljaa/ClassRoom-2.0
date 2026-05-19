package com.example.classroom20.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom20.R
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.User
import com.example.classroom20.ui.theme.PrimaryPurple
import com.example.classroom20.ui.theme.PurpleGradient
import com.example.classroom20.ui.theme.ThemeSettings

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

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

        // Glow efekti

        Box(
            modifier = Modifier
                .size(240.dp)
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

            // HEADER

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                Column {

                    Text(
                        text = stringResource(R.string.create_account),
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
            }

            Spacer(modifier = Modifier.height(40.dp))

            // REGISTER CARD

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
                        text = stringResource(R.string.register),
                        color =
                            if (ThemeSettings.isDarkTheme)
                                Color.White
                            else
                                Color(0xFF111827),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // IME

                    OutlinedTextField(
                        value = firstName,
                        onValueChange = {
                            firstName = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(stringResource(R.string.first_name))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
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

                    // PREZIME

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = {
                            lastName = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(stringResource(R.string.last_name))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
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

                    // BUTTON

                    Button(
                        onClick = {

                            if (
                                firstName.isNotBlank() &&
                                lastName.isNotBlank() &&
                                email.isNotBlank() &&
                                password.isNotBlank()
                            ) {

                                isLoading = true

                                val newUser = User(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email
                                )

                                FirebaseManager.registerUser(
                                    newUser,
                                    password
                                ) { success, error ->

                                    isLoading = false

                                    if (success) {

                                        Toast.makeText(
                                            context,
                                            "Registracija uspješna. Prijavite se.",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        onRegisterSuccess()

                                    } else {

                                        Toast.makeText(
                                            context,
                                            error ?: "Greška pri registraciji",
                                            Toast.LENGTH_LONG
                                        ).show()
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
                                    text = stringResource(R.string.register),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // LOGIN LINK

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(R.string.already_have_account),
                    color =
                        if (ThemeSettings.isDarkTheme)
                            Color.LightGray
                        else
                            Color.Gray
                )

                Text(
                    text = " " + stringResource(R.string.signin),
                    color = PrimaryPurple,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        onBackToLogin()
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}