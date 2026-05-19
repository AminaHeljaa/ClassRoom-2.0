package com.example.classroom20

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.classroom20.data.FirebaseManager
import com.example.classroom20.ui.navigation.ClassroomNavGraph
import com.example.classroom20.ui.theme.Classroom20Theme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var languageCode by remember { mutableStateOf("bs") }
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                FirebaseManager.listenToUserLanguage { code ->
                    if (code != languageCode) {
                        languageCode = code
                        val locale = Locale(code)
                        Locale.setDefault(locale)
                        val config = context.resources.configuration
                        config.setLocale(locale)
                        context.resources.updateConfiguration(config, context.resources.displayMetrics)
                    }
                }
            }

            Classroom20Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    ClassroomNavGraph(navController = navController)
                }
            }
        }
    }
}
