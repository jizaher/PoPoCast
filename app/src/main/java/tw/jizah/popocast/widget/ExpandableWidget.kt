package tw.jizah.popocast.widget

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ExpandableWidget(
    expandedState: MutableState<Boolean> = remember { mutableStateOf(false) },
    modifier: Modifier = Modifier,
    expandedContent: @Composable () -> Unit,
    collapsedContent: @Composable () -> Unit
) {
    Column(
        modifier.fillMaxWidth()
            .animateContentSize()
    ) {
        if (expandedState.value) {
            expandedContent()
        } else {
            collapsedContent()
        }
    }
}