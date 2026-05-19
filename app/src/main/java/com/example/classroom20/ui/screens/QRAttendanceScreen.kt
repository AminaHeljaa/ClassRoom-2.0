package com.example.classroom20.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom20.R
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Subject
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.ui.components.QRScannerView
import com.example.classroom20.ui.theme.PrimaryPurple
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.delay

@Composable
fun QRAttendanceScreen(
    activeSubject: Subject?,
    onBack: () -> Unit,
    onScanSuccess: (Subject) -> Unit = {}
) {

    var userRole by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    LaunchedEffect(Unit) {

        FirebaseManager.getCurrentUser { user ->

            if (user != null) {
                userRole = user.role
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
                            Color(0xFFF8FAFF),
                            Color(0xFFF3F4FF),
                            Color.White
                        )
                    )
                }
            )
    ) {

        // GORNJI KRUG
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = (-110).dp, y = (-110).dp)
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
                .offset(x = 80.dp, y = 80.dp)
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
                title = stringResource(R.string.qr_attendance),
                onBack = onBack
            )

            Text(
                text = "Digitalno evidentiranje prisustva",

                color = PrimaryPurple,

                fontSize = 13.sp,

                fontWeight = FontWeight.Medium,

                textAlign = TextAlign.Center,

                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (activeSubject == null) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = stringResource(R.string.select_subject_dashboard),

                        color = Color.Gray,

                        textAlign = TextAlign.Center
                    )
                }

            } else {

                Card(
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(28.dp),

                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme())
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                        else
                            Color.White
                    ),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Aktivni predmet",

                            color = Color.Gray,

                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = activeSubject.name,

                            color = MaterialTheme.colorScheme.onSurface,

                            fontWeight = FontWeight.Bold,

                            fontSize = 22.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Profesor: ${activeSubject.teacherName}",

                            color = Color.Gray,

                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                if (userRole == "teacher") {

                    TeacherQRView(activeSubject)

                } else if (userRole == "student") {

                    StudentQRScannerView(
                        activeSubject,
                        onScanSuccess = {
                            onScanSuccess(activeSubject)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TeacherQRView(subject: Subject) {

    var durationMinutes by remember {
        mutableStateOf("5")
    }

    var timeLeft by remember {
        mutableIntStateOf(0)
    }

    var isGenerated by remember {
        mutableStateOf(false)
    }

    var qrTimestamp by remember {
        mutableLongStateOf(0L)
    }

    var qrData by remember {
        mutableStateOf("")
    }

    // Provjera postojeće sesije pri pokretanju
    LaunchedEffect(Unit) {
        FirebaseManager.getActiveAttendanceSession(subject.id) { timestamp, duration ->
            if (timestamp != null && duration != null) {
                val now = System.currentTimeMillis()
                val elapsedSeconds = (now - timestamp) / 1000
                val totalSeconds = duration * 60
                
                if (elapsedSeconds < totalSeconds) {
                    qrTimestamp = timestamp
                    durationMinutes = duration.toString()
                    qrData = "QR: ${subject.id}|$qrTimestamp|$duration"
                    timeLeft = (totalSeconds - elapsedSeconds).toInt()
                    isGenerated = true
                }
            }
        }
    }

    LaunchedEffect(isGenerated) {
        if (isGenerated && qrTimestamp == 0L) {
            // Samo ako kreiramo novu, inače koristimo postojeću iz gornjeg LaunchedEffect
            val duration = durationMinutes.toIntOrNull() ?: 5
            FirebaseManager.startAttendanceSession(subject.id, duration) { success ->
                if (success) {
                    qrTimestamp = System.currentTimeMillis()
                    qrData = "QR: ${subject.id}|$qrTimestamp|$duration"
                    timeLeft = duration * 60
                }
            }
        }

        if (isGenerated) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            isGenerated = false
            qrTimestamp = 0L
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (!isGenerated) {

                OutlinedTextField(
                    value = durationMinutes,

                    onValueChange = {
                        durationMinutes = it
                    },

                    label = {
                        Text("Trajanje QR-a (minute)")
                    },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(18.dp),

                    singleLine = true
                )

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = {
                        isGenerated = true
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),

                    shape = RoundedCornerShape(18.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryPurple
                    )
                ) {

                    Text(
                        text = "Generiši QR kod",

                        color = Color.White,

                        fontWeight = FontWeight.Bold,

                        fontSize = 15.sp
                    )
                }

            } else {

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .clip(RoundedCornerShape(34.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        PrimaryPurple.copy(alpha = 0.08f),
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                            .border(
                                2.dp,
                                PrimaryPurple.copy(alpha = 0.25f),
                                RoundedCornerShape(34.dp)
                            )
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Surface(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(190.dp),

                            shape = RoundedCornerShape(28.dp),

                            color = Color.White,

                            shadowElevation = 8.dp
                        ) {

                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                if (qrData.isNotEmpty()) {
                                    val bitmap = remember(qrData) {
                                        generateQRCode(qrData)
                                    }
                                    if (bitmap != null) {
                                        Image(
                                            bitmap = bitmap.asImageBitmap(),
                                            contentDescription = "QR Code",
                                            modifier = Modifier.size(150.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.QrCode,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(150.dp)
                                        )
                                    }
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(150.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "QR kod aktivan",

                            color = PrimaryPurple,

                            fontWeight = FontWeight.Bold,

                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                val minutes = timeLeft / 60
                val seconds = timeLeft % 60

                Text(
                    text = String.format(
                        "%02d:%02d",
                        minutes,
                        seconds
                    ),

                    color =
                        if (timeLeft < 60)
                            Color.Red
                        else
                            Color(0xFF16A34A),

                    fontSize = 34.sp,

                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Vrijeme do zaključavanja",

                    color = Color.Gray,

                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun StudentQRScannerView(
    subject: Subject,
    onScanSuccess: () -> Unit
) {

    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Surface(
                modifier = Modifier.size(90.dp),

                shape = CircleShape,

                color = PrimaryPurple.copy(alpha = 0.10f)
            ) {

                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = null,

                    tint = PrimaryPurple,

                    modifier = Modifier.padding(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Skeniraj QR kod",

                color = MaterialTheme.colorScheme.onSurface,

                fontWeight = FontWeight.Bold,

                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kliknite ispod da evidentirate prisustvo na času.",

                color = Color.Gray,

                textAlign = TextAlign.Center,

                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            QRScannerView(
                onScan = { qrData ->
                    try {
                        val parts = qrData.split("|")
                        val scannedId = parts[0].replace("QR: ", "").trim()
                        
                        // PROVJERA: Da li je skenirani predmet isti kao aktivni predmet na dashboardu?
                        if (scannedId != subject.id) {
                            Toast.makeText(context, "Greška: Ovaj QR kod nije za predmet ${subject.name}!", Toast.LENGTH_LONG).show()
                        } else {
                            // Prvo dohvaćamo svježe podatke o sesiji iz baze da budemo sigurni
                            FirebaseManager.getActiveAttendanceSession(scannedId) { sessionTimestamp, sessionDuration ->
                                if (sessionTimestamp != null && sessionDuration != null) {
                                    FirebaseManager.recordAttendance(
                                        scannedId,
                                        sessionTimestamp,
                                        subject.teacherName,
                                        subject.name,
                                        sessionDuration
                                    ) { success, msg ->
                                        if (success) {
                                            Toast.makeText(context, "Prisustvo zabilježeno!", Toast.LENGTH_LONG).show()
                                            onScanSuccess()
                                        } else {
                                            Toast.makeText(context, msg ?: "Greška", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } else {
                                    // Ako nema sesije u bazi, pokušavamo sa podacima iz QR koda kao fallback
                                    val qrTimestamp = parts[1].toLong()
                                    val qrDuration = if (parts.size > 2) parts[2].toInt() else 5
                                    
                                    FirebaseManager.recordAttendance(
                                        scannedId,
                                        qrTimestamp,
                                        subject.teacherName,
                                        subject.name,
                                        qrDuration
                                    ) { success, msg ->
                                        if (success) {
                                            Toast.makeText(context, "Prisustvo zabilježeno!", Toast.LENGTH_LONG).show()
                                            onScanSuccess()
                                        } else {
                                            Toast.makeText(context, msg ?: "Greška", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, context.getString(R.string.invalid_qr), Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}

fun generateQRCode(content: String): Bitmap? {
    return try {
        val hints = mapOf(EncodeHintType.MARGIN to 1)
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        null
    }
}
