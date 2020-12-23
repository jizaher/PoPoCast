package tw.jizah.popocast.model

data class ChannelItem(
    val id: Long,
    val imageUrl: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val isFollowed: Boolean,
    val episodeList: List<EpisodeItem>,
    val tagList: List<TagInfo>
)