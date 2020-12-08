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
    val isPlayingState by viewModel.isPlaying.collectAsState()
    val progressState by viewModel.progress.collectAsState()

    Surface(color = Colors.gray900) {
        Column {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = progressState,
                color = Colors.yellow
            )
            val size = Dimens.playerControllerSize
            Row(modifier = Modifier.preferredHeight(size).fillMaxWidth()) {
                Box(modifier = Modifier.width(size)) {
                    CoilImage(
                        data = "https://picsum.photos/300/300",
                        contentScale = ContentScale.Crop
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(8F)
                        .padding(Dimens.m1)
                ) {
                    Column(modifier = Modifier.align(Alignment.CenterStart)) {
                        EllipsisText(
                            text = "podcast title podcast title podcast title podcast title",
                            style = MaterialTheme.typography.subtitle1,
                            color = Colors.white,
                        )
                        EllipsisText(
                            text = "podcast owner",
                            style = MaterialTheme.typography.subtitle2,
                            color = Colors.gray500,
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
                        val icon =
                            if (isPlayingState) Icons.Filled.Pause else Icons.Filled.PlayArrow
                        Icon(imageVector = icon, tint = Colors.white)
                    }
                }
            }
        }
    }
}