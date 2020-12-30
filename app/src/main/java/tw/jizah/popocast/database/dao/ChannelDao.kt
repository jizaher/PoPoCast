package tw.jizah.popocast.database.dao

import androidx.room.*
import tw.jizah.popocast.database.entity.ChannelEntity

@Dao
interface ChannelDao {
    @Query("select * from ${ChannelEntity.TABLE_NAME}")
    fun getChannels(): List<ChannelEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateChannels(list: List<ChannelEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addChannel(channel: ChannelEntity)
}