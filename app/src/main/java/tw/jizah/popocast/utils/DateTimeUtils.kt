package tw.jizah.popocast.utils

import android.content.Context
import tw.jizah.popocast.R
import java.time.*
import java.time.format.TextStyle
import java.util.*

object DateTimeUtils {

    fun getDateString(context: Context, millisecond: Long = Instant.now().toEpochMilli()): String {
        val releaseLocalDate = millisecond.getLocalDate()
        val todayLocalDate = Instant.now().toEpochMilli().getLocalDate()
        val yesterdayLocalDate = todayLocalDate.minusDays(1)
        val beforeOneWeekMilliSeconds = todayLocalDate.minusWeeks(1).getMilliSecond()

        return when {
            beforeOneWeekMilliSeconds > millisecond -> {
                val month = releaseLocalDate.month.getDisplayName(
                    TextStyle.SHORT_STANDALONE,
                    Locale.getDefault()
                )
                val isThisYear = (todayLocalDate.year == releaseLocalDate.year)
                if (isThisYear) {
                    "$month ${releaseLocalDate.dayOfMonth}"
                } else {
                    "$month ${releaseLocalDate.dayOfMonth}, ${releaseLocalDate.year}"
                }
            }
            yesterdayLocalDate.getMilliSecond() > millisecond -> {
                releaseLocalDate.dayOfWeek.getDisplayName(
                    TextStyle.SHORT_STANDALONE,
                    Locale.getDefault()
                )
            }
            releaseLocalDate == yesterdayLocalDate -> {
                context.getString(R.string.yesterday)
            }
            else -> {
                context.getString(R.string.today)
            }
        }
    }


    private fun Long.getLocalDate(): LocalDate {
        return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
    }

    private fun LocalDate.getMilliSecond(): Long {
        return atStartOfDay().getEpochMilliSecond()
    }

    fun LocalDateTime.getEpochMilliSecond(): Long =
        atZone(ZoneId.systemDefault()).toInstant().epochSecond * 1000L
}