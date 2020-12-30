package tw.jizah.popocast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import tw.jizah.popocast.database.PoPoCastDataBase.Companion.DB_VERSION
import tw.jizah.popocast.database.dao.ChannelDao
import tw.jizah.popocast.database.dao.EpisodeDao
import tw.jizah.popocast.database.dao.TagDao
import tw.jizah.popocast.database.dao.TagMapDao
import tw.jizah.popocast.database.entity.ChannelEntity
import tw.jizah.popocast.database.entity.EpisodeEntity
import tw.jizah.popocast.database.entity.TagEntity
import tw.jizah.popocast.database.entity.TagMapEntity

@Database(
    entities = [
        ChannelEntity::class,
        EpisodeEntity::class,
        TagEntity::class,
        TagMapEntity::class
    ],
    version = DB_VERSION
)
abstract class PoPoCastDataBase : RoomDatabase() {
    abstract fun channelDao(): ChannelDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun tagDao(): TagDao
    abstract fun tagMapDao(): TagMapDao

    companion object {
        const val DB_VERSION = 1
        private const val DB_NAME = "popocast_database.db"

        @Volatile
        private var INSTANCE: PoPoCastDataBase? = null

        fun getInstance(context: Context): PoPoCastDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PoPoCastDataBase::class.java,
                        DB_NAME
                    )
                        .addMigrations(*getMigrations())
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private fun getMigrations(): Array<Migration> {
            return arrayOf()
        }
    }
}