package tw.jizah.popocast.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.model.SearchItem
import tw.jizah.popocast.ui.theme.Colors.black
import tw.jizah.popocast.ui.theme.Colors.gray600
import tw.jizah.popocast.ui.theme.Colors.gray800
import tw.jizah.popocast.ui.theme.Colors.white
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.ui.theme.Dimens.searchItemImageSize
import tw.jizah.popocast.widget.EllipsisText

@Composable
@Preview
fun SearchPagePreview() {
    SearchPage()
}

@Composable
fun SearchPage() {
    Surface(color = black, modifier = Modifier.fillMaxSize()) {
        SearchContent()
    }
}

@Composable
fun SearchContent() {
    Column {
        SearchBar()
        RecentSearchTitle()
        RecentSearchList(getSearchItems()) {
            // TODO: Ivan
        }
        ClearRecentSearches {
            // TODO: Ivan
        }
    }
}

@Composable
fun SearchBar() {
    Row(modifier = Modifier.fillMaxWidth().padding(Dimens.m2)) {
        Text(
            text = stringResource(id = R.string.search),
            textAlign = TextAlign.Center,
            color = white,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = gray800,
                    shape = RoundedCornerShape(Dimens.m1)
                ).padding(Dimens.m2),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun RecentSearchTitle() {
    Row(modifier = Modifier.fillMaxWidth().padding(Dimens.m2)) {
        Text(
            text = stringResource(id = R.string.recent_searches),
            color = white,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun RecentSearchList(list: List<SearchItem>, onClick: (SearchItem) -> Unit) {
    LazyColumnFor(items = list) {
        RecentSearchRow(item = it, onClick = onClick)
    }
}

@Composable
fun RecentSearchRow(item: SearchItem, onClick: (SearchItem) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.m2)
            .clickable(onClick = { onClick(item) })
    ) {
        val (cover, title, subtitle, close) = createRefs()
        CoilImage(
            data = item.cover,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .constrainAs(cover) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(title.start)
                    start.linkTo(parent.start)
                }
                .preferredSize(searchItemImageSize)
                .clip(shape = RoundedCornerShape(Dimens.m1)),
            error = {
                Image(asset = imageResource(id = R.mipmap.ic_launcher))
            }
        )
        EllipsisText(
            text = item.title,
            color = white,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    bottom.linkTo(subtitle.top)
                    start.linkTo(cover.end, Dimens.m3)
                    end.linkTo(close.start, Dimens.m3)
                    width = Dimension.fillToConstraints
                }
        )
        EllipsisText(
            text = item.subtitle,
            color = gray600,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier
                .constrainAs(subtitle) {
                    top.linkTo(title.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(cover.end, Dimens.m3)
                    end.linkTo(close.start, Dimens.m3)
                    width = Dimension.fillToConstraints
                }
        )
        IconButton(
            onClick = {
                // TODO: Ivan
            },
            modifier = Modifier
                .preferredSize(Dimens.m6)
                .constrainAs(close) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(title.end)
                    end.linkTo(parent.end, Dimens.m2)
                }
        ) {
            Icon(
                asset = Icons.Filled.Close,
                tint = white
            )
        }
    }
}

@Composable
fun ClearRecentSearches(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.m2)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = stringResource(id = R.string.clear_recent_searches),
            color = gray600,
            style = MaterialTheme.typography.subtitle1
        )
    }
}

private fun getSearchItems(): List<SearchItem> {
    return listOf(
        SearchItem(
            cover = "https://upload.wikimedia.org/wikipedia/en/4/4b/The_Joe_Rogan_Experience_logo.jpg",
            title = "The Joe Rogan Experience",
            subtitle = "Podcast"
        ),
        SearchItem(
            cover = "https://upload.wikimedia.org/wikipedia/en/2/29/On_the_Media_%28logo%29.png",
            title = "On The Media",
            subtitle = "Podcast"
        ),
        SearchItem(
            cover = "https://vignette.wikia.nocookie.net/whm/images/8/83/Whm_2019_logo.png/revision/latest?cb=20190825181343",
            title = "We Hate Movies",
            subtitle = "Podcast"
        )
    )
}

