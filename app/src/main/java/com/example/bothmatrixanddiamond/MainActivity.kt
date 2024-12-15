package com.example.combinedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CombinedAppTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun CombinedAppTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }

    when (currentScreen) {
        Screen.HOME -> HomeScreen(
            onNavigateToDiamond = { currentScreen = Screen.DIAMOND },
            onNavigateToMatrix = { currentScreen = Screen.MATRIX }
        )
        Screen.DIAMOND -> DiamondApp(onBack = { currentScreen = Screen.HOME })
        Screen.MATRIX -> MatrixApp(onBack = { currentScreen = Screen.HOME })
    }
}
