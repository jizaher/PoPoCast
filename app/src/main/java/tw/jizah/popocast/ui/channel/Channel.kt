package tw.jizah.popocast.ui.channel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.model.CategoryItem
import tw.jizah.popocast.model.ChannelItem
import tw.jizah.popocast.model.EpisodeItem
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.widget.CenterRow
import tw.jizah.popocast.widget.EllipsisText
import tw.jizah.popocast.widget.ExpandableWidget
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun ChannelPage(channelItem: ChannelItem) {
    Column(modifier = Modifier.fillMaxSize().background(Colors.black)) {
        ConstraintLayout(modifier = Modifier.padding(horizontal = Dimens.m4).fillMaxWidth()) {
            val (actionBack, coverId, titleId, subtitleId,
                followBtnId, moreBtnId, introductionId,
                categoryId) = createRefs()

            IconButton(onClick = {},
                modifier = Modifier.constrainAs(actionBack) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }) {
                Icon(
                    asset = Icons.Filled.ArrowBack,
                    tint = Colors.white
                )
            }

            CoilImage(data = channelItem.imageUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier.preferredSize(Dimens.channelCoverSize)
                    .clip(MaterialTheme.shapes.medium)
                    .constrainAs(coverId) {
                        start.linkTo(parent.start)
                        top.linkTo(actionBack.bottom, Dimens.m3)
                    })

            EllipsisText(text = channelItem.title,
                style = MaterialTheme.typography.h4,
                color = Colors.white,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(titleId) {
                    start.linkTo(coverId.end, Dimens.m4)
                    end.linkTo(parent.end)
                    top.linkTo(coverId.top)
                    bottom.linkTo(coverId.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
            )
            EllipsisText(text = channelItem.subtitle,
                color = Colors.white,
                modifier = Modifier.constrainAs(subtitleId) {
                    start.linkTo(titleId.start)
                    end.linkTo(titleId.end)
                    top.linkTo(titleId.bottom)
                    width = Dimension.fillToConstraints
                }
            )

            OutlinedButton(onClick = { /* todo: [Amy] click event */ },
                colors = ButtonConstants.defaultButtonColors(
                    backgroundColor = Colors.black
                ),
                border = BorderStroke(Dimens.buttonBorder, Colors.white),
                modifier = Modifier.constrainAs(followBtnId) {
                    top.linkTo(coverId.bottom, Dimens.m3)
                    start.linkTo(coverId.start)
                }) {

                val textResId = if (channelItem.isFollowed) {
                    R.string.followed
                } else {
                    R.string.not_yet_followed
                }
                Text(
                    color = Colors.white,
                    text = stringResource(id = textResId)
                )
            }

            IconButton(onClick = { /* todo: [Amy] click event */ },
                modifier = Modifier.constrainAs(moreBtnId) {
                    top.linkTo(followBtnId.top)
                    bottom.linkTo(followBtnId.bottom)
                    end.linkTo(parent.end)
                }) {
                Icon(
                    asset = Icons.Default.MoreVert,
                    tint = Colors.gray400
                )
            }

            val expandedState = remember { mutableStateOf(false) }
            ExpandableWidget(
                expandedState = expandedState,
                modifier = Modifier.constrainAs(introductionId) {
                    start.linkTo(coverId.start)
                    end.linkTo(titleId.end)
                    top.linkTo(followBtnId.bottom, Dimens.m3)
                    width = Dimension.fillToConstraints
                },
                expandedContent = {
                    Text(
                        text = channelItem.introduction,
                        color = Colors.gray400
                    )
                    CenterRow {
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
                        text = channelItem.introduction,
                        maxLines = 2,
                        color = Colors.gray400
                    )
                    CenterRow {
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

            LazyRowFor(items = channelItem.categoryList,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(categoryId) {
                        start.linkTo(coverId.start)
                        end.linkTo(titleId.end)
                        top.linkTo(introductionId.bottom, Dimens.m3)
                    }
            ) { item ->
                OutlinedButton(
                    shape = RoundedCornerShape(50F),
                    onClick = { /* todo: [Amy] click event */ },
                    colors = ButtonConstants.defaultButtonColors(backgroundColor = Colors.black),
                    border = BorderStroke(Dimens.buttonBorder, Colors.white),
                ) {
                    Text(
                        color = Colors.white,
                        text = item.name
                    )
                }

                Spacer(modifier = Modifier.preferredWidth(Dimens.m2))
            }
        }

        EpisodeItemList(
            channelName = channelItem.title,
            episodeItemList = channelItem.episodeList,
            modifier = Modifier.padding(start = Dimens.m4, end = Dimens.m4).fillMaxWidth()
        )
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
            introduction = "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction",
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