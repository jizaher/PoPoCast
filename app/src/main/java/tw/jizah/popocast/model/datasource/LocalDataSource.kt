package tw.jizah.popocast.model.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import tw.jizah.popocast.model.ChannelItem

interface LocalDataSource {
    fun getChannels(): List<ChannelItem>
    fun updateChannels(list: List<ChannelItem>): Flow<List<ChannelItem>>
    fun setChannelFollowed(channelId: Long, isFollow: Boolean)
    fun addChannel(rssSource: String): Flow<ChannelItem>
}

class LocalDataSourceImpl: LocalDataSource {
    override fun getChannels(): List<ChannelItem> {
        // TODO: Get a list of channels from the local database.
        return emptyList()
    }

    override fun updateChannels(list: List<ChannelItem>): Flow<List<ChannelItem>> {
        // TODO: Update a list of channels for the local database.
        return emptyFlow()
    }

    override fun setChannelFollowed(channelId: Long, isFollow: Boolean) {
        // TODO: Mark a channel followed.
    }

    override fun addChannel(rssSource: String): Flow<ChannelItem> {
        // TODO: Add and parse a custom rss channel to the local database.
        return emptyFlow()
    }
}