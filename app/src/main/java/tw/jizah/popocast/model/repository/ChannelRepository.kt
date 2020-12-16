package tw.jizah.popocast.model.repository

import kotlinx.coroutines.flow.Flow
import tw.jizah.popocast.model.ChannelItem
import tw.jizah.popocast.model.datasource.LocalDataSource
import tw.jizah.popocast.model.datasource.RemoteDataSource

interface ChannelRepository {
    fun getPopularChannels(): List<ChannelItem>
    fun getFollowedChannels(): List<ChannelItem>
    fun setChannelFollowed(channelId: Long, isFollowed: Boolean)
    fun addChannel(rssSource: String): Flow<ChannelItem>
}

class ChannelRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
): ChannelRepository {

    override fun getPopularChannels(): List<ChannelItem> {
        // TODO: Get data from the Firebase database and update them to the local database.
        return emptyList()
    }

    override fun getFollowedChannels(): List<ChannelItem> {
        // TODO: Get data from the local database.
        return emptyList()
    }

    override fun setChannelFollowed(channelId: Long, isFollowed: Boolean) {
        // TODO: Mark a channel followed.
        localDataSource.setChannelFollowed(channelId, isFollowed)
    }

    override fun addChannel(rssSource: String): Flow<ChannelItem> {
        // TODO: Add and parse a custom rss channel to the local database.
        return localDataSource.addChannel(rssSource)
    }
}