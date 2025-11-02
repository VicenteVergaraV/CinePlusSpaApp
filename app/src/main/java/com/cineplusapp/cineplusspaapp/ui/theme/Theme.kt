package com.cineplusapp.cineplusspaapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Blanco reutilizable
private val ColorWhite = androidx.compose.ui.graphics.Color(0xFFFFFFFF)

private val DarkColors = darkColorScheme(
    primary = Accent,
    secondary = AccentVariant,
    tertiary = Teal,
    background = Navy,
    surface = Blue,
    error = Error
)

private val LightColors = lightColorScheme(
    primary = LightBlue,
    secondary = Accent,
    tertiary = Teal,
    background = ColorWhite,
    surface = ColorWhite,
    error = Error
)


@Composable
fun CinePlusSPAAPPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (darkTheme) DarkColors else LightColors
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TypographyDefault,
        content = content
    )
}
