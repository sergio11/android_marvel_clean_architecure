package sanchez.sanchez.sergio.androidmobiletest.di.factory

import androidx.appcompat.app.AppCompatActivity
import sanchez.sanchez.sergio.androidmobiletest.AndroidMobileTestApp
import sanchez.sanchez.sergio.androidmobiletest.di.component.MainActivityComponent
import sanchez.sanchez.sergio.androidmobiletest.di.component.ApplicationComponent
import sanchez.sanchez.sergio.androidmobiletest.di.component.DaggerApplicationComponent
import sanchez.sanchez.sergio.androidmobiletest.di.component.character.CharacterDetailFragmentComponent
import sanchez.sanchez.sergio.androidmobiletest.di.component.character.CharacterListFragmentComponent
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ActivityModule
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ApplicationModule

/**
 * Dagger Component Factory
 */
object DaggerComponentFactory {

    private var appComponent: ApplicationComponent? = null
    private var mainActivityComponent: MainActivityComponent? = null

    fun getAppComponent(app: AndroidMobileTestApp): ApplicationComponent =
        appComponent ?: DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(app))
            .build().also {
                appComponent = it
            }

    fun getMainActivityComponent(activity: AppCompatActivity): MainActivityComponent =
        mainActivityComponent ?: getAppComponent(activity.application as AndroidMobileTestApp)
            .mainActivityComponent(ActivityModule(activity)).also {
                mainActivityComponent = it
            }

    fun getCharactersFragmentComponent(activity: AppCompatActivity): CharacterListFragmentComponent =
        getMainActivityComponent(activity).charactersFragmentComponent()

    fun getCharacterDetailFragmentComponent(activity: AppCompatActivity): CharacterDetailFragmentComponent =
        getMainActivityComponent(activity).characterDetailFragmentComponent()

}