package tw.jizah.popocast.ui.player

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.viewinterop.viewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.widget.EllipsisText

@Composable
fun PlayerController() {
    val viewModel: PlayerViewModel = viewModel(modelClass = PlayerViewModel::class.java)
    val isPlaying by viewModel.isPlaying.collectAsState()
    val progress by viewModel.progress.collectAsState()

    Surface(color = Colors.playerControllerBg) {
        Column {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = progress,
                color = Colors.playerControllerProgress
            )
            val size = Dimens.playerControllerSize
            Row(modifier = Modifier.height(size).fillMaxWidth()) {
                Box(modifier = Modifier.width(size)) {
                    CoilImage(
                        data = "https://picsum.photos/300/300",
                        contentScale = ContentScale.Crop,
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(8F)
                        .padding(Dimens.playerControllerTextPadding)
                ) {
                    Column(modifier = Modifier.align(Alignment.CenterStart)) {
                        EllipsisText(
                            text = "podcast title podcast title podcast title podcast title",
                            style = MaterialTheme.typography.subtitle1,
                            color = Colors.playerControllerTitleText,
                        )
                        EllipsisText(
                            text = "podcast owner",
                            style = MaterialTheme.typography.subtitle2,
                            color = Colors.playerControllerOwnerText,
                        )
                    }
                }
                Box(
                    modifier = Modifier.weight(2F).fillMaxSize(),
                ) {
                    IconButton(
                        onClick = { viewModel.togglePlayOrPause() },
                        modifier = Modifier.align(Alignment.Center),
                    ) {
                        val icon = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow
                        Icon(asset = icon, tint = Colors.white)
                    }
                }
            }
        }
    }
}