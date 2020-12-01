package tw.jizah.popocast.extensions


import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.unit.Dp

fun VectorAsset.resize(size: Dp) = resize(size, size)
fun VectorAsset.resize(width: Dp, height: Dp) = copy(defaultWidth = width, defaultHeight = height)