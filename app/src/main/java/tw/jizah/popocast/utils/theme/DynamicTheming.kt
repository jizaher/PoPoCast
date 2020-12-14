package tw.jizah.popocast.utils.theme

import android.content.Context
import androidx.compose.animation.animate
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext

@Composable
fun DynamicThemePrimaryColorsFromImage(
    dominantColorState: DominantColorState = rememberDominantColorState(),
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colors.copy(
        primary = animate(dominantColorState.color, spring(stiffness = Spring.StiffnessLow)),
    )
    MaterialTheme(colors = colors, content = content)
}

@Composable
fun rememberDominantColorState(
    context: Context = AmbientContext.current,
    defaultColor: Color = MaterialTheme.colors.primary
): DominantColorState = remember {
    DominantColorState(context, defaultColor)
}