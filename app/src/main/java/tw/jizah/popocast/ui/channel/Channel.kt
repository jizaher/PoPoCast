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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.ui.theme.Dimens.channelCoverSize
import tw.jizah.popocast.ui.theme.Dimens.m2
import tw.jizah.popocast.ui.theme.Dimens.m3
import tw.jizah.popocast.ui.theme.Dimens.m4
import tw.jizah.popocast.widget.EllipsisText
import java.util.*
import java.util.concurrent.TimeUnit

data class ChannelInfo(
    val imageUrl: String,
    val title: String,
    val subtitle: String,
    val introduction: String,
    val isFollowed: Boolean,
    val isExpanded: Boolean,
    val itemInfo: ItemInfo,
    val categoryList: List<Category>
)

data class Category(val name: String, val url: String)

@Composable
fun ChannelPage(channelInfo: ChannelInfo) {

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        ConstraintLayout(modifier = Modifier.padding(horizontal = m4).fillMaxWidth()) {
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
                    tint = Color.White
                )
            }

            CoilImage(data = channelInfo.imageUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier.preferredSize(channelCoverSize)
                    .clip(MaterialTheme.shapes.medium)
                    .constrainAs(coverId) {
                        start.linkTo(parent.start)
                        top.linkTo(actionBack.bottom, m3)
                    })

            EllipsisText(text = channelInfo.title,
                style = MaterialTheme.typography.h4,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(titleId) {
                    start.linkTo(coverId.end, m4)
                    end.linkTo(parent.end)
                    top.linkTo(coverId.top)
                    bottom.linkTo(coverId.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.wrapContent
                }
            )
            EllipsisText(text = channelInfo.subtitle,
                color = Color.White,
                modifier = Modifier.constrainAs(subtitleId) {
                    start.linkTo(titleId.start)
                    end.linkTo(titleId.end)
                    top.linkTo(titleId.bottom)
                    width = Dimension.fillToConstraints
                }
            )

            OutlinedButton(onClick = { /* todo */ },
                colors = ButtonConstants.defaultButtonColors(
                    backgroundColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.White),
                modifier = Modifier.constrainAs(followBtnId) {
                    top.linkTo(coverId.bottom, m3)
                    start.linkTo(coverId.start)
                }) {

                val textResId = if (channelInfo.isFollowed) {
                    R.string.followed
                } else {
                    R.string.not_yet_followed
                }
                Text(
                    color = Color.White,
                    text = stringResource(id = textResId)
                )
            }

            IconButton(onClick = {},
                modifier = Modifier.constrainAs(moreBtnId) {
                    top.linkTo(followBtnId.top)
                    bottom.linkTo(followBtnId.bottom)
                    end.linkTo(parent.end)
                }) {
                Icon(
                    asset = Icons.Default.MoreVert,
                    tint = Color.LightGray
                )
            }

            val maxLines = if (channelInfo.isExpanded) Int.MAX_VALUE else 2
            EllipsisText(
                text = channelInfo.introduction,
                color = Color.LightGray,
                maxLines = maxLines,
                modifier = Modifier.constrainAs(introductionId) {
                    start.linkTo(coverId.start)
                    end.linkTo(seeMoreId.start)
                    top.linkTo(followBtnId.bottom, m3)
                    width = Dimension.fillToConstraints
                })

            val stringId = if (channelInfo.isExpanded) R.string.see_less else R.string.see_more
            Text(text = stringResource(id = stringId),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(seeMoreId) {
                    end.linkTo(titleId.end)
                    top.linkTo(introductionId.top)
                    bottom.linkTo(introductionId.bottom)
                    height = Dimension.wrapContent
                    width = Dimension.wrapContent
                })

            LazyRowFor(items = channelInfo.categoryList,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(categoryId) {
                        start.linkTo(coverId.start)
                        end.linkTo(titleId.end)
                        top.linkTo(introductionId.bottom, m3)
                    }
            ) { item ->
                OutlinedButton(
                    shape = RoundedCornerShape(m4),
                    onClick = { /* todo */ },
                    colors = ButtonConstants.defaultButtonColors(backgroundColor = Color.Black),
                    border = BorderStroke(1.dp, Color.White),
                ) {

                    Text(
                        color = Color.White,
                        text = item.name
                    )
                }

                Spacer(modifier = Modifier.preferredWidth(m2))
            }
        }

        EpisodeItemList(
            itemInfo = channelInfo.itemInfo,
            modifier = Modifier.padding(start = m4, end = m4).fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun ChannelPagePreview() {
    ChannelPage(
        ChannelInfo(
            imageUrl = "https://picsum.photos/300/300",
            title = "百靈果NEWS",
            subtitle = "百靈果NEWS",
            introduction = "在這裏，Kylie跟Ken，用雙語的對話包裝知識，用輕鬆的口吻胡說八道。我們閒聊也談正經事，讓生硬的國際大事變得鬆軟好入口；歡迎你加入這外表看似嘴砲，內容卻異於常人的有料聊天。",
            isFollowed = true,
            isExpanded = true,
            categoryList = listOf(Category("喜劇", ""), Category("知識", "")),
            itemInfo = ItemInfo(
                channelName = "百靈果NEWS",
                itemList = (0..10).map { index ->
                    ItemInfo.Item(
                        imageUrl = "https://picsum.photos/300/300",
                        itemName = "This is item title $index",
                        itemInfo = "This is item info",
                        releaseTime = Calendar.getInstance().timeInMillis - TimeUnit.MILLISECONDS.convert(
                            (index + 1).toLong(),
                            TimeUnit.DAYS
                        )
                    )
                })
        )
    )
}