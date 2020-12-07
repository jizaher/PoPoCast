package tw.jizah.popocast

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.setContent
import tw.jizah.popocast.model.CategoryItem
import tw.jizah.popocast.model.ChannelItem
import tw.jizah.popocast.model.EpisodeItem
import tw.jizah.popocast.ui.channel.ChannelPage
import tw.jizah.popocast.ui.theme.PoPoCastTheme
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PoPoCastTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ChannelPage(
                        ChannelItem(
                            imageUrl = "https://picsum.photos/300/300",
                            title = "Channel title",
                            subtitle = "Author",
                            description = "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction" +
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction" +
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+"This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"
                            +"This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+"This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"
                            +"This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"
                            +"This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                            "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+
                                    "This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+"This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction"+"This is introduction. This is introduction. This is introduction.\nThis is introduction\nThis is introduction\nThis is introduction",
                            isFollowed = true,
                            isExpanded = true,
                            categoryList = listOf(CategoryItem("Comedy", ""), CategoryItem("Knowledge", "")),
                            episodeList = (0..10).map { index ->
                                EpisodeItem(
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
                    )
                }
            }
        }
    }
}