package tw.jizah.popocast.ui.episode

import androidx.compose.animation.animate
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toDateTimePeriod
import kotlinx.datetime.toLocalDateTime
import tw.jizah.popocast.R
import tw.jizah.popocast.model.ChannelItem
import tw.jizah.popocast.model.EpisodeItem
import tw.jizah.popocast.model.PlayState
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.utils.quantityStringResource
import tw.jizah.popocast.widget.ExpandableText
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

private const val MILLISECONDS_PER_MINUTES = 60000
private val iconTint = Colors.white

@Composable
fun EpisodeDetail(
    channel: ChannelItem,
    episode: EpisodeItem,
    playState: PlayState
) {
    val expandedState: MutableState<Boolean> = remember { mutableStateOf(false) }

    Surface(Modifier.fillMaxSize()) {
        ScrollableColumn(modifier = Modifier.fillMaxSize()) {
            EpisodeAppBar(Modifier.fillMaxWidth())
            EpisodeHeader(channel = channel, episode = episode, playState = playState, modifier = Modifier.fillMaxWidth())
            EpisodeButtonBar(modifier = Modifier.fillMaxWidth())
            EpisodeBody(episode = episode, expandedState = expandedState, modifier = Modifier.fillMaxWidth())
            SeeAllEpisodes(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun EpisodeAppBar(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.padding(Dimens.m3)) {
        val (backButton, moreButton) = createRefs()
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            tint = iconTint,
            modifier = Modifier.constrainAs(backButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            },
        )
        Icon(
            imageVector = Icons.Filled.MoreVert,
            tint = iconTint,
            modifier = Modifier.constrainAs(moreButton) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
        )
    }
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
            text = episode.itemName,
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
            color = Colors.greenA700,
            backgroundColor = Colors.gray100.copy(alpha = 0.1F)
        )
    }
}

@Composable
private fun EpisodeButtonBar(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(Dimens.m3)) {
        PlayButton(isPlaying = false, modifier = Modifier.wrapContentSize())
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
        colors = ButtonConstants.defaultButtonColors(backgroundColor = Colors.greenA700),
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