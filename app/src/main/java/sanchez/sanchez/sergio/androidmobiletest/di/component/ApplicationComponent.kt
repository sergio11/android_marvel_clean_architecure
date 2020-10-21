package sanchez.sanchez.sergio.androidmobiletest.di.component

import android.content.Context
import dagger.Component
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ActivityModule
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ApplicationModule
import sanchez.sanchez.sergio.androidmobiletest.di.modules.database.DatabaseModule
import sanchez.sanchez.sergio.androidmobiletest.di.modules.network.NetworkModule
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerApplication


/**
 * A component whose lifetime is the life of the application.
 */
@PerApplication
@Component(modules = [
    ApplicationModule::class,
    DatabaseModule::class,
    NetworkModule::class
])
interface ApplicationComponent {

    //Exposed to sub-graphs.
    fun context(): Context

    fun mainActivityComponent(activityModule: ActivityModule): MainActivityComponent
}