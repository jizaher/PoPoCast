package tw.jizah.popocast.ui.channel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
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
import tw.jizah.popocast.widget.EllipsisText
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun ChannelPage(channelItem: ChannelItem) {
    Column(modifier = Modifier.fillMaxSize().background(Colors.black)) {
        ConstraintLayout(modifier = Modifier.padding(horizontal = Dimens.m4).fillMaxWidth()) {
            val (actionBack, coverId, titleId, subtitleId,
                followBtnId, moreBtnId, introductionId,
                seeMoreId, categoryId) = createRefs()

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

            val maxLines = if (channelItem.isExpanded) Int.MAX_VALUE else 2
            EllipsisText(
                text = channelItem.introduction,
                color = Colors.gray400,
                maxLines = maxLines,
                modifier = Modifier.constrainAs(introductionId) {
                    start.linkTo(coverId.start)
                    end.linkTo(seeMoreId.start)
                    top.linkTo(followBtnId.bottom, Dimens.m3)
                    width = Dimension.fillToConstraints
                })

            val stringId = if (channelItem.isExpanded) R.string.see_less else R.string.see_more
            Text(text = stringResource(id = stringId),
                color = Colors.white,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(seeMoreId) {
                    end.linkTo(titleId.end)
                    top.linkTo(introductionId.top)
                    bottom.linkTo(introductionId.bottom)
                    height = Dimension.wrapContent
                    width = Dimension.wrapContent
                })

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
fun ChannelPagePreview() {
    ChannelPage(
        ChannelItem(
            imageUrl = "https://picsum.photos/300/300",
            title = "百靈果NEWS",
            subtitle = "百靈果NEWS",
            introduction = "在這裏，Kylie跟Ken，用雙語的對話包裝知識，用輕鬆的口吻胡說八道。我們閒聊也談正經事，讓生硬的國際大事變得鬆軟好入口；歡迎你加入這外表看似嘴砲，內容卻異於常人的有料聊天。",
            isFollowed = true,
            isExpanded = true,
            categoryList = listOf(CategoryItem("喜劇", ""), CategoryItem("知識", "")),
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