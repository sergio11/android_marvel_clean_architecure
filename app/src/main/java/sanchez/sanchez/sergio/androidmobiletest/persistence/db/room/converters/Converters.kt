package sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.converters

import androidx.room.TypeConverter
import java.util.*


/**
 * All Converters for save entities
 */
class Converters {

    /**
     * From Timestamp
     * @param value
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    /**
     * Date to Timestamp
     * @param date
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}