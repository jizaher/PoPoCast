package tw.jizah.popocast.model.datasource

import tw.jizah.popocast.model.ChannelItem

interface RemoteDataSource {
    fun getPopularChannels(): List<ChannelItem>
}

class RemoteDataSourceImpl: RemoteDataSource {
    override fun getPopularChannels(): List<ChannelItem> {
        // TODO: Get popular channels from Firebase database.
        return emptyList()
    }
}