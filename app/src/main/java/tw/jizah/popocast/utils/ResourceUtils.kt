package tw.jizah.popocast.utils

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ContextAmbient

@Composable
fun quantityStringResource(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String {
    val context = ContextAmbient.current
    return context.resources.getQuantityString(id, quantity, *formatArgs)
}
