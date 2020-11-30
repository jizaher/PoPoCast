package tw.jizah.popocast.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.model.CategoryCollection
import tw.jizah.popocast.model.CategoryItem
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.widget.VerticalGrid
import kotlin.math.round

@Composable
@Preview
fun SearchCategoriesPagePreview() {
    SearchCategoriesPage()
}

@Composable
fun SearchCategoriesPage() {
    Surface(
        color = Colors.black,
        modifier = Modifier.fillMaxSize()
    ) {
        val state = rememberLazyListState()
        val searchSectionHeight = remember { mutableStateOf(0.dp) }
        Box {
            LazyColumn(state = state) {
                item {
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = Dimens.m6)
                            .preferredHeight(searchSectionHeight.value)
                    )
                }
                items(getCategoryCollections()) { SearchCategoryCollection(collection = it) }
            }
            SearchSection(state, searchSectionHeight)
        }
    }
}

@Composable
fun SearchSection(state: LazyListState, searchSectionHeight: MutableState<Dp>) {
    Layout(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Colors.black)
            .padding(Dimens.m3),
        children = { SearchSectionContent() }
    ) { measurables, constraints ->
        val textPlaceable = measurables[0].measure(constraints)
        val searchBarPlaceable = measurables[1].measure(constraints)
        searchSectionHeight.value = (textPlaceable.height + searchBarPlaceable.height).toDp()
        val placeY: Int = if (state.firstVisibleItemIndex == 0
            && state.firstVisibleItemScrollOffset < textPlaceable.height
        ) {
            state.firstVisibleItemScrollOffset
        } else {
            textPlaceable.height
        }
        val remainingTextHeight = textPlaceable.height - placeY

        layout(
            width = constraints.maxWidth,
            height = searchBarPlaceable.height + remainingTextHeight
        ) {
            textPlaceable.place(0, -placeY)
            searchBarPlaceable.place(0, remainingTextHeight)
        }
    }
}

@Composable
fun SearchSectionContent() {
    Text(
        text = stringResource(id = R.string.search),
        color = Colors.white,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(bottom = Dimens.m3)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(
                color = Colors.white,
                shape = RoundedCornerShape(Dimens.m1)
            )
            .fillMaxWidth()
            .padding(Dimens.m3)
    ) {
        Icon(asset = Icons.Filled.Search, tint = Colors.gray700)
        Text(
            text = stringResource(id = R.string.podcasts),
            color = Colors.gray700,
            style = MaterialTheme.typography.body1,
            modifier = Modifier.padding(start = Dimens.m3)
        )
    }
}

@Composable
private fun SearchCategoryCollection(
    collection: CategoryCollection,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = collection.name,
            style = MaterialTheme.typography.h6,
            color = Colors.white,
            modifier = Modifier
                .padding(horizontal = Dimens.m3, vertical = Dimens.m1)
                .wrapContentHeight()
        )
        VerticalGrid(modifier = Modifier.padding(Dimens.searchCategoryPadding)) {
            collection.categories.forEach { category ->
                SearchCategory(
                    category = category,
                    modifier = Modifier.padding(Dimens.searchCategoryPadding)
                )
            }
        }
        Spacer(Modifier.preferredHeight(Dimens.m1))
    }
}

@Composable
private fun SearchCategory(category: CategoryItem, modifier: Modifier = Modifier) {
    val categorySizeRatio = 1.6F
    val categoryTextRatio = 0.7F
    val categoryImageRatio = 0.8F
    val categoryImageYRatio = 0.25F

    Layout(
        modifier = modifier
            .aspectRatio(categorySizeRatio)
            .clip(RoundedCornerShape(Dimens.m1))
            .background(color = MaterialTheme.colors.primaryVariant)
            .clickable(onClick = {
                // TODO: Ivan
            }),
        children = { SearchCategoryContent(category) }
    ) { measurables, constraints ->
        val textWidth = (constraints.maxWidth * categoryTextRatio).toInt()
        val textPlaceable = measurables[0].measure(Constraints.fixedWidth(textWidth))
        val imageSize = round(constraints.maxHeight * categoryImageRatio).toInt()
        val imagePlaceable = measurables[1].measure(Constraints.fixed(imageSize, imageSize))
        layout(
            width = constraints.maxWidth,
            height = constraints.minHeight
        ) {
            textPlaceable.place(x = 0, y = 0)
            imagePlaceable.place(
                x = textWidth,
                y = (constraints.maxHeight * categoryImageYRatio).toInt()
            )
        }
    }
}

@Composable
private fun SearchCategoryContent(category: CategoryItem) {
    val rotationZ = 25F

    Text(
        text = category.name,
        style = MaterialTheme.typography.h6,
        color = Colors.white,
        modifier = Modifier.padding(horizontal = Dimens.m3, vertical = Dimens.m3)
    )
    CoilImage(
        data = category.url,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .drawLayer(rotationZ = rotationZ)
            .drawShadow(Dimens.m1)
    )
}

private fun getCategoryCollections(): List<CategoryCollection> {
    return listOf(
        CategoryCollection(
            name = "Your top categories",
            categories = listOf(
                CategoryItem(
                    name = "Stories",
                    url = "https://upload.wikimedia.org/wikipedia/en/8/8f/WTF_with_Marc_Maron.png"
                ),
                CategoryItem(
                    name = "True crime",
                    url = "https://upload.wikimedia.org/wikipedia/en/7/73/My_Favorite_Murder_Podcast_Logo.jpeg"
                ),
                CategoryItem(
                    name = "News",
                    url = "https://upload.wikimedia.org/wikipedia/en/b/b7/The_Daily_logo.jpg"
                ),
                CategoryItem(
                    name = "Comedy",
                    url = "https://upload.wikimedia.org/wikipedia/en/9/9f/Unqualified_logo.jpg"
                )
            )
        ),
        CategoryCollection(
            name = "Browse all",
            categories = listOf(
                CategoryItem(
                    name = "Sports",
                    url = "https://upload.wikimedia.org/wikipedia/en/5/56/ESPN_FC_logo.jpg"
                ),
                CategoryItem(
                    name = "Educational",
                    url = "https://upload.wikimedia.org/wikipedia/en/9/94/StuffYouShouldKnow.jpg"
                ),
                CategoryItem(
                    name = "Lifestyle",
                    url = "https://upload.wikimedia.org/wikipedia/en/2/2f/BBC_World_Service_The_Forum.png"
                ),
                CategoryItem(
                    name = "Business",
                    url = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/32/BBC_World_News_2019.svg/300px-BBC_World_News_2019.svg.png"
                ),
                CategoryItem(
                    name = "Arts",
                    url = "https://upload.wikimedia.org/wikipedia/en/e/ee/Conan_O%27Brien_Needs_a_Friend_podcast.jpg"
                ),
                CategoryItem(
                    name = "Health",
                    url = "https://upload.wikimedia.org/wikipedia/en/9/94/Pod_save_the_world_logo.jpg"
                ),
                CategoryItem(
                    name = "Music",
                    url = "https://upload.wikimedia.org/wikipedia/en/c/c7/NPR_Planet_Money_cover_art.jpg"
                ),
                CategoryItem(
                    name = "Games",
                    url = "https://upload.wikimedia.org/wikipedia/en/e/e9/NPR_Code_Switch_cover_art.png"
                )
            )
        )
    )
}