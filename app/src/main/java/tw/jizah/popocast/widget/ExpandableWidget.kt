package tw.jizah.popocast.widget

import android.text.TextUtils
import android.text.util.Linkify
import android.view.ViewGroup
import android.widget.TextView
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.TextViewCompat
import tw.jizah.popocast.R
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.utils.StringUtils

@Composable
fun ExpandableWidget(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier.fillMaxWidth()
            .animateContentSize()
    ) {
        content()
    }
}

@Composable
fun ExpandableText(
    text: String,
    maxLines: Int = Integer.MAX_VALUE,
    expandedState: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    fun toggleDescription() {
        expandedState.value = !expandedState.value
    }

    ExpandableWidget(content = {
        AndroidView(viewBlock = { context ->
            TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                TextViewCompat.setTextAppearance(this, R.style.TextAppearance_App_Body2)
                ellipsize = TextUtils.TruncateAt.END
                autoLinkMask = Linkify.ALL
                setTextColor(Colors.gray400.toArgb())
                setLinkTextColor(Colors.white.toArgb())
                setOnTouchListener(ClickTextTouchListener())
            }
        }, modifier = modifier) { textView ->
            textView.maxLines = if (expandedState.value) Integer.MAX_VALUE else maxLines
            textView.text = StringUtils.styleHtmlText(text = text)
        }

        CenterRow(modifier = Modifier.clickable(onClick = { toggleDescription() })) {
            val (expandText, icon) = if (expandedState.value) {
                stringResource(id = R.string.see_less) to Icons.Filled.ExpandLess
            } else {
                stringResource(id = R.string.see_more) to Icons.Filled.ExpandMore
            }
            Text(
                text = expandText,
                color = Colors.white,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = { toggleDescription() }) {
                Icon(icon, tint = Colors.white)
            }
        }
    })
}