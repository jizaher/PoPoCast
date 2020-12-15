package tw.jizah.popocast.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.VerticalGradient

fun Modifier.verticalGradientScrim(
    color: Color
) = composed {
    val colors = listOf(color.copy(alpha = 0f), color)
    var height by remember { mutableStateOf(0f) }
    val brush = remember(color, height) {
        VerticalGradient(
            startY = height,
            endY = 0.0F,
            colors = colors
        )
    }
    drawBehind {
        height = size.height
        drawRect(brush = brush)
    }
}