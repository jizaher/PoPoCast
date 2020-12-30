package tw.jizah.popocast.database.dao

import androidx.room.Dao
import androidx.room.Query
import tw.jizah.popocast.database.entity.TagMapEntity

@Dao
interface TagMapDao {
    @Query("select * from ${TagMapEntity.TABLE_NAME} where ${TagMapEntity.CID}=:channelId")
    fun getTadIds(channelId: Long): List<TagMapEntity>
}