package com.example.bothmatrixanddiamond

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
@Composable
fun HomeScreen(onNavigateToDiamond: () -> Unit, onNavigateToMatrix: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Choose a Program", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToDiamond, modifier = Modifier.fillMaxWidth()) {
            Text("Diamond Program")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToMatrix, modifier = Modifier.fillMaxWidth()) {
            Text("Matrix Program")
        }
    }
}
@Composable
fun DiamondApp(onBack: () -> Unit) {
    var input by remember { mutableStateOf(TextFieldValue("")) }
    var diamondOutput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = input,
            onValueChange = {
                input = it
            },
            label = { Text("Enter a positive number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val number = input.text.toIntOrNull()
            diamondOutput = if (number != null && number > 0) {
                diamondGen(number.toString())
            } else {
                "Please enter a valid positive number."
            }
        }) {
            Text("Generate Diamond")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = diamondOutput, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

fun diamondGen (input: String): String {
    val diamond = input.toIntOrNull() ?: return "Invalid input"
    if (diamond <= 0) return "Please enter a positive number"

    val diamondBuilder = StringBuilder()
    val evenNum = diamond % 2 == 0

    if (evenNum) {
        // Top single star
        val topSpace = (diamond / 2) - 1
        diamondBuilder.append("  ".repeat(topSpace)).append("  *").appendLine()

        // Top half of the diamond
        for (i in 1..(diamond / 2)) {
            val spaces = (diamond/ 2) - i
            val stars = 2 * (i + 1) - 2

            diamondBuilder.append("  ".repeat(spaces))
            diamondBuilder.append(" *".repeat(stars)).appendLine()
        }

        // Bottom half of the diamond
        for (i in 0 until (diamond / 2) - 1) {
            val spaces = i + 1
            val stars = diamond - 2 * (i + 1)

            diamondBuilder.append("  ".repeat(spaces))
            diamondBuilder.append(" *".repeat(stars)).appendLine()
        }

        // Bottom single star
        diamondBuilder.append("  ".repeat(topSpace)).append("  *").appendLine()
    } else {
        // Top half of the diamond
        for (i in 0..(diamond / 2)) {
            val spaces = (diamond / 2) - i
            val stars = 2 * i + 1

            diamondBuilder.append(" ".repeat(spaces))
            diamondBuilder.append("* ".repeat(stars).trimEnd()).appendLine()
        }

        // Bottom half of the diamond
        for (i in (diamond / 2) - 1 downTo 0) {
            val spaces = (diamond / 2) - i
            val stars = 2 * i + 1

            diamondBuilder.append(" ".repeat(spaces))
            diamondBuilder.append("* ".repeat(stars).trimEnd()).appendLine()
        }
    }

    return diamondBuilder.toString().trimEnd()
}
@Composable
fun MatrixApp(onBack: () -> Unit) {
    var inputText by remember { mutableStateOf("") }
    var matrixSize by remember { mutableStateOf(0) }
    var generatedMatrix by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = inputText,
            onValueChange = {
                inputText = it
                matrixSize = it.toIntOrNull() ?: 0
                errorMessage = if (matrixSize <= 0) "Please enter a positive integer." else ""
            },
            label = { Text("Enter matrix size") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (matrixSize > 0) {
                generatedMatrix = createMatrix(matrixSize)
                errorMessage = ""
            } else {
                errorMessage = "Please enter a valid matrix size."
            }
        }) {
            Text("Generate Matrix")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        } else if (generatedMatrix.isNotEmpty()) {
            Text(text = generatedMatrix, modifier = Modifier.fillMaxWidth())
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

private fun createMatrix(size: Int): String {
    if (size <= 0) {
        return "Invalid matrix size"
    }

    val defaultMatrix = generateDefaultMatrix(size)
    val numberedMatrix = generateNumberedMatrix(size)
    val flippedMatrix = generateFlippedMatrix(size)

    return "Default Matrix:\n$defaultMatrix\n" +
            "Numbered Matrix:\n$numberedMatrix\n" +
            "Flipped Matrix:\n$flippedMatrix"
}

private fun generateDefaultMatrix(size: Int): String {
    val builder = StringBuilder()
    for (row in 0 until size) {
        for (col in 0 until size) {
            //cant figure out coloring in this app, its not working
            val value = if (row + col == size - 1) "()" else "0"
            builder.append(value).append(" ")
        }
        builder.append("\n")
    }
    return builder.toString()
}

private fun generateNumberedMatrix(size: Int): String {
    val builder = StringBuilder()
    var count = 1
    for (row in 0 until size) {
        for (col in 0 until size) {
            val value = if (row + col == size - 1) "()_$count" else "$count"
            builder.append(value).append(" ")
            count++
        }
        builder.append("\n")
    }
    return builder.toString()
}

private fun generateFlippedMatrix(size: Int): String {
    val builder = StringBuilder()
    val maxNumber = size * size
    var count = maxNumber
    for (row in 0 until size) {
        for (col in 0 until size) {
            val value = if (row + col == size - 1) "()_$count" else "$count"
            builder.append(value).append(" ")
            count--
        }
        builder.append("\n")
    }
    return builder.toString()
}
enum class Screen {
    HOME, DIAMOND, MATRIX
}
