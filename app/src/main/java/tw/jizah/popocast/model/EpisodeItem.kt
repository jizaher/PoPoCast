package tw.jizah.popocast.model

data class EpisodeItem(
    val channelId: Long,
    val id: Long,
    val imageUrl: String,
    val title: String,
    val subtitle: String,
    val releaseTime: Long,
    val duration: Long,
    val description: String
)