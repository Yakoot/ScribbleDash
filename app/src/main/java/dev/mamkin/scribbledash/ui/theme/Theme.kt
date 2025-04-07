package dev.mamkin.scribbledash.ui.theme

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
    primary = Primary,
    onPrimary = OnPrimary,
    secondary = Secondary,
    tertiary = TertiaryContainer,
    error = Error,
    background = Background,
    onBackground = OnBackground,
    surface = SurfaceHigh,
    surfaceVariant = SurfaceOpacity80,
    surfaceContainerLow = SurfaceLow,
    surfaceContainerLowest = SurfaceLowest,
    surfaceContainerHigh = SurfaceHigh,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant
)

@Composable
fun ScribbleDashTheme(
    content: @Composable () -> Unit
) {
//    val window = (LocalView.current.context as Activity).window
//    SideEffect {
//        window.statusBarColor = Color.Transparent.toArgb()
//        window.navigationBarColor = Color.Transparent.toArgb()
//    }


    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
