package tw.jizah.popocast.model

data class ChannelItem(
    val imageUrl: String,
    val title: String,
    val subtitle: String,
    val introduction: String,
    val isFollowed: Boolean,
    val isExpanded: Boolean,
    val episodeList: List<EpisodeItem>,
    val categoryList: List<CategoryItem>
)