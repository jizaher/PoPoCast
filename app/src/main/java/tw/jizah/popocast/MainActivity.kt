package tw.jizah.popocast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import tw.jizah.popocast.ui.player.PlayerController
import tw.jizah.popocast.ui.theme.PoPoCastTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PoPoCastTheme {
                Surface(color = MaterialTheme.colors.background) {
                }
            }
        }
    }
}