package tw.jizah.popocast.ui.channel

import androidx.lifecycle.ViewModel
import tw.jizah.popocast.model.CategoryItem
import tw.jizah.popocast.model.ChannelItem
import tw.jizah.popocast.model.EpisodeItem
import java.util.*
import java.util.concurrent.TimeUnit

class ChannelViewModel : ViewModel() {
    fun getChannel(channelId: Long): ChannelItem {
        // TODO: [AMY] get channel data from db
        return ChannelItem(
            id = channelId,
            imageUrl = "https://picsum.photos/300/300",
            title = "Channel title",
            subtitle = "Author",
            description = "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction",
            isFollowed = true,
            isExpanded = true,
            categoryList = listOf(CategoryItem("Comedy", ""), CategoryItem("Knowledge", "")),
            episodeList = (0..10).map { index ->
                EpisodeItem(
                    id = index.toLong(),
                    imageUrl = "https://picsum.photos/300/300",
                    itemName = "This is item title $index",
                    itemInfo = "This is item info",
                    releaseTime = Calendar.getInstance().timeInMillis - TimeUnit.MILLISECONDS.convert(
                        (index + 1).toLong(),
                        TimeUnit.DAYS
                    ),
                    duration = 0L,
                    description = ""
                )
            }
        )
    }
}