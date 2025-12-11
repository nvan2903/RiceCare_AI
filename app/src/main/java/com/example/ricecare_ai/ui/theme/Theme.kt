package com.example.ricecare_ai.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = Gray100,
    onPrimaryContainer = Black,
    secondary = Gray700,
    onSecondary = White,
    secondaryContainer = Gray200,
    onSecondaryContainer = Gray900,
    tertiary = Gray600,
    onTertiary = White,
    tertiaryContainer = Gray300,
    onTertiaryContainer = Gray900,
    error = DangerRed,
    onError = White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray700,
    outline = Gray400,
    outlineVariant = Gray300,
    scrim = Black,
    inverseSurface = Gray900,
    inverseOnSurface = White,
    inversePrimary = Gray300,
)

private val DarkColorScheme = darkColorScheme(
    primary = White,
    onPrimary = Black,
    primaryContainer = Gray800,
    onPrimaryContainer = White,
    secondary = Gray300,
    onSecondary = Gray900,
    secondaryContainer = Gray700,
    onSecondaryContainer = Gray200,
    tertiary = Gray400,
    onTertiary = Gray900,
    tertiaryContainer = Gray600,
    onTertiaryContainer = Gray200,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White,
    surfaceVariant = Gray900,
    onSurfaceVariant = Gray300,
    outline = Gray600,
    outlineVariant = Gray700,
    scrim = Black,
    inverseSurface = Gray100,
    inverseOnSurface = Gray900,
    inversePrimary = Gray700,
)

@Composable
fun RiceCare_AITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}