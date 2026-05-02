package com.example.proyecto.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ModernDarkColorScheme = darkColorScheme(
    primary = ElectricBlue,
    secondary = NeonCyan,
    tertiary = CyberPurple,
    background = DeepSpace,
    surface = SurfaceDark,
    onBackground = OnSurfaceMain,
    onSurface = OnSurfaceMain,
    onSurfaceVariant = OnSurfaceSecondary,
    primaryContainer = GlassSurface,
    onPrimaryContainer = OnSurfaceMain
)

private val ModernLightColorScheme = lightColorScheme(
    primary = ElectricBlue,
    secondary = ElectricBlue,
    tertiary = CyberPurple,
    background = Color(0xFFF9FAFB),
    surface = Color.White,
    onBackground = Color(0xFF111827),
    onSurface = Color(0xFF111827),
    onSurfaceVariant = Color(0xFF6B7280),
    primaryContainer = Color(0xFFEFF6FF),
    onPrimaryContainer = ElectricBlue
)

@Composable
fun ProyectoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ModernDarkColorScheme else ModernLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
