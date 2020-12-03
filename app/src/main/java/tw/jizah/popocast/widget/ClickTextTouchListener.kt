package tw.jizah.popocast.widget

import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.TextView

class ClickTextTouchListener : OnTouchListener {

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_UP) {
            return false
        }

        val widget = v as TextView
        val text = widget.text
        if (text is Spanned) {
            var x = event.x
            var y = event.y

            x -= widget.totalPaddingLeft
            y -= widget.totalPaddingTop

            x += widget.scrollX
            y += widget.scrollY

            val layout = widget.layout
            val line = layout.getLineForVertical(y.toInt())
            val off = layout.getOffsetForHorizontal(line, x)

            val links = text.getSpans(off, off, ClickableSpan::class.java)

            if (links.isNotEmpty()) {
                links[0].onClick(widget)
            }
        }
        return true
    }
}