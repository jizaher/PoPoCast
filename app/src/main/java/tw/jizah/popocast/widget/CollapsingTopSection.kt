package tw.jizah.popocast.widget

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tw.jizah.popocast.ui.theme.Dimens

@Composable
fun CollapsingTopSection(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    topMaxHeightState: MutableState<Dp>,
    topBarSectionSlot: @Composable () -> Unit = { Spacer(modifier = Modifier.fillMaxWidth().height(0.dp)) },
    centerSectionSlot: @Composable () -> Unit = { Spacer(modifier = Modifier.fillMaxWidth().height(0.dp)) },
    stickySectionSlot: @Composable () -> Unit = { Spacer(modifier = Modifier.fillMaxWidth().height(0.dp)) },
    topSectionCollapseFactor: MutableState<Float>
) {
    Layout(
        modifier = modifier,
        content = {
            topBarSectionSlot()
            centerSectionSlot()
            stickySectionSlot()
        }
    ) { measurables, constraints ->
        val expandedTopBarIndex = 0
        val centerSectionIndex = 1
        val stickySectionIndex = 2
        val contentIndexList = listOf(expandedTopBarIndex, centerSectionIndex)

        val placeables = measurables.map { measurable -> measurable.measure(constraints) }
        val totalHeight = placeables.sumBy { it.height }
        topMaxHeightState.value = totalHeight.toDp()

        if (placeables[expandedTopBarIndex].height == 0) {
            topSectionCollapseFactor.value = 1F
        } else {
            val expandedSectionHeight = placeables[expandedTopBarIndex].height - Dimens.toolBarHeight.toPx()
            topSectionCollapseFactor.value = calculateTopSectionCollapsedFactor(expandedSectionHeight = expandedSectionHeight, scrollState)
        }

        val toolBarHeightDp =  if(placeables[expandedTopBarIndex].height == 0) 0.dp else Dimens.toolBarHeight
        val topContentHeight = totalHeight - toolBarHeightDp.toPx() - placeables[stickySectionIndex].height

        layout(placeables[0].width, totalHeight) {
            var offset = -scrollState.value.toInt()
            contentIndexList.forEach { index ->
                placeables[index].place(0, offset)
                offset += placeables[index].height
            }

            // place sticky section
            if (scrollState.value > topContentHeight) {
                placeables[stickySectionIndex].place(0, toolBarHeightDp.toPx().toInt())
            } else {
                placeables[stickySectionIndex].place(0, offset)
            }
        }
    }
}

private fun calculateTopSectionCollapsedFactor(expandedSectionHeight: Float, scrollState: ScrollState): Float {
    val alphaValue: Float = if (scrollState.value > expandedSectionHeight) {
        1f
    } else {
        scrollState.value / expandedSectionHeight
    }

    return alphaValue.coerceIn(0F, 1F)
}

@Composable
fun CollapsingHeaderLayout(
    appBarHeight: Int,
    topAppBar: @Composable (ScrollState, State<Int>) -> Unit,
    header: @Composable () -> Unit,
    body: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val headerHeightState = remember { mutableStateOf(0) }
    val totalHeightState = remember { mutableStateOf(0) }
    val bodyHeightState = remember { mutableStateOf(0) }
    val dummySpace = with (AmbientDensity.current) {
        (totalHeightState.value - bodyHeightState.value - appBarHeight).toDp()
    }
    Column (
        modifier = modifier.fillMaxSize().onSizeChanged { totalHeightState.value = it.height }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            ScrollableColumn(
                scrollState = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier.onSizeChanged { headerHeightState.value = it.height }) {
                    header()
                }
                Column(modifier = Modifier.onSizeChanged { bodyHeightState.value = it.height }) {
                    body()
                }
                if (dummySpace > 0.dp) {
                    Spacer(modifier = Modifier.fillMaxWidth().height(dummySpace))
                }
            }
            topAppBar(scrollState, headerHeightState)
        }
    }
}
