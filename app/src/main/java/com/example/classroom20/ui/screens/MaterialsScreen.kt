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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classroom20.R
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.data.Material
import com.example.classroom20.data.Subject
import com.example.classroom20.data.User
import com.example.classroom20.ui.components.AppHeader
import com.example.classroom20.ui.theme.PrimaryPurple

@Composable
fun MaterialsScreen(
    activeSubject: Subject?,
    onBack: () -> Unit
) {
    var user by remember { mutableStateOf<User?>(null) }
    var materials by remember { mutableStateOf<List<Material>>(emptyList()) }
    var showAddDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        FirebaseManager.getCurrentUser { user = it }
    }

    LaunchedEffect(activeSubject) {
        if (activeSubject != null) {
            FirebaseManager.getMaterialsForSubject(activeSubject.id) { materials = it }
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Dekoracije
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .offset(x = (-100).dp, y = (-100).dp)
                    .background(PrimaryPurple.copy(alpha = 0.05f), CircleShape)
            )

            Scaffold(
                containerColor = Color.Transparent,
                floatingActionButton = {
                    if (user?.role == "teacher" && activeSubject != null) {
                        FloatingActionButton(
                            onClick = { showAddDialog = true },
                            containerColor = PrimaryPurple,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(22.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
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
                    AppHeader(title = "Materijali", onBack = onBack)

                    Text(
                        text = activeSubject?.name ?: "",
                        color = PrimaryPurple,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 26.dp)
                    )

                    if (materials.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Nema dostupnih materijala", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 100.dp)
                        ) {
                            items(materials) { material ->
                                MaterialCard(
                                    material = material,
                                    isTeacher = user?.role == "teacher",
                                    onDelete = {
                                        FirebaseManager.deleteMaterial(material.id) {
                                            // Refresh auto-handled by snapshot listener
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showAddDialog && activeSubject != null) {
            AddMaterialDialog(
                subjectId = activeSubject.id,
                onDismiss = { showAddDialog = false }
            )
        }
    }
}

@Composable
fun MaterialCard(material: Material, isTeacher: Boolean, onDelete: () -> Unit) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(material.fileUrl))
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(14.dp),
                color = PrimaryPurple.copy(alpha = 0.1f)
            ) {
                Icon(
                    Icons.Default.Description,
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = material.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = material.fileName,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            if (isTeacher) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red.copy(alpha = 0.7f))
                }
            }

            Icon(
                Icons.Default.OpenInNew,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun AddMaterialDialog(subjectId: String, onDismiss: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedUri = uri
            fileName = uri.lastPathSegment ?: "dokument"
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novi materijal", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Naslov") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { launcher.launch("*/*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple.copy(alpha = 0.1f), contentColor = PrimaryPurple)
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (selectedUri == null) "Izaberi fajl" else fileName)
                }

                if (isUploading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = PrimaryPurple)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && selectedUri != null) {
                        isUploading = true
                        FirebaseManager.uploadMaterial(selectedUri!!, fileName, title, subjectId) { success ->
                            isUploading = false
                            if (success) {
                                onDismiss()
                            } else {
                                Toast.makeText(context, "Greška pri slanju", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                enabled = !isUploading && title.isNotBlank() && selectedUri != null,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Text("Objavi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Otkaži") }
        }
    )
}
