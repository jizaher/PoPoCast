package tw.jizah.popocast.ui.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.model.EpisodeItem
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.widget.EllipsisText

@Composable
fun AllEpisodeSection(
    modifier: Modifier = Modifier,
    useLazyState: Boolean = false,
    lazyListState: LazyListState
) {
    val alpha = if (useLazyState) {
        if (lazyListState.firstVisibleItemIndex >= categoryItemIndex) 1F else 0F
    } else {
        1F
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.alpha(alpha).background(Colors.black)
    )
    {
        EllipsisText(
            text = stringResource(id = R.string.all_episodes),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = Colors.white
        )

        Button(
            onClick = { /* todo: [Amy] click event */ },
            colors = ButtonConstants.defaultButtonColors(
                backgroundColor = Colors.gray800
            ),
            modifier = Modifier.padding(vertical = Dimens.m2)
        ) {
            Text(text = stringResource(id = R.string.sort), color = Colors.gray400)
        }
    }
}

@Composable
fun EpisodeItemView(
    channelId: Long,
    episode: EpisodeItem,
    playerInfo: String,
    isPlaying: Boolean,
    progress: Float,
    modifier: Modifier = Modifier,
    options: (Long, Long) -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.m2),
        backgroundColor = Colors.gray800
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(start = Dimens.m4)
            ) {
                CoilImage(
                    data = episode.imageUrl,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.padding(top = Dimens.m4)
                        .size(Dimens.episodeListCoverSize)
                        .clip(MaterialTheme.shapes.medium)
                )
                Column(modifier = Modifier.weight(1F).padding(start = Dimens.m2, end = Dimens.m2, top = Dimens.m4)) {
                    EllipsisText(
                        text = episode.title,
                        textAlign = TextAlign.Left,
                        maxLines = 2,
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                        color = Colors.white,
                    )
                    EllipsisText(
                        text = episode.subtitle,
                        style = MaterialTheme.typography.subtitle2,
                        color = Colors.gray400,
                        modifier = Modifier.padding(top = Dimens.m2)
                    )
                }

                IconButton(
                    onClick = { options(channelId, episode.id) },
                    modifier = Modifier,
                    content = {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            tint = Colors.gray400
                        )
                    },
                )
            }

            EllipsisText(
                text = episode.description,
                maxLines = 2,
                style = MaterialTheme.typography.subtitle2,
                color = Colors.gray400,
                modifier = Modifier.fillMaxWidth().padding(start = Dimens.m4, end = Dimens.m4,  top = Dimens.m2)
            )

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimens.m1, bottom = Dimens.m4, start = Dimens.m4, end = Dimens.m4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .preferredSize(Dimens.m9)
                        .clip(CircleShape)
                        .background(Colors.white)
                ) {
                    IconButton(
                        onClick = { /* todo: [Amy] click event */ },
                    ) {
                        if (isPlaying) {
                            Icon(Icons.Filled.Pause)
                        } else {
                            Icon(Icons.Filled.PlayArrow)
                        }
                    }
                }

                EllipsisText(
                    text = playerInfo,
                    style = MaterialTheme.typography.overline,
                    color = Colors.gray400,
                    modifier = Modifier.padding(start = Dimens.m2).weight(1F)
                )
            }

            if (progress != 0F) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = progress,
                    color = Colors.yellow
                )
            }
        }
    }
}