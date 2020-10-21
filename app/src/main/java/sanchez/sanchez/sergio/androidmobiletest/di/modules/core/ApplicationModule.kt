package sanchez.sanchez.sergio.androidmobiletest.di.modules.core

import android.content.Context
import dagger.Module
import dagger.Provides
import sanchez.sanchez.sergio.androidmobiletest.AndroidMobileTestApp
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerApplication
import sanchez.sanchez.sergio.androidmobiletest.utils.IApplicationAware

/**
 * Application Module
 */
@Module
class ApplicationModule constructor(private val application: AndroidMobileTestApp) {

    /**
     * Provide Application Context
     * @return
     */
    @Provides
    @PerApplication
    fun provideApplicationContext(): Context = application

    @Provides
    @PerApplication
    fun provideApplicationAware(): IApplicationAware = application
}