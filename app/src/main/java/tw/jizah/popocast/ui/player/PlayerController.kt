package tw.jizah.popocast.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.ui.theme.playerControllerBgColor
import tw.jizah.popocast.ui.theme.playerControllerOwnerTextColor
import tw.jizah.popocast.widget.EllipsisText

@Composable
fun PlayerController() {
    val viewModel: PlayerViewModel = viewModel(modelClass = PlayerViewModel::class.java)
    val isPlaying by viewModel.isPlaying.collectAsState()

    val size = 80.dp
    Surface(color = playerControllerBgColor) {
        Row(modifier = Modifier.height(size).fillMaxWidth()) {
            Box(modifier = Modifier.width(size)) {
                CoilImage(
                    data = "https://picsum.photos/300/300",
                    contentScale = ContentScale.Crop,
                )
            }
            Box(modifier = Modifier.fillMaxSize().weight(8F).padding(4.dp)) {
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    EllipsisText(
                        text = "podcast title podcast title podcast title podcast title",
                        style = MaterialTheme.typography.subtitle1,
                        color = Color.White,
                    )
                    EllipsisText(
                        text = "podcast owner",
                        style = MaterialTheme.typography.subtitle2,
                        color = playerControllerOwnerTextColor,
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
                    val id = if (isPlaying) {
                        android.R.drawable.ic_media_pause
                    } else {
                        android.R.drawable.ic_media_play
                    }
                    loadImageResource(id).resource.resource?.let {
                        Image(asset = it)
                    }
                }
            }
        }
    }
}