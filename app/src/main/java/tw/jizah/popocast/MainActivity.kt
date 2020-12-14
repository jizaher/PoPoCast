package tw.jizah.popocast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.platform.setContent

@ExperimentalFocus
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavGraph()
        }
    }
}