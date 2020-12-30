package tw.jizah.popocast.database.dao

import androidx.room.Dao
import androidx.room.Query
import tw.jizah.popocast.database.entity.TagEntity

@Dao
interface TagDao {
    @Query("select * from ${TagEntity.TABLE_NAME}")
    fun getAllTags(): List<TagEntity>
}