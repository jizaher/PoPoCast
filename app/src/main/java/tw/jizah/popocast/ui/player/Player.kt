package tw.jizah.popocast.ui.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.VerticalGradient
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.viewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import tw.jizah.popocast.R
import tw.jizah.popocast.extensions.resize
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.widget.EllipsisText

@ExperimentalCoroutinesApi
@Composable
fun Player() {
    val viewModel: PlayerViewModel = viewModel(modelClass = PlayerViewModel::class.java)
    val isPlayingState by viewModel.isPlaying.collectAsState()
    val progressState by viewModel.progress.collectAsState()
    val speedState by viewModel.speed.collectAsState()

    val playerIconColor = Colors.white

    Box(modifier = Modifier
        .drawWithCache {
            val gradient = VerticalGradient(
                startY = 0.0F,
                endY = size.height,
                colors = listOf(Colors.blueGray800, Colors.blueGray900)
            )
            onDrawBehind {
                drawRect(brush = gradient)
            }
        }
    ) {

        Column(modifier = Modifier.padding(Dimens.m4)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        // TODO: Joseph add click event
                    }) {
                    Icon(
                        imageVector = Icons.Filled.KeyboardArrowDown,
                        tint = playerIconColor
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    EllipsisText(
                        text = "從podcast播放",
                        style = MaterialTheme.typography.subtitle2,
                        color = Colors.gray500,
                    )
                    EllipsisText(
                        text = "podcast title",
                        style = MaterialTheme.typography.subtitle1,
                        color = Colors.white,
                    )
                }
                IconButton(
                    onClick = {
                        // TODO: Joseph add click event
                    }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        tint = playerIconColor
                    )
                }
            }

            Spacer(Modifier.preferredHeight(Dimens.m4))

            ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
                val (img, title, owner, progress, startTime, endTime, controller) = createRefs()

                CoilImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(size = Dimens.m1))
                        .aspectRatio(1F)
                        .constrainAs(img) {
                            start.linkTo(title.start)
                            end.linkTo(title.end)
                            top.linkTo(parent.top)
                            bottom.linkTo(title.top, Dimens.m4)
                            linkTo(top = parent.top, bottom = title.top, bias = 0.0F)
                        },
                    data = "https://picsum.photos/300/300",
                    contentScale = ContentScale.Crop,
                )

                EllipsisText(
                    modifier = Modifier.fillMaxWidth().constrainAs(title) {
                        start.linkTo(owner.start)
                        end.linkTo(owner.end)
                        bottom.linkTo(owner.top, Dimens.m2)
                    },
                    text = "The KK Show - #53 黃豪平",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.h6,
                    color = Colors.white,
                )

                EllipsisText(
                    modifier = Modifier.fillMaxWidth().constrainAs(owner) {
                        start.linkTo(progress.start)
                        end.linkTo(progress.end)
                        bottom.linkTo(progress.top, Dimens.m4)
                    },
                    text = "百靈果NEWS",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.subtitle2,
                    color = Colors.gray500,
                )

                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().constrainAs(progress) {
                        start.linkTo(startTime.start)
                        end.linkTo(endTime.end)
                        bottom.linkTo(startTime.top, Dimens.m2)
                    },
                    progress = progressState,
                    color = Colors.yellow
                )

                val timeMarginBottom = Dimens.m4

                EllipsisText(
                    modifier = Modifier.constrainAs(startTime) {
                        start.linkTo(controller.start)
                        bottom.linkTo(controller.top, margin = timeMarginBottom)
                    },
                    text = "00:00",
                    style = MaterialTheme.typography.subtitle2,
                    color = Colors.gray500,
                )

                EllipsisText(
                    modifier = Modifier.constrainAs(endTime) {
                        end.linkTo(controller.end)
                        bottom.linkTo(controller.top, margin = timeMarginBottom)
                    },
                    text = "1:23:45",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.subtitle2,
                    color = Colors.gray500,
                )


                Row(
                    modifier = Modifier.fillMaxWidth()
                        .constrainAs(controller) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = Dimens.m8)
                        },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "${speedState.text}x",
                        color = playerIconColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .preferredWidth(Dimens.m8)
                            .clickable(onClick = {
                                viewModel.switchSpeed()
                            })
                    )

                    IconButton(
                        onClick = {
                            // TODO: Joseph add click event
                        },
                        content = {
                            Icon(
                                bitmap = imageResource(id = R.drawable.ic_back_15),
                                modifier = Modifier.preferredSize(Dimens.m8),
                                tint = playerIconColor
                            )
                        })

                    IconButton(
                        onClick = {
                            viewModel.togglePlayOrPause()
                        },
                        content = {
                            val icon =
                                if (isPlayingState) Icons.Filled.PauseCircleFilled else Icons.Filled.PlayCircleFilled
                            Icon(
                                imageVector = icon.resize(Dimens.m12),
                                tint = playerIconColor
                            )
                        })

                    IconButton(
                        onClick = {
                            // TODO: Joseph add click event
                        },
                        content = {
                            Icon(
                                bitmap = imageResource(id = R.drawable.ic_skip_15),
                                modifier = Modifier.preferredSize(Dimens.m8),
                                tint = playerIconColor
                            )
                        })

                    IconButton(
                        onClick = {
                            // TODO: Joseph add click event
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Filled.Share.resize(Dimens.m6),
                                tint = playerIconColor
                            )
                        })
                }
            }
        }
    }
}
