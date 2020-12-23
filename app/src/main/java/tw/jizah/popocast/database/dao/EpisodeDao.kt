package tw.jizah.popocast.database.dao

import androidx.room.Dao
import androidx.room.Query
import tw.jizah.popocast.database.entity.EpisodeEntity

@Dao
interface EpisodeDao {
    @Query("select * from ${EpisodeEntity.TABLE_NAME} where ${EpisodeEntity.CID}=:channelId")
    fun getEpisodes(channelId: Long): List<EpisodeEntity>

    @Query("select * from ${EpisodeEntity.TABLE_NAME} where ${EpisodeEntity.CID}=:channelId and ${EpisodeEntity.ID}=:episodeId")
    fun getEpisode(channelId: Long, episodeId: Long): EpisodeEntity
}