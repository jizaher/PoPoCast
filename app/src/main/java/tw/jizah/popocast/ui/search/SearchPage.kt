package tw.jizah.popocast.ui.search

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.DpPropKey
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.node.Ref
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import tw.jizah.popocast.R
import tw.jizah.popocast.model.SearchItem
import tw.jizah.popocast.ui.theme.Colors
import tw.jizah.popocast.ui.theme.Dimens
import tw.jizah.popocast.widget.EllipsisText

@ExperimentalFocus
@Composable
@Preview
fun SearchPagePreview() {
    SearchPage()
}

@ExperimentalFocus
@Composable
fun SearchPage() {
    Surface(color = Colors.black, modifier = Modifier.fillMaxSize()) {
        SearchContent()
    }
}

@ExperimentalFocus
@Composable
fun SearchContent() {
    Column {
        val isSearching = remember { mutableStateOf(false) }
        val transitionState = transition(
            definition = searchTransitionDef,
            initState = isSearching.value,
            toState = !isSearching.value
        )
        SearchBar(isSearching, transitionState)
        RecentSearchTitle()
        RecentSearchList(getSearchItems()) {
            // TODO: Ivan
        }
        ClearRecentSearches {
            // TODO: Ivan
        }
    }
}

private val outerPadding = DpPropKey()
private val innerPadding = DpPropKey()
private val visibilityProp = FloatPropKey()
private val colorProp = ColorPropKey()
private val roundCornerProp = DpPropKey()
private const val transitionDurationMillis = 300

val searchTransitionDef = transitionDefinition<Boolean> {
    state(false) {
        this[outerPadding] = 0.dp
        this[innerPadding] = Dimens.m4
        this[visibilityProp] = 1F
        this[colorProp] = Colors.transparent
        this[roundCornerProp] = 0.dp
    }
    state(true) {
        this[outerPadding] = Dimens.m2
        this[innerPadding] = Dimens.m2
        this[visibilityProp] = 0F
        this[colorProp] = Colors.white
        this[roundCornerProp] = Dimens.m1
    }
    transition {
        outerPadding using tween(durationMillis = transitionDurationMillis)
        innerPadding using tween(durationMillis = transitionDurationMillis)
        visibilityProp using tween(durationMillis = transitionDurationMillis)
    }
}

@ExperimentalFocus
@Composable
fun SearchBar(isSearchingState: MutableState<Boolean>, transitionState: TransitionState) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .padding(transitionState[outerPadding])
                .clickable(onClick = {
                    if (!isSearchingState.value) isSearchingState.value = true
                })
        ) {
            Text(
                text = stringResource(id = R.string.search),
                textAlign = TextAlign.Center,
                color = transitionState[colorProp],
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Colors.gray800,
                        shape = RoundedCornerShape(transitionState[roundCornerProp])
                    ).padding(transitionState[innerPadding]),
                style = MaterialTheme.typography.subtitle1
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            IconButton(
                onClick = { isSearchingState.value = false },
                modifier = Modifier.drawOpacity(transitionState[visibilityProp])
            ) {
                Icon(
                    asset = Icons.Filled.ArrowBack,
                    tint = Colors.white
                )
            }
            if (isSearchingState.value) {
                SearchTextField()
            }
        }
    }
}

@ExperimentalFocus
@Composable
fun SearchTextField() {
    val focusRequester = FocusRequester()
    val textState = remember { mutableStateOf("") }
    val keyboardController: Ref<SoftwareKeyboardController> = remember { Ref() }
    BasicTextField(
        value = textState.value,
        textStyle = TextStyle.Default.copy(color = Colors.white),
        onValueChange = { textState.value = it },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        maxLines = 1,
        onImeActionPerformed = { action ->
            if (action == ImeAction.Search) keyboardController.value?.hideSoftwareKeyboard()
        },
        onTextInputStarted = { keyboardController.value = it },
        cursorColor = Colors.greenA700,
        modifier = Modifier
            .background(color = Colors.transparent)
            .focusRequester(focusRequester)
    )
    onActive {
        focusRequester.requestFocus()
    }
}

@Composable
fun RecentSearchTitle() {
    Row(modifier = Modifier.fillMaxWidth().padding(Dimens.m3)) {
        Text(
            text = stringResource(id = R.string.recent_searches),
            color = Colors.white,
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
            .padding(Dimens.m3)
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
                .preferredSize(Dimens.searchItemImageSize)
                .clip(shape = RoundedCornerShape(Dimens.m1)),
            error = {
                Image(asset = imageResource(id = R.mipmap.ic_launcher))
            }
        )
        EllipsisText(
            text = item.title,
            color = Colors.white,
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
            color = Colors.gray600,
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
                    end.linkTo(parent.end, Dimens.m3)
                }
        ) {
            Icon(
                asset = Icons.Filled.Close,
                tint = Colors.white
            )
        }
    }
}

@Composable
fun ClearRecentSearches(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.m3)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = stringResource(id = R.string.clear_recent_searches),
            color = Colors.gray600,
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

