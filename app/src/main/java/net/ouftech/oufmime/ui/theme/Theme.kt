package net.ouftech.oufmime.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val InvertColorPalette = darkColors(
    primary = Accent,
    primaryVariant = AccentTransparent,
    secondary = Primary,
)

private val LightColorPalette = lightColors(
    primary = Primary,
    primaryVariant = PrimaryDark,
    secondary = Accent,
)

@Composable
fun OufMimeTheme(
    invert: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (invert) {
        InvertColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
