package tw.jizah.popocast.ui.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.model.EpisodeItem
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.widget.EllipsisText
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun AllEpisodeSection(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier) {
        val (headerId, sortBtnId) = createRefs()

        EllipsisText(text = stringResource(id = R.string.all_episodes),
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            color = Colors.white,
            modifier = Modifier
                .constrainAs(headerId) {
                    start.linkTo(parent.start)
                    end.linkTo(sortBtnId.start)
                    top.linkTo(parent.top, Dimens.m2)
                    width = Dimension.fillToConstraints
                })

        Button(onClick = { /* todo: [Amy] click event */ },
            colors = ButtonConstants.defaultButtonColors(
                backgroundColor = Colors.gray800
            ),
            modifier = Modifier.padding(vertical = Dimens.m2)
                .constrainAs(sortBtnId) {
                end.linkTo(parent.end)
                top.linkTo(headerId.top)
                bottom.linkTo(headerId.bottom)
            }
        ) {
            Text(text = stringResource(id = R.string.sort), color = Colors.gray400)
        }
    }
}

@Composable
fun EpisodeItemList(
    channelName: String,
    episodeItemList: List<EpisodeItem>,
    modifier: Modifier = Modifier,
) {
    // todo: [Amy] logic for player info
    val playerInfo = "昨天"

    Column(modifier.fillMaxWidth()) {
        episodeItemList.forEach {
                item ->
            EpisodeItemView(
                modifier = Modifier.fillMaxWidth(),
                imageUrl = item.imageUrl,
                title = item.itemName,
                subTitle = channelName,
                itemInfo = item.itemInfo,
                playerInfo = playerInfo,
                isPlaying = false,
                progress = 0.2F
            )
            Spacer(modifier = Modifier.preferredHeight(Dimens.m2))
        }
    }
}

@Composable
fun EpisodeItemView(
    imageUrl: String,
    title: String,
    subTitle: String,
    itemInfo: String,
    playerInfo: String,
    isPlaying: Boolean,
    progress: Float,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.m2),
        backgroundColor = Colors.gray800
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (coverId, titleId, subtitleId, moreIconId,
                channelInfoId, playerIconId, itemInfoId, progressBarId) = createRefs()

            CoilImage(
                data = imageUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(Dimens.episodeListCoverSize)
                    .clip(MaterialTheme.shapes.medium)
                    .constrainAs(coverId) {
                        start.linkTo(parent.start, Dimens.m4)
                        top.linkTo(parent.top, Dimens.m4)
                    }
            )

            EllipsisText(text = title,
                textAlign = TextAlign.Left,
                maxLines = 2,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = Colors.white,
                modifier = Modifier.constrainAs(titleId) {
                    start.linkTo(coverId.end, Dimens.m4)
                    end.linkTo(moreIconId.start, Dimens.m1)
                    top.linkTo(coverId.top)
                    width = Dimension.fillToConstraints
                }
            )

            EllipsisText(text = subTitle,
                style = MaterialTheme.typography.subtitle2,
                color = Colors.gray400,
                modifier = Modifier.constrainAs(subtitleId) {
                    start.linkTo(titleId.start)
                    end.linkTo(titleId.end)
                    top.linkTo(titleId.bottom, Dimens.m1)
                    width = Dimension.fillToConstraints
                }
            )

            IconButton(
                onClick = { /* todo: [Amy] click event */ },
                modifier = Modifier.constrainAs(moreIconId) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
                icon = {
                    Icon(
                        asset = Icons.Default.MoreVert,
                        tint = Colors.gray400
                    )
                },
            )

            EllipsisText(text = itemInfo,
                maxLines = 2,
                style = MaterialTheme.typography.subtitle2,
                color = Colors.gray400,
                modifier = Modifier.constrainAs(channelInfoId) {
                    start.linkTo(coverId.start)
                    top.linkTo(coverId.bottom, Dimens.m2)
                    end.linkTo(moreIconId.end)
                    width = Dimension.fillToConstraints
                }
            )

            Box(
                alignment = Alignment.Center,
                modifier = Modifier
                    .preferredSize(Dimens.m9)
                    .clip(CircleShape)
                    .background(Colors.white)
                    .constrainAs(playerIconId) {
                        start.linkTo(coverId.start)
                        top.linkTo(channelInfoId.bottom, Dimens.m1)
                        bottom.linkTo(parent.bottom, Dimens.m4)
                    }
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

            EllipsisText(text = playerInfo,
                style = MaterialTheme.typography.overline,
                color = Colors.gray400,
                modifier = Modifier.constrainAs(itemInfoId) {
                    start.linkTo(playerIconId.end, Dimens.m2)
                    end.linkTo(moreIconId.end, Dimens.m1)
                    top.linkTo(playerIconId.top)
                    bottom.linkTo(playerIconId.bottom)
                    width = Dimension.fillToConstraints
                }
            )

            if (progress != 0F) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().constrainAs(progressBarId) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                    progress = progress,
                    color = Colors.yellow
                )
            }

        }
    }
}

@Preview
@Composable
fun previewItemList() {
    EpisodeItemList(channelName = "channel title",
        episodeItemList = (0..10).map { index ->
            EpisodeItem(
                imageUrl = "https://picsum.photos/300/300",
                itemName = "This is item title $index This is item title $index This is item title $index This is item title $index This is item title $index This is item title $index This is item title $index This is item title $index",
                itemInfo = "This is item info This is item info This is item info This is item info This is item info This is item info",
                releaseTime = Calendar.getInstance().timeInMillis - TimeUnit.MILLISECONDS.convert(
                    (index + 1).toLong(),
                    TimeUnit.DAYS
                ),
                duration = 0L,
                description = ""
            )
        }
    )
}

@Preview
@Composable
fun previewItem() {
    EpisodeItemView(
        imageUrl = "https://picsum.photos/300/300",
        title = "This is channel Title2 This is channel Title2 This is channel Title2 This is channel Title2",
        subTitle = "This is channel subTitle",
        itemInfo = "This is channel info",
        playerInfo = "Item info",
        isPlaying = false,
        progress = 0.5F
    )
}