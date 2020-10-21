package sanchez.sanchez.sergio.androidmobiletest.di.modules.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerApplication
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.AppRoomDatabase

@Module
class DatabaseModule {

    /**
     * Provide Local Database
     * @param context
     */
    @Provides
    @PerApplication
    fun provideDatabase(context: Context): AppRoomDatabase =
        Room.databaseBuilder(context, AppRoomDatabase::class.java,
            AppRoomDatabase.DATABASE_NAME
        ).build()

}