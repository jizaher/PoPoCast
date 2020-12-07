package tw.jizah.popocast.utils

import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AmbientContext

@Composable
fun quantityStringResource(@PluralsRes id: Int, quantity: Int, vararg formatArgs: Any): String {
    val context = AmbientContext.current
    return context.resources.getQuantityString(id, quantity, *formatArgs)
}
