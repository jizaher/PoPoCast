package tw.jizah.popocast.ui.player

import androidx.annotation.IntRange
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tw.jizah.popocast.constant.Const.PLAYER_END_PERCENTAGE
import tw.jizah.popocast.constant.Const.PLAYER_START_PERCENTAGE

class PlayerViewModel : ViewModel() {

    val isPlaying: StateFlow<Boolean>
        get() = _isPlaying
    private val _isPlaying = MutableStateFlow(false)

    // TODO: [Joseph] Fake value
    val progress: StateFlow<Float>
        get() = _progress
    private val _progress = MutableStateFlow(0.25F)

    val speed: StateFlow<Speed>
        get() = _speed
    private val _speed = MutableStateFlow(Speed.Normal)

    fun togglePlayOrPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun setProgress(
        @IntRange(from = PLAYER_START_PERCENTAGE, to = PLAYER_END_PERCENTAGE) progress: Int
    ) {
        _progress.value = (progress / PLAYER_END_PERCENTAGE).toFloat()
    }

    fun switchSpeed() {
        val currentSpeed = _speed.value
        val speedValues = Speed.values()
        val index = speedValues.indexOf(currentSpeed)
        val nextSpeedIndex = if (index == speedValues.lastIndex) 0 else index + 1
        _speed.value = speedValues[nextSpeedIndex]
    }

    enum class Speed(val text: String) {
        SLOW("0.5"), Normal("1"), FAST("2")
    }
}
