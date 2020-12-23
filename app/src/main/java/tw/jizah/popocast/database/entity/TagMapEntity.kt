package tw.jizah.popocast.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tw.jizah.popocast.database.entity.TagMapEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class TagMapEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long,
    @ColumnInfo(name = CID)
    val channelId: Long,
    @ColumnInfo(name = TAG_ID)
    val tagId: Int,
) {
    companion object {
        const val TABLE_NAME = "PodcastChannelTag"

        const val ID = "id"
        const val CID = "channel_id"
        const val TAG_ID = "tag_id"
    }
}