package tw.jizah.popocast.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import tw.jizah.popocast.database.entity.EpisodeEntity.Companion.CID
import tw.jizah.popocast.database.entity.EpisodeEntity.Companion.ID
import tw.jizah.popocast.database.entity.EpisodeEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME, primaryKeys = [CID, ID])
class EpisodeEntity (
    @ColumnInfo(name = CID)
    val channelId: Long,
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
    @ColumnInfo(name = RELEASE_TIME)
    val releaseTime: Long,
    @ColumnInfo(name = DURATION)
    val duration: Long,
) {
    companion object {
        const val TABLE_NAME = "PodcastEpisode"

        const val CID = "channel_id"
        const val ID = "id"
        const val TITLE = "title"
        const val IMAGE_URL = "image_url"
        const val SUBTITLE = "subtitle"
        const val DESCRIPTION = "description"
        const val RELEASE_TIME = "release_time"
        const val DURATION = "duration"
    }
}