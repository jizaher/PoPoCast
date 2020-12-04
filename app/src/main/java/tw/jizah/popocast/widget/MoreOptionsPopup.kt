package tw.jizah.popocast.widget

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.model.OptionItem
import tw.jizah.popocast.model.OptionsCollection
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens

@Preview
@Composable
fun MoreOptionsPopupPreview() {
    MoreOptionsPopup(collection = getOptionsCollection(), onItemClicked = { })
}

@Composable
fun MoreOptionsPopup(collection: OptionsCollection, onItemClicked: (OptionItem) -> Unit) {
    ScrollableColumn(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Colors.black)
    ) {
        CoilImage(
            data = collection.image,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = Dimens.m8)
                .preferredSize(Dimens.optionPopupImageSize)
                .align(Alignment.CenterHorizontally)
        )
        EllipsisText(
            text = collection.title,
            color = Colors.white,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(Dimens.m2).align(Alignment.CenterHorizontally)
        )
        EllipsisText(
            text = collection.subtitle,
            color = Colors.gray600,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        collection.options.forEach {
            OptionItem(item = it, onItemClicked = onItemClicked)
        }
    }
}

@Composable
private fun OptionItem(item: OptionItem, onItemClicked: (OptionItem) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable(onClick = { onItemClicked(item) }),
    ) {
        Icon(
            asset = item.icon,
            tint = Colors.gray600,
            modifier = Modifier.padding(Dimens.m4).preferredSize(Dimens.m8)
        )
        Text(
            text = item.name,
            color = Colors.white,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(top = Dimens.m4, bottom = Dimens.m4, end = Dimens.m4)
        )
    }
}

private fun getOptionsCollection(): OptionsCollection {
    return OptionsCollection(
        image = "https://vignette.wikia.nocookie.net/whm/images/8/83/Whm_2019_logo.png/revision/latest?cb=20190825181343",
        title = "We Hate Movies",
        subtitle = "Podcast",
        options = listOf(
            OptionItem(icon = Icons.Filled.Favorite, name = "Like"),
            OptionItem(icon = Icons.Filled.EmojiEmotions, name = "Follow"),
            OptionItem(icon = Icons.Filled.Share, name = "Share"),
            OptionItem(icon = Icons.Filled.AddToQueue, name = "Add to Queue"),
            OptionItem(icon = Icons.Filled.PlaylistAdd, name = "Sleep timer"),
            OptionItem(icon = Icons.Filled.Report, name = "Report Explicit Content"),
            OptionItem(icon = Icons.Filled.Info, name = "Show Credits"),
        )
    )
}