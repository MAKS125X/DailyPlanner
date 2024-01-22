package com.example.dailyplaner.presentation.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Blue,
    secondary = SkyBlue,
    tertiary = LightBlue,
    background = Color.White,
    surface = Color.White,
    error = Color.Red,
    errorContainer = Color.Red,

    onPrimary = Color.White,
    onSecondary = Blue,
    onTertiary = Blue,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onSurfaceVariant = Blue,
    onError = Color.White,
    onErrorContainer = Color.White,
)

@Composable
fun DailyPlanerTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DailyPlannerTypography,
        content = content
    )
}