package com.example.classroom20.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(
    onTimeout: () -> Unit
) {

    var startAnimation by remember {
        mutableStateOf(false)
    }

    // LOGO SCALE
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.7f,

        animationSpec = tween(
            durationMillis = 1200,
            easing = FastOutSlowInEasing
        ),

        label = "logoScale"
    )

    // FADE
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,

        animationSpec = tween(1500),

        label = "alpha"
    )

    // FLOATING EFFECT
    val infiniteTransition = rememberInfiniteTransition(
        label = "floating"
    )

    val floating by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,

        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2800,
                easing = EaseInOut
            ),

            repeatMode = RepeatMode.Reverse
        ),

        label = "floating"
    )

    // GLOW
    val glow by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,

        animationSpec = infiniteRepeatable(
            animation = tween(2200),
            repeatMode = RepeatMode.Reverse
        ),

        label = "glow"
    )

    LaunchedEffect(Unit) {

        startAnimation = true

        delay(3200)

        onTimeout()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8FAFF),
                            Color(0xFFF1F5FF),
                            Color.White
                        )
                    )
                )
        ) {

            // TOP LEFT GLOW
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .align(Alignment.TopStart)
                    .offset(x = (-90).dp, y = (-60).dp)
                    .background(
                        Color(0xFF8B5CF6).copy(alpha = 0.10f),
                        CircleShape
                    )
            )

            // BOTTOM RIGHT GLOW
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 90.dp, y = 90.dp)
                    .background(
                        Color(0xFF2563EB).copy(alpha = 0.08f),
                        CircleShape
                    )
            )

            // LEFT LIGHT BAR
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(180.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = 18.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color(0xFF8B5CF6),
                                Color.Transparent
                            )
                        ),
                        RoundedCornerShape(30.dp)
                    )
            )

            // RIGHT LIGHT BAR
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .height(180.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-18).dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                Color(0xFF3B82F6),
                                Color.Transparent
                            )
                        ),
                        RoundedCornerShape(30.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .alpha(alpha),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // LOGO SECTION
                Box(
                    contentAlignment = Alignment.Center,

                    modifier = Modifier
                        .offset(y = floating.dp)
                        .scale(logoScale * glow)
                ) {

                    // OUTER GLOW
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF8B5CF6).copy(alpha = 0.16f),
                                        Color.Transparent
                                    )
                                ),
                                CircleShape
                            )
                    )

                    // MAIN ROUND CONTAINER
                    Surface(
                        modifier = Modifier.size(145.dp),

                        shape = CircleShape,

                        color = Color.White,

                        shadowElevation = 22.dp
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF8B5CF6),
                                            Color(0xFF6366F1),
                                            Color(0xFF3B82F6)
                                        )
                                    )
                                ),

                            contentAlignment = Alignment.Center
                        ) {

                            // INNER CIRCLE
                            Box(
                                modifier = Modifier
                                    .size(92.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.14f),
                                        CircleShape
                                    ),

                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.AutoStories,
                                    contentDescription = null,

                                    tint = Color.White,

                                    modifier = Modifier.size(44.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(42.dp))

                Text(
                    text = "Classroom 2.0",

                    color = Color(0xFF111827),

                    fontSize = 36.sp,

                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Interaktivna platforma za učenje",

                    color = Color(0xFF6B7280),

                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(30.dp),

                    color = Color(0xFF8B5CF6).copy(alpha = 0.10f)
                ) {

                    Text(
                        text = "Moderno • Pametno • Povezano",

                        color = Color(0xFF7C3AED),

                        fontSize = 13.sp,

                        fontWeight = FontWeight.Bold,

                        modifier = Modifier.padding(
                            horizontal = 18.dp,
                            vertical = 10.dp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(55.dp))

                CircularProgressIndicator(
                    color = Color(0xFF8B5CF6),
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}