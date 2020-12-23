package tw.jizah.popocast.model

data class EpisodeItem(
    val id: Long,
    val imageUrl: String,
    val itemName: String,
    val itemInfo: String,
    val releaseTime: Long,
    val duration: Long,
    val description: String
)