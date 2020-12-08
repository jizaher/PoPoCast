package tw.jizah.popocast.extensions


import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

fun ImageVector.resize(size: Dp) = resize(size, size)
fun ImageVector.resize(width: Dp, height: Dp) = copy(defaultWidth = width, defaultHeight = height)