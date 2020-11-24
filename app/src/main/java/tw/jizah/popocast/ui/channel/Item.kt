package tw.jizah.popocast.ui.channel

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens.episodeListCoverSize
import tw.jizah.popocast.ui.theme.Dimens.m1
import tw.jizah.popocast.ui.theme.Dimens.m2
import tw.jizah.popocast.ui.theme.Dimens.m4
import tw.jizah.popocast.ui.theme.Dimens.m9
import tw.jizah.popocast.widget.EllipsisText
import java.util.*
import java.util.concurrent.TimeUnit

data class ItemInfo(val channelName: String, val itemList: List<Item>) {
    data class Item(
        val imageUrl: String,
        val itemName: String,
        val itemInfo: String,
        val releaseTime: Long
    )
}

@Composable
fun EpisodeItemList(itemInfo: ItemInfo, modifier: Modifier = Modifier.fillMaxWidth()) {
    val channelName = itemInfo.channelName
    // TODO DEBUG [Amy] logic for player info
    val playerInfo = "昨天"

    Column(modifier = modifier) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (headerId, sortBtnId) = createRefs()

            EllipsisText(text = stringResource(id = R.string.all_chapter),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = m2, bottom = m2).constrainAs(headerId) {
                    start.linkTo(parent.start)
                    end.linkTo(sortBtnId.start)
                    top.linkTo(parent.top, m2)
                    width = Dimension.fillToConstraints
                })

            Button(onClick = { /* todo */ },
                colors = ButtonConstants.defaultButtonColors(
                    backgroundColor = Color.DarkGray
                ),
                modifier = Modifier.constrainAs(sortBtnId) {
                    end.linkTo(parent.end)
                    top.linkTo(headerId.top)
                    bottom.linkTo(headerId.bottom)
                }
            ) {
                Text(text = stringResource(id = R.string.sort), color = Color.LightGray)
            }
        }

        Spacer(modifier = Modifier.preferredHeight(m2))

        LazyColumnFor(
            modifier = Modifier.fillMaxWidth(),
            items = itemInfo.itemList,
        ) { item ->
            /* todo : logic for progress and play state */
            Column {
                EpisodeItem(
                    imageUrl = item.imageUrl,
                    title = item.itemName,
                    subTitle = channelName,
                    itemInfo = item.itemInfo,
                    playerInfo = playerInfo,
                    isPlaying = false,
                    progress = 0.2F
                )
                Spacer(modifier = Modifier.preferredSize(m2))
            }
        }
    }
}

@Composable
fun EpisodeItem(
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
        shape = RoundedCornerShape(m2),
        backgroundColor = Color.DarkGray
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (coverId, titleId, subtitleId, moreIconId,
                channelInfoId, playerIconId, itemInfoId, progressBarId) = createRefs()

            CoilImage(
                data = imageUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier.preferredSize(episodeListCoverSize)
                    .clip(MaterialTheme.shapes.medium)
                    .constrainAs(coverId) {
                        start.linkTo(parent.start, m4)
                        top.linkTo(parent.top, m4)
                    }
            )

            EllipsisText(text = title,
                textAlign = TextAlign.Left,
                maxLines = 2,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.constrainAs(titleId) {
                    start.linkTo(coverId.end, m4)
                    end.linkTo(moreIconId.start, m1)
                    top.linkTo(coverId.top)
                    width = Dimension.fillToConstraints
                }
            )

            EllipsisText(text = subTitle,
                style = MaterialTheme.typography.subtitle2,
                color = Color.LightGray,
                modifier = Modifier.constrainAs(subtitleId) {
                    start.linkTo(titleId.start)
                    end.linkTo(titleId.end)
                    top.linkTo(titleId.bottom, m1)
                    width = Dimension.fillToConstraints
                }
            )

            IconButton(
                onClick = { /* todo */ },
                modifier = Modifier.constrainAs(moreIconId) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                },
                icon = {
                    Icon(
                        asset = Icons.Default.MoreVert,
                        tint = Color.LightGray
                    )
                },
            )

            EllipsisText(text = itemInfo,
                maxLines = 2,
                style = MaterialTheme.typography.subtitle2,
                color = Color.LightGray,
                modifier = Modifier.constrainAs(channelInfoId) {
                    start.linkTo(coverId.start)
                    top.linkTo(coverId.bottom, m2)
                    end.linkTo(moreIconId.end)
                    width = Dimension.fillToConstraints
                }
            )

            Box(
                alignment = Alignment.Center,
                modifier = Modifier
                    .preferredSize(m9)
                    .clip(CircleShape)
                    .background(Color.White)
                    .constrainAs(playerIconId) {
                        start.linkTo(coverId.start)
                        top.linkTo(channelInfoId.bottom, m1)
                        bottom.linkTo(parent.bottom, m4)
                    }
            ) {
                IconButton(
                    onClick = { /* todo */ },
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
                color = Color.LightGray,
                modifier = Modifier.constrainAs(itemInfoId) {
                    start.linkTo(playerIconId.end, m2)
                    end.linkTo(moreIconId.end, m1)
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
                    color = Colors.playerControllerProgress
                )
            }

        }
    }
}

@Preview
@Composable
fun previewItemList() {
    val itemInfo = ItemInfo(
        channelName = "百靈果NEWS",
        itemList = (0..10).map { index ->
            ItemInfo.Item(
                imageUrl = "https://picsum.photos/300/300",
                itemName = "This is item title $index This is item title $index This is item title $index This is item title $index This is item title $index This is item title $index This is item title $index This is item title $index",
                itemInfo = "This is item info This is item info This is item info This is item info This is item info This is item info",
                releaseTime = Calendar.getInstance().timeInMillis - TimeUnit.MILLISECONDS.convert(
                    (index + 1).toLong(),
                    TimeUnit.DAYS
                )
            )
        }
    )

    EpisodeItemList(itemInfo)
}

@Preview
@Composable
fun previewItem() {
    EpisodeItem(
        imageUrl = "https://picsum.photos/300/300",
        title = "This is channel Title2 This is channel Title2 This is channel Title2 This is channel Title2",
        subTitle = "This is channel subTitle",
        itemInfo = "This is channel info",
        playerInfo = "Item info",
        isPlaying = false,
        progress = 0.5F
    )
}