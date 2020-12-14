package tw.jizah.popocast.utils.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DominantColorState(
    private val context: Context,
    private val defaultColor: Color,
) {

    private val requestImageSize = 128
    private val maximumPaletteColorCount = 8

    var color by mutableStateOf(defaultColor)
        private set

    suspend fun updateColorsFromImageUrl(url: String) {
        val result = calculateDominantColor(url)
        color = result?.color ?: defaultColor
    }

    private suspend fun calculateDominantColor(url: String): DominantColors? {
        val cached = DominantColorCache.getColors(url)
        if (cached != null) {
            return cached
        }

        return calculateSwatchesInImage(context, url)
            .maxByOrNull { swatch -> swatch.population }
            ?.let { swatch -> DominantColors(color = Color(swatch.rgb)) }
            ?.also { result -> DominantColorCache.setColors(url, result) }
    }

    private suspend fun calculateSwatchesInImage(
        context: Context,
        imageUrl: String
    ): List<Palette.Swatch> {
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .size(requestImageSize)
            .scale(Scale.FILL)
            // Disable hardware bitmaps, since Palette uses Bitmap.getPixels()
            .allowHardware(false)
            .build()

        val bitmap = when (val result = Coil.execute(request)) {
            is SuccessResult -> result.drawable.toBitmap()
            else -> null
        }

        return bitmap?.let {
            withContext(Dispatchers.Default) {
                val palette = Palette.Builder(bitmap)
                    // Disable any bitmap resizing in Palette.
                    .resizeBitmapArea(0)
                    .maximumColorCount(maximumPaletteColorCount)
                    .generate()
                palette.swatches
            }
        } ?: emptyList()
    }
}