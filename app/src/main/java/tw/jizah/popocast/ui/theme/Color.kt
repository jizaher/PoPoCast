package tw.jizah.popocast.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val purple200 = Color(0xFFBB86FC)
val purple500 = Color(0xFF6200EE)
val purple700 = Color(0xFF3700B3)
val teal200 = Color(0xFF03DAC5)

val gray50 = Color(0xFFFAFAFA)
val gray100 = Color(0xFFF5F5F5)
val gray200 = Color(0xFFEEEEEE)
val gray300 = Color(0xFFE0E0E0)
val gray400 = Color(0xFFBDBDBD)
val gray500 = Color(0xFF9E9E9E)
val gray600 = Color(0xFF757575)
val gray700 = Color(0xFF616161)
val gray800 = Color(0xFF424242)
val gray900 = Color(0xFF212121)


val playerControllerBgColor = gray900
val playerControllerOwnerTextColor = gray500


@Composable
fun Colors.compositedOnSurface(alpha: Float): Color {
    return onSurface.copy(alpha = alpha).compositeOver(surface)
}

val Yellow800 = Color(0xFFF29F05)
val Red300 = Color(0xFFEA6D7E)

val PoPoCastColors = darkColors(
    primary = Yellow800,
    onPrimary = Color.Black,
    primaryVariant = Yellow800,
    secondary = Yellow800,
    onSecondary = Color.Black,
    error = Red300,
    onError = Color.Black
)