package sanchez.sanchez.sergio.androidmobiletest.di.component

import dagger.Subcomponent
import sanchez.sanchez.sergio.androidmobiletest.di.component.character.CharacterDetailFragmentComponent
import sanchez.sanchez.sergio.androidmobiletest.di.component.character.CharacterListFragmentComponent
import sanchez.sanchez.sergio.androidmobiletest.di.component.core.ActivityComponent
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ActivityModule
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerActivity
import sanchez.sanchez.sergio.androidmobiletest.ui.MainActivity


@PerActivity
@Subcomponent(modules = [
    ActivityModule::class
])
interface MainActivityComponent: ActivityComponent {

    fun inject(activity: MainActivity)

    fun charactersFragmentComponent(): CharacterListFragmentComponent

    fun characterDetailFragmentComponent(): CharacterDetailFragmentComponent

}