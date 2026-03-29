package com.example.studentcopilot.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PrimaryColor = Color(0xFF6750A4) // Purple
private val SecondaryColor = Color(0xFF625B71) // Purple-gray
private val TertiaryColor = Color(0xFF7D5260) // Pink
private val BackgroundColor = Color(0xFFFFFBFE)
private val ErrorColor = Color(0xFFB3261E)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = BackgroundColor,
    error = ErrorColor
)

@Composable
fun StudentCopilotTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
