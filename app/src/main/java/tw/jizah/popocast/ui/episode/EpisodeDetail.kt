package tw.jizah.popocast.ui.episode

import androidx.compose.animation.animate
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowCircleDown
import androidx.compose.runtime.*
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toDateTimePeriod
import kotlinx.datetime.toLocalDateTime
import tw.jizah.popocast.R
import tw.jizah.popocast.model.ChannelItem
import tw.jizah.popocast.model.EpisodeItem
import tw.jizah.popocast.model.PlayState
import tw.jizah.popocast.ui.player.PlayerViewModel
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.utils.quantityStringResource
import tw.jizah.popocast.utils.theme.DynamicThemePrimaryColorsFromImage
import tw.jizah.popocast.utils.theme.rememberDominantColorState
import tw.jizah.popocast.utils.verticalGradientScrim
import tw.jizah.popocast.widget.CollapsingHeaderLayout
import tw.jizah.popocast.widget.EllipsisText
import tw.jizah.popocast.widget.ExpandableText
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

private const val MILLISECONDS_PER_MINUTES = 60000
private val iconTint = Colors.white
private val iconHighlightTint = Colors.greenA700

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun EpisodeDetail(
    channel: ChannelItem,
    episode: EpisodeItem,
    playState: PlayState
) {
    val viewModel: PlayerViewModel = viewModel(modelClass = PlayerViewModel::class.java)
    val isPlayingState by viewModel.isPlaying.collectAsState()
    val expandedState: MutableState<Boolean> = remember { mutableStateOf(false) }
    val isItemAddedState: MutableState<Boolean> = remember { mutableStateOf(false) }
    val downloadState: MutableState<Int> = remember { mutableStateOf(0) }
    val appBarHeight = with (AmbientDensity.current) {
        Dimens.toolBarHeight.toIntPx()
    }
    val dominantColorState = rememberDominantColorState()
    val channelImageUrl = channel.imageUrl

    DynamicThemePrimaryColorsFromImage(
        dominantColorState = dominantColorState
    ) {
        if (channelImageUrl.isNotBlank()) {
            LaunchedEffect(channelImageUrl) {
                dominantColorState.updateColorsFromImageUrl(channelImageUrl)
            }
        }

        Surface(Modifier.fillMaxSize()) {
            CollapsingHeaderLayout(
                appBarHeight = appBarHeight,
                topAppBar = { scrollState, headerHeightState ->
                    EpisodeAppBar(
                        title = episode.title,
                        appBarHeight = appBarHeight,
                        scrollState = scrollState,
                        headerHeightState = headerHeightState,
                        modifier = Modifier.fillMaxWidth().height(Dimens.toolBarHeight)
                    )
                }, header = {
                    EpisodeHeader(
                        channel = channel,
                        episode = episode,
                        playState = playState,
                        modifier = Modifier.fillMaxWidth()
                            .verticalGradientScrim(
                                color = MaterialTheme.colors.primary.copy(alpha = 0.8f)
                            )
                            .padding(top = Dimens.toolBarHeight)
                    )
                }, body = {
                    EpisodeButtonBar(
                        isPlaying = isPlayingState,
                        onClickPlay = viewModel::togglePlayOrPause,
                        isItemAdded = isItemAddedState.value,
                        downloadState = downloadState.value,
                        modifier = Modifier.fillMaxWidth()
                    )
                    EpisodeBody(
                        episode = episode,
                        expandedState = expandedState,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SeeAllEpisodes(modifier = Modifier.fillMaxWidth())
                })
        }
    }
}

@Composable
private fun EpisodeAppBar(
    title: String,
    appBarHeight: Int,
    scrollState: ScrollState,
    headerHeightState: State<Int>,
    modifier: Modifier = Modifier
) {
    val collapseFraction = (scrollState.value / (headerHeightState.value - appBarHeight)).coerceIn(0F, 1F)
    TopAppBar(
        title = {
            EllipsisText(
                text = title,
                color = Colors.white.copy(alpha = collapseFraction),
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                tint = iconTint,
                modifier = Modifier.padding(start = Dimens.m1)
            )
        },
        actions = {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                tint = iconTint,
                modifier = Modifier.padding(end = Dimens.m1)
            )
        },
        backgroundColor = Colors.black.copy(alpha = collapseFraction),
        elevation = 0.dp,
        modifier = modifier
    )
}

@Composable
private fun EpisodeHeader(
    channel: ChannelItem,
    episode: EpisodeItem,
    playState: PlayState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(Dimens.m3)) {
        CoilImage(
            data = channel.imageUrl,
            fadeIn = true,
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(top = Dimens.m3, bottom = Dimens.m4)
                .preferredSize(Dimens.episodeCoverSize)
                .clip(RoundedCornerShape(size = Dimens.m1))
        )
        Text(
            text = episode.title,
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = Dimens.m6)
        )
        Text(
            text = channel.title,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = Dimens.m6)
        )
        // TODO: [Zoey] sync player progress
        PlayStateInfo(
            releaseTime = episode.releaseTime,
            playState = playState,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PlayStateInfo(
    releaseTime: Long,
    playState: PlayState,
    modifier: Modifier = Modifier
) {
    val formattedDate = formatDate(releaseTime)
    val timeRemaining = playState.duration - playState.elapsedTime
    val isNotStarted = (playState.elapsedTime == 0L)
    val formattedTime = when {
        isNotStarted -> {
            formatDuration(timeRemaining)
        }
        playState.duration == playState.elapsedTime -> {
            stringResource(id = R.string.podcast_played)
        }
        else -> {
            val formattedTimeRemaining = formatDuration(timeRemaining)
            stringResource(id = R.string.time_remaining, formattedTimeRemaining)
        }
    }

    val progress = (playState.elapsedTime.toFloat()).div(playState.duration.toFloat())
    Row(modifier = modifier.padding(vertical = Dimens.m1)) {
        Text(text = "$formattedDate • $formattedTime", style = MaterialTheme.typography.overline, color = Colors.gray400)
        if (!isNotStarted) {
            EpisodeProgressBar(
                progress = progress,
                modifier = Modifier.align(Alignment.CenterVertically)
                    .padding(horizontal = Dimens.m3)
                    .preferredWidth(Dimens.episodeProgressBarWidth)
            )
        }
    }
}

@Composable
private fun EpisodeProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val animatedProgress = animate(
        target = progress,
        animSpec = ProgressIndicatorConstants.DefaultProgressAnimationSpec
    )

    Row(modifier = modifier.clip(RoundedCornerShape(percent = 50))) {
        LinearProgressIndicator(
            progress = animatedProgress,
            color = iconHighlightTint,
            backgroundColor = Colors.gray100.copy(alpha = 0.1F)
        )
    }
}

@Composable
private fun EpisodeButtonBar(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    onClickPlay: () -> Unit = {},
    onClickShare: () -> Unit = {},
    isItemAdded: Boolean,
    onClickAdd: () -> Unit = {},
    downloadState: Int,
    onClickDownload: () -> Unit = {},
) {
    ConstraintLayout(modifier = modifier.padding(Dimens.m3)) {
        val (playBtn, shareBtn, addBtn, downloadBtn) = createRefs()
        PlayButton(
            isPlaying = isPlaying,
            onClick = onClickPlay,
            modifier = Modifier.constrainAs(playBtn) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
        IconButton(
            onClick = onClickShare,
            modifier = Modifier.constrainAs(shareBtn) {
                end.linkTo(addBtn.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        ) {
            Icon(imageVector = Icons.Filled.Share, tint = iconTint)
        }
        AddToPlaylistButton(
            isAdded = isItemAdded,
            onClick = onClickAdd,
            modifier = Modifier.constrainAs(addBtn) {
                end.linkTo(downloadBtn.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
        DownloadButton(
            downloadState = downloadState,
            onClick = onClickDownload,
            modifier = Modifier.constrainAs(downloadBtn) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
private fun AddToPlaylistButton(
    modifier: Modifier = Modifier,
    isAdded: Boolean,
    onClick: () -> Unit = {}
) {
    val (asset, tint) = if (isAdded) {
        Icons.Filled.CheckCircle to iconHighlightTint
    } else {
        Icons.Filled.AddCircleOutline to iconTint
    }
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(imageVector = asset, tint = tint)
    }
}

@Composable
private fun DownloadButton(
    downloadState: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // TODO: [Zoey] display download progress icon
    val (asset, tint) = when (downloadState) {
        0 -> Icons.Outlined.ArrowCircleDown to iconTint
        else -> Icons.Filled.ArrowCircleDown to iconHighlightTint
    }
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = asset, tint = tint)
    }
}

@Composable
private fun PlayButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        colors = ButtonConstants.defaultButtonColors(backgroundColor = iconHighlightTint),
        modifier = modifier.defaultMinSizeConstraints(minWidth = Dimens.episodePlayButtonMinWidth, minHeight = Dimens.episodePlayButtonMinHeight)
    ) {
        val text = if (isPlaying) {
            stringResource(id = R.string.podcast_pause)
        } else {
            stringResource(id = R.string.podcast_play)
        }
        Text(text = text, style = MaterialTheme.typography.button)
    }
}

@Composable
private fun EpisodeBody(
    episode: EpisodeItem,
    expandedState: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val date = formatDate(episode.releaseTime)
    val duration = formatDuration(episode.duration)
    val timeText = "$date • $duration"
    Column(modifier = modifier.padding(Dimens.m3)) {
        ExpandableText(text = episode.description, maxLines = 5, expandedState = expandedState)
        Text(text = timeText, style = MaterialTheme.typography.overline, color = Colors.gray400, modifier = Modifier.padding(vertical = Dimens.m4))
        // TODO: [Zoey] show episode image
    }
}

@Composable
private fun SeeAllEpisodes(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.padding(horizontal = Dimens.m3, vertical = Dimens.m4)) {
        val (titleText, naviIcon) = createRefs()
        Text(
            text = stringResource(id = R.string.see_all_episodes),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(titleText) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            }
        )
        Icon(imageVector = Icons.Filled.NavigateNext, tint = iconTint, modifier = Modifier.constrainAs(naviIcon) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        })
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun formatDuration(durationInMillis: Long): String {
    val carry1MinDuration = if (durationInMillis % MILLISECONDS_PER_MINUTES > 0) {
        // carry one minute
        durationInMillis + MILLISECONDS_PER_MINUTES
    } else {
        durationInMillis
    }
    val period = carry1MinDuration.milliseconds.toDateTimePeriod()

    val durationBuilder = StringBuilder()
    if (period.hours > 0) {
        durationBuilder.append(quantityStringResource(R.plurals.time_unit_hour, period.hours, period.hours))
        durationBuilder.append(" ")
    }

    if (period.minutes > 0) {
        durationBuilder.append(quantityStringResource(R.plurals.time_unit_minute, period.minutes, period.minutes))
    }

    return durationBuilder.toString()
}

private fun formatDate(dateInMillis: Long): String {
    // TODO: [Zoey] format date
    val tz = TimeZone.currentSystemDefault()
    val localDateTime = Instant.fromEpochMilliseconds(dateInMillis).toLocalDateTime(tz)
    return localDateTime.date.toString()
}