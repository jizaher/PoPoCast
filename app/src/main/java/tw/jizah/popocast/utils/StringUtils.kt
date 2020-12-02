package tw.jizah.popocast.utils

import android.text.Spanned
import androidx.core.text.HtmlCompat

object StringUtils {
    fun styleHtmlText(text: String): Spanned {
        val htmlLineBreak = "<br>"
        return HtmlCompat.fromHtml(
            text.replace("\n", htmlLineBreak),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }
}
