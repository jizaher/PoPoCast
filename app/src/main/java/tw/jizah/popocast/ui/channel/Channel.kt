package tw.jizah.popocast.ui.channel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.model.CategoryItem
import tw.jizah.popocast.model.ChannelItem
import tw.jizah.popocast.ui.episode.formatDuration
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.utils.DateTimeUtils
import tw.jizah.popocast.widget.*

private val coverSectionHeight = Dimens.channelCoverSize + Dimens.m3 * 2
private val followSectionHeight = 56.dp
private val categorySectionHeight = Dimens.toolBarHeight
private val allEpisodeTitleSectionHeight = 56.dp
const val categoryItemIndex = 3

@Composable
fun ChannelPage(
    channelId: Long,
    options: (Long, Long) -> Unit,
    back: () -> Unit
) {
    val viewModel = viewModel(modelClass = ChannelViewModel::class.java)
    val channelItem = viewModel.getChannel(channelId = channelId)
    val expandedState = remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        EpisodeListSection(
            channelItem = channelItem,
            expandedState = expandedState,
            lazyListState = lazyListState,
            options = options
        )

        AllEpisodeSection(
            lazyListState = lazyListState,
            useLazyState = true,
            modifier = Modifier.fillMaxWidth().padding(top = Dimens.toolBarHeight)
                .height(allEpisodeTitleSectionHeight)
                .padding(horizontal = Dimens.m4)
        )

        CollapsedTopToolbar(
            modifier = Modifier.fillMaxWidth().height(Dimens.toolBarHeight),
            channelTitle = channelItem.title,
            lazyListState = lazyListState,
            back = back
        )
    }
}

@Composable
private fun CollapsedTopToolbar(
    modifier: Modifier,
    channelTitle: String,
    lazyListState: LazyListState,
    back: () -> Unit
) {
    val alpha = if (lazyListState.firstVisibleItemIndex == 0) {
        val offsetDp =
            with(AmbientDensity.current) { lazyListState.firstVisibleItemScrollOffset.toDp() }
        (offsetDp / coverSectionHeight).coerceIn(0F, 1F)
    } else {
        1F
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.alpha(alpha).background(Colors.black)
    ) {
        Text(
            textAlign = TextAlign.Center,
            text = channelTitle,
            fontWeight = FontWeight.Bold,
            color = Colors.white,
            style = MaterialTheme.typography.h6
        )
    }

    IconButton(
        onClick = { back() },
        modifier = Modifier.height(Dimens.toolBarHeight)
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            tint = Colors.white
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
        modifier = modifier.fillMaxWidth().height(followSectionHeight),
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
                imageVector = Icons.Default.MoreVert,
                tint = Colors.gray400
            )
        }
    }
}

@Composable
private fun EpisodeListSection(
    channelItem: ChannelItem,
    lazyListState: LazyListState,
    expandedState: MutableState<Boolean>,
    options: (Long, Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Colors.black).padding(horizontal = Dimens.m4),
        state = lazyListState
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.fillMaxWidth().height(Dimens.toolBarHeight))
                CoverTitleSection(
                    channelItem = channelItem,
                    modifier = Modifier.fillMaxWidth().padding(vertical = Dimens.m3)
                )
            }
        }
        item {
            FollowSection(
                isFollowed = channelItem.isFollowed,
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            ExpandableText(
                text = channelItem.description, maxLines = 2, expandedState = expandedState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.m1)
            )
        }

        item {
            CategoryList(
                items = channelItem.categoryList,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            AllEpisodeSection(
                lazyListState = lazyListState,
                modifier = Modifier.fillMaxWidth().height(allEpisodeTitleSectionHeight)
                    .background(Colors.black)
            )
        }

        items(channelItem.episodeList) { item ->
            val dateStr = DateTimeUtils.getDateString(AmbientContext.current, item.releaseTime)
            val durationStr = formatDuration(item.duration)
            Column {
                EpisodeItemView(
                    channelId = channelItem.id,
                    modifier = Modifier.fillMaxWidth(),
                    episode = item,
                    playerInfo = "$dateStr．$durationStr",
                    isPlaying = false,
                    progress = 0.2F,
                    options = options
                )
                Spacer(modifier = Modifier.fillMaxWidth().preferredHeight(Dimens.m3))
            }
        }
    }
}

@Composable
private fun CategoryList(modifier: Modifier, items: List<CategoryItem>) {
    LazyRowFor(
        items = items,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(categorySectionHeight)
    ) { item ->
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