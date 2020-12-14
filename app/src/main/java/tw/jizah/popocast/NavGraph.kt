package tw.jizah.popocast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.ExperimentalFocus
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import tw.jizah.popocast.MainDestinations.CHANNEL_ID_KEY
import tw.jizah.popocast.MainDestinations.CHANNEL_ITEM_KEY
import tw.jizah.popocast.MainDestinations.EPISODE_DETAIL_ID_KEY
import tw.jizah.popocast.ui.channel.ChannelPage
import tw.jizah.popocast.ui.main.Discover
import tw.jizah.popocast.ui.search.SearchPage

object MainDestinations {
    const val DISCOVERY_ROUTE = "discovery"
    const val CHANNEL_ROUTE = "channel"
    const val EPISODE_DETAIL_ROUTE = "episode_detail"
    const val SEARCH_ROUTE = "search"
    const val EPISODE_OPTIONS_ROUTE = "episode_options"
    const val CHANNEL_ID_KEY = "channelId"
    const val CHANNEL_ITEM_KEY = "key_channel_item"
    const val EPISODE_DETAIL_ID_KEY = "episode_id"
}

@ExperimentalFocus
@Composable
fun NavGraph(startDestination: String = MainDestinations.DISCOVERY_ROUTE) {
    val navController = rememberNavController()

    val actions = remember(navController) { MainActions(navController) }
    NavHost(navController, startDestination = startDestination) {
        composable(MainDestinations.DISCOVERY_ROUTE) {
            // TODO : fake entrance
            Discover(
                channel = actions.enterChannelPage,
                search = actions.enterSearchPage,
                episode = actions.enterEpisodePage,
                options = actions.enterEpisodeOptionsPage
            )
        }
        composable(
            route = "${MainDestinations.CHANNEL_ROUTE}/{$CHANNEL_ITEM_KEY}",
            arguments = listOf(navArgument(CHANNEL_ITEM_KEY) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getLong(CHANNEL_ITEM_KEY)?.let { channelId ->
                ChannelPage(
                    channelId = channelId,
                    options = actions.enterEpisodeOptionsPage,
                    back = actions.upPress
                )
            }
        }
        composable(
            route = "${MainDestinations.EPISODE_DETAIL_ROUTE}/{$CHANNEL_ID_KEY}/{$EPISODE_DETAIL_ID_KEY}",
            arguments = listOf(
                navArgument(CHANNEL_ID_KEY) { type = NavType.LongType },
                navArgument(EPISODE_DETAIL_ID_KEY) { type = NavType.LongType })
        ) {
            // TODO : enter episode page
        }
        composable(route = MainDestinations.SEARCH_ROUTE) { SearchPage() }
        composable(
            route = "${MainDestinations.EPISODE_OPTIONS_ROUTE}/{$CHANNEL_ID_KEY}/{$EPISODE_DETAIL_ID_KEY}",
            arguments = listOf(
                navArgument(CHANNEL_ID_KEY) { type = NavType.LongType },
                navArgument(EPISODE_DETAIL_ID_KEY) { type = NavType.LongType })
        ) {
            // TODO : enter options page
        }
    }
}


class MainActions(navController: NavHostController) {
    val enterChannelPage: (Long) -> Unit = { channel: Long ->
        navController.navigate("${MainDestinations.CHANNEL_ROUTE}/$channel")
    }

    val enterEpisodePage: (Long, Long) -> Unit = { channelId: Long, episodeId: Long ->
        navController.navigate("${MainDestinations.EPISODE_DETAIL_ROUTE}/$channelId/$episodeId")
    }

    val enterEpisodeOptionsPage: (Long, Long) -> Unit = { channelId: Long, episodeId: Long ->
        navController.navigate("${MainDestinations.EPISODE_OPTIONS_ROUTE}/$channelId/$episodeId")
    }

    val enterSearchPage: () -> Unit = {
        navController.navigate(MainDestinations.SEARCH_ROUTE)
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
