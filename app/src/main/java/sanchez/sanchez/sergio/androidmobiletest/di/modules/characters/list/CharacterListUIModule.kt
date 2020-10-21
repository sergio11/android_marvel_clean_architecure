package sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.list

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ViewModelModule
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.viewmodel.ViewModelKey
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment
import sanchez.sanchez.sergio.androidmobiletest.ui.features.characterlist.CharactersListViewModel

@Module(includes = [ ViewModelModule::class ])
abstract class CharacterListUIModule {

    @PerFragment
    @Binds
    @IntoMap
    @ViewModelKey(CharactersListViewModel::class)
    abstract fun bindsCharactersViewModel(charactersListViewModel: CharactersListViewModel): ViewModel
}