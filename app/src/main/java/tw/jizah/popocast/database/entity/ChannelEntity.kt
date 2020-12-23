package tw.jizah.popocast.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import tw.jizah.popocast.database.entity.ChannelEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class ChannelEntity(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: Long,
    @ColumnInfo(name = IMAGE_URL)
    val imageUrl: String,
    @ColumnInfo(name = TITLE)
    val title: String,
    @ColumnInfo(name = SUBTITLE)
    val subtitle: String,
    @ColumnInfo(name = DESCRIPTION)
    val description: String,
    @ColumnInfo(name = IS_FOLLOWED)
    val isFollowed: Boolean,
) {
    companion object {
        const val TABLE_NAME = "PodcastChannel"

        const val ID = "id"
        const val TITLE = "title"
        const val IMAGE_URL = "imageUrl"
        const val SUBTITLE = "subtitle"
        const val DESCRIPTION = "description"
        const val IS_FOLLOWED = "is_followed"
    }
}