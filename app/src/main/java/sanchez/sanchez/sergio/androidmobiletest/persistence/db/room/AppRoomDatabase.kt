package sanchez.sanchez.sergio.androidmobiletest.persistence.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.converters.Converters
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.dao.character.CharacterDAOImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.entity.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppRoomDatabase : RoomDatabase() {

    /**
     * DAOs declarations
     */
    abstract fun characterDAO(): CharacterDAOImpl

    companion object {

        const val DATABASE_NAME = "APP_DATABASE"
    }
}