package com.example.classroom20.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.classroom20.ui.theme.PrimaryPurple
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun QRScannerView(onScan: (String) -> Unit) {
    val context = LocalContext.current
    val scanner = remember { GmsBarcodeScanning.getClient(context) }

    Button(
        onClick = {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    val rawValue: String? = barcode.rawValue
                    if (rawValue != null) {
                        onScan(rawValue)
                    }
                }
                .addOnFailureListener { e ->
                    // Nemoj prikazivati Toast ako je korisnik sam otkazao skeniranje (nazad dugme)
                    // Toast.makeText(context, "Skeniranje otkazano ili greška", Toast.LENGTH_SHORT).show()
                }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
    ) {
        Text("Skeniraj QR Kod", color = Color.White, fontWeight = FontWeight.Bold)
    }
}
