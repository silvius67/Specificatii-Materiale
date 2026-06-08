package ro.subcontrol.decizii.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DarkColors = darkColorScheme(
    primary         = Blue500,
    onPrimary       = Navy100,
    background      = Navy900,
    onBackground    = Navy100,
    surface         = Navy800,
    onSurface       = Navy100,
    surfaceVariant  = Navy700,
    onSurfaceVariant= Navy300,
    outline         = Navy700
)

@Composable
fun SubcontrolTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        content     = content
    )
}
