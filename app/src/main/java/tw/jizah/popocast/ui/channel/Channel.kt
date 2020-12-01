package tw.jizah.popocast.ui.channel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.model.CategoryItem
import tw.jizah.popocast.model.ChannelItem
import tw.jizah.popocast.model.EpisodeItem
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.widget.CenterRow
import tw.jizah.popocast.widget.CollapsingTopSection
import tw.jizah.popocast.widget.EllipsisText
import tw.jizah.popocast.widget.ExpandableWidget
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
private fun ExpandedTopToolbar(modifier: Modifier, channelItem: ChannelItem) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.fillMaxWidth().height(Dimens.toolBarHeight))
        CoverTitleSection(
            channelItem = channelItem,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.m4)
                .padding(bottom = Dimens.m3)
        )
    }
}

@Composable
private fun CollapsedTopToolbar(
    modifier: Modifier,
    channelTitle: String,
    collapseFactorState: MutableState<Float>
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
            .background(Colors.black.copy(alpha = collapseFactorState.value))
    ) {
        val (iconId, titleId) = createRefs()
        IconButton(onClick = {/* todo: [Amy] click event */ },
            modifier = Modifier.constrainAs(iconId) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }
        ) {
            Icon(
                asset = Icons.Filled.ArrowBack,
                tint = Colors.white
            )
        }

        Text(
            modifier = Modifier.constrainAs(titleId) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(iconId.top)
                bottom.linkTo(iconId.bottom)
                width = Dimension.fillToConstraints
            },
            textAlign = TextAlign.Center,
            text = channelTitle,
            fontWeight = FontWeight.Bold,
            color = Colors.white.copy(alpha = collapseFactorState.value),
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
private fun CoverTitleSection(channelItem: ChannelItem, modifier: Modifier) {
    Row(modifier = modifier.padding(top = Dimens.m3)) {
        CoilImage(
            data = channelItem.imageUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(Dimens.channelCoverSize)
                .clip(MaterialTheme.shapes.medium)
        )
        Column(
            modifier = Modifier.align(Alignment.CenterVertically).padding(start = Dimens.m2)
        ) {
            EllipsisText(
                text = channelItem.title,
                style = MaterialTheme.typography.h4,
                color = Colors.white,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    top = Dimens.m9,
                    end = Dimens.m4,
                    bottom = Dimens.m1
                )
            )
            EllipsisText(
                text = channelItem.subtitle,
                color = Colors.white,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun FollowSection(isFollowed: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = { /* todo: [Amy] click event */ },
            colors = ButtonConstants.defaultButtonColors(backgroundColor = Colors.black),
            border = BorderStroke(Dimens.buttonBorder, Colors.white),
            modifier = Modifier
        ) {

            val textResId = if (isFollowed) {
                R.string.followed
            } else {
                R.string.not_yet_followed
            }
            Text(
                color = Colors.white,
                text = stringResource(id = textResId)
            )
        }

        IconButton(
            onClick = { /* todo: [Amy] click event */ },
            modifier = Modifier
        ) {
            Icon(
                asset = Icons.Default.MoreVert,
                tint = Colors.gray400
            )
        }
    }
}

@Composable
private fun EpisodeListSection(
    channelItem: ChannelItem,
    scrollState: ScrollState,
    topSectionHeightState: MutableState<Dp>
) {
    ScrollableColumn(
        modifier = Modifier.fillMaxSize().background(Colors.black),
        scrollState = scrollState
    ) {
        Spacer(modifier = Modifier.fillMaxWidth().height(topSectionHeightState.value))
        EpisodeItemList(
            channelName = channelItem.title,
            episodeItemList = channelItem.episodeList,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.m4)
                .padding(top = Dimens.m3)
        )
    }
}

@Composable
fun ChannelPage(channelItem: ChannelItem) {
    val scrollState: ScrollState = rememberScrollState()
    val expandedState = remember { mutableStateOf(false) }
    val topSectionMaxHeightState = remember { mutableStateOf(0.dp) }
    val topSectionCollapseFactorState = remember { mutableStateOf(0F) }

    Box(modifier = Modifier.fillMaxSize()) {
        EpisodeListSection(
            channelItem = channelItem,
            scrollState = scrollState,
            topSectionHeightState = topSectionMaxHeightState
        )

        CollapsingTopSection(
            scrollState = scrollState,
            modifier = Modifier.fillMaxWidth(),
            topMaxHeightState = topSectionMaxHeightState,
            topSectionCollapseFactor = topSectionCollapseFactorState,
            topBarSectionSlot = {
                ExpandedTopToolbar(modifier = Modifier.fillMaxWidth(), channelItem = channelItem)
            },
            centerSectionSlot = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    FollowSection(
                        isFollowed = channelItem.isFollowed,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.m4)
                    )
                    ExpandedDescription(
                        expandedState = expandedState,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.m4)
                            .padding(top = Dimens.m3),
                        description = channelItem.description
                    )
                    CategoryList(
                        items = channelItem.categoryList,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.m4)
                    )
                }
            },
            stickySectionSlot = {
                AllEpisodeSection(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = Dimens.m4)
                        .background(Colors.black)
                )
            }
        )

        CollapsedTopToolbar(
            modifier = Modifier.fillMaxWidth().height(Dimens.toolBarHeight),
            channelTitle = channelItem.title,
            collapseFactorState = topSectionCollapseFactorState
        )
    }
}

@Composable
private fun ExpandedDescription(
    expandedState: MutableState<Boolean>,
    modifier: Modifier,
    description: String
) {
    ExpandableWidget(
        modifier = modifier,
        expandedState = expandedState,
        expandedContent = {
            Text(
                text = description,
                color = Colors.gray400
            )
            CenterRow(modifier = Modifier.clickable(onClick = { expandedState.value = false }))  {
                Text(
                    text = stringResource(id = R.string.see_less),
                    color = Colors.white,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { expandedState.value = false }) {
                    Icon(Icons.Default.ExpandLess, tint = Colors.white)
                }
            }
        },
        collapsedContent = {
            EllipsisText(
                text = description,
                maxLines = 2,
                color = Colors.gray400
            )
            CenterRow(modifier = Modifier.clickable(onClick = { expandedState.value = true })) {
                Text(
                    text = stringResource(id = R.string.see_more),
                    color = Colors.white,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { expandedState.value = true }) {
                    Icon(Icons.Default.ExpandMore, tint = Colors.white)
                }
            }
        }
    )
}

@Composable
private fun CategoryList(modifier: Modifier, items: List<CategoryItem>) {
    LazyRowFor(items = items, modifier = modifier.fillMaxWidth()) { item ->
        OutlinedButton(
            shape = RoundedCornerShape(50F),
            onClick = { /* todo: [Amy] click event */ },
            colors = ButtonConstants.defaultButtonColors(backgroundColor = Colors.black),
            border = BorderStroke(Dimens.buttonBorder, Colors.white),
        ) {
            Text(
                color = Colors.white,
                text = item.name,
            )
        }

        Spacer(modifier = Modifier.preferredWidth(Dimens.m2))
    }
}

@Preview
@Composable
fun ExpandingTextPreview() {
    ExpandableWidget(
        expandedContent = {
            Text(text = "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction")
        },
        collapsedContent = {
            Text(text = "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction")
        }
    )
}

@Preview
@Composable
fun ChannelPagePreview() {
    ChannelPage(
        ChannelItem(
            imageUrl = "https://picsum.photos/300/300",
            title = "Channel title",
            subtitle = "Author",
            description = "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction",
            isFollowed = true,
            isExpanded = true,
            categoryList = listOf(CategoryItem("Comedy", ""), CategoryItem("Knowledge", "")),
            episodeList = (0..10).map { index ->
                EpisodeItem(
                    imageUrl = "https://picsum.photos/300/300",
                    itemName = "This is item title $index",
                    itemInfo = "This is item info",
                    releaseTime = Calendar.getInstance().timeInMillis - TimeUnit.MILLISECONDS.convert(
                        (index + 1).toLong(),
                        TimeUnit.DAYS
                    ),
                    duration = 0L,
                    description = ""
                )
            }
        )
    )
}