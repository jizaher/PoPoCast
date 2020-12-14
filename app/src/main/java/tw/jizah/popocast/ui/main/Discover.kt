package tw.jizah.popocast.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Fake discovery page
@Composable
fun Discover(channel: (Long) -> Unit,
             search: () -> Unit,
             episode: (Long, Long) -> Unit,
             options: (Long, Long) -> Unit) {
    Column (
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            channel(1L)
        }) {
            Text("channel")
        }
        Button(onClick = { episode(1L, 1L) }) {
            Text("episode")
        }
        Button(onClick = { search() }) {
            Text("search")
        }
        Button(onClick = { options(1L, 1L) }) {
            Text("options")
        }
    }
}
