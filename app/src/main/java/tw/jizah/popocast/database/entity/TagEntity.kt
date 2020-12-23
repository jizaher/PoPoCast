package tw.jizah.popocast.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tw.jizah.popocast.database.entity.TagEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class TagEntity (
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: Long,
    @ColumnInfo(name = NAME)
    val name: String
)
{
    companion object {
        const val TABLE_NAME = "PodcastTag"
        const val ID = "id"
        const val NAME = "name"
    }
}