package tw.jizah.popocast.ui.player

import androidx.annotation.IntRange
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tw.jizah.popocast.constant.Const
import tw.jizah.popocast.constant.Const.PLAYER_END_PERCENTAGE
import tw.jizah.popocast.constant.Const.PLAYER_START_PERCENTAGE

class PlayerViewModel : ViewModel() {
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean>
        get() = _isPlaying


    // TODO: [Joseph] Fake value
    private val _progress = MutableStateFlow(1F)
    val progress: StateFlow<Float>
        get() = _progress


    fun togglePlayOrPause() {
        _isPlaying.value = !_isPlaying.value
    }

    fun setProgress(
        @IntRange(from = PLAYER_START_PERCENTAGE, to = PLAYER_END_PERCENTAGE) progress: Int
    ) {
        _progress.value = (progress / PLAYER_END_PERCENTAGE).toFloat()
    }
}
