package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val SophisticatedColorScheme =
  darkColorScheme(
    primary = SophisticatedPrimary,
    onPrimary = SophisticatedOnPrimary,
    primaryContainer = SophisticatedPrimaryContainer,
    onPrimaryContainer = SophisticatedOnPrimaryContainer,
    background = SophisticatedBackground,
    onBackground = SophisticatedText,
    surface = SophisticatedBackground,
    onSurface = SophisticatedText,
    surfaceVariant = SophisticatedSurfaceVariant,
    onSurfaceVariant = SophisticatedOnSurfaceVariant,
    outline = SophisticatedOutline,
    error = SophisticatedError,
    errorContainer = SophisticatedErrorContainer,
    onErrorContainer = SophisticatedOnErrorContainer
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      else -> SophisticatedColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
