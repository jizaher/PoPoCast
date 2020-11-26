package tw.jizah.popocast.ui.episode

import android.content.Context
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
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
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds

const val MINUTES_PER_HOUR = 60

@Composable
fun EpisodeDetail(
        channel: ChannelItem,
        episode: EpisodeItem,
) {
    Surface(Modifier.fillMaxSize()) {
        ScrollableColumn(modifier = Modifier.fillMaxSize()) {
            EpisodeAppBar(Modifier.fillMaxWidth())
            EpisodeHeader(channel = channel, episode = episode, modifier = Modifier.fillMaxWidth())
            EpisodeButtonBar(modifier = Modifier.fillMaxWidth())
            EpisodeDescription(episode = episode, modifier = Modifier.fillMaxWidth())
            SeeAllEpisodes(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun EpisodeAppBar(
        modifier: Modifier = Modifier
) {
    val iconTint = Color.White
    ConstraintLayout(modifier = modifier.padding(Dimens.m3)) {
        val (backButton, moreButton) = createRefs()
        Icon(
                asset = Icons.Filled.ArrowBack,
                tint = iconTint,
                modifier = Modifier.constrainAs(backButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        )
        Icon(
                asset = Icons.Filled.MoreVert,
                tint = iconTint,
                modifier = Modifier.constrainAs(moreButton) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
        )
    }
}

@Composable
private fun EpisodeHeader(
        channel: ChannelItem,
        episode: EpisodeItem,
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
        PlayState(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun PlayState(
        modifier: Modifier
) {
    // TODO: [Zoey] play status
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
        isPlaying: Boolean,
        onClick: () -> Unit = {},
        modifier: Modifier = Modifier
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
private fun EpisodeDescription(
        episode: EpisodeItem,
        modifier: Modifier = Modifier
) {
    val date = formatDate(episode.releaseTime)
    val duration = formatDuration(ContextAmbient.current, episode.duration)
    val timeText = "$date•$duration"
    Column(modifier = modifier.padding(Dimens.m3)) {
        Text (text = episode.description, style = MaterialTheme.typography.body2, color = Colors.gray600)
        Text(text = timeText, style = MaterialTheme.typography.body2, color = Colors.gray600, modifier = Modifier.padding(vertical = Dimens.m4))
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
        Icon(asset = Icons.Filled.NavigateNext, modifier = Modifier.constrainAs(naviIcon) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        })
    }
}

@OptIn(ExperimentalTime::class)
fun formatDuration(context: Context, durationInMillis: Long): String {
    val period = durationInMillis.milliseconds.toDateTimePeriod()
    var minutes = if (period.seconds > 0) {
        period.minutes + 1
    } else {
        period.minutes
    }

    val hours = if (minutes >= MINUTES_PER_HOUR) {
        minutes -= MINUTES_PER_HOUR
        period.hours + 1
    } else {
        period.hours
    }

    // TODO: [Zoey] duration string quantity
    val durationBuilder = StringBuilder()
    if (hours > 0) {
        durationBuilder.append("$hours ${context.getString(R.string.time_unit_hour)} ")
    }

    if (minutes > 0) {
        durationBuilder.append("$minutes ${context.getString(R.string.time_unit_minute)}")
    }

    return durationBuilder.toString()
}

@Composable
private fun formatDate(dateInMillis: Long): String {
    // TODO: [Zoey] format date
    val tz = TimeZone.currentSystemDefault()
    val localDateTime = Instant.fromEpochMilliseconds(dateInMillis).toLocalDateTime(tz)
    return localDateTime.date.toString()
}