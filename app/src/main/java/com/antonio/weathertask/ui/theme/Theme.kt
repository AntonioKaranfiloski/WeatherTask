package com.antonio.weathertask.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat


@Composable
fun WeatherTaskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF80CBC4), // Light red for primary
            onPrimary = Color(0xFFFFFFFF), // White on primary
            secondary = Color(0xFF81D4FA), // Light blue for secondary
            onSecondary = Color(0xFFFFFFFF), // White on secondary
            tertiary = Color(0xFFA5D6A7), // Bright green for tertiary
            onTertiary = Color(0xFF212121), // Black on tertiary
            background = Color(0xFFFFFBFE), // Light background
            onBackground = Color(0xFF212121), // Black on background
            surface = Color(0xFFFFFBFE), // Light surface
            onSurface = Color(0xFF212121), // Black on surface
            error = Color(0xFFF44336), // Light red for error
            onError = Color(0xFFFFFFFF), // White on error
            primaryContainer = Color(0xFFE0F7FA)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF80CBC4), // Light red for primary
            onPrimary = Color(0xFFFFFFFF), // White on primary
            secondary = Color(0xFF81D4FA), // Light blue for secondary
            onSecondary = Color(0xFFFFFFFF), // White on secondary
            tertiary = Color(0xFFA5D6A7), // Bright green for tertiary
            onTertiary = Color(0xFF212121), // Black on tertiary
            background = Color(0xFFFFFBFE), // Light background
            onBackground = Color(0xFF212121), // Black on background
            surface = Color(0xFFFFFBFE), // Light surface
            onSurface = Color(0xFF212121), // Black on surface
            error = Color(0xFFF44336), // Light red for error
            onError = Color(0xFFFFFFFF), // White on error
            primaryContainer = Color(0xFFE0F7FA)
        )
    }
    val typography = Typography(
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        // Add more typography styles as needed for customization
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(8.dp), // Slightly more rounded corners for medium components
        large = RoundedCornerShape(0.dp) // Keeping large components without rounded corners
    )

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}