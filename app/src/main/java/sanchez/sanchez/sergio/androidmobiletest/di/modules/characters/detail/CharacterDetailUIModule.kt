package sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.detail

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ViewModelModule
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.viewmodel.ViewModelKey
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment
import sanchez.sanchez.sergio.androidmobiletest.ui.features.characterdetail.CharacterDetailViewModel

@Module(includes = [ ViewModelModule::class ])
abstract class CharacterDetailUIModule {

    @PerFragment
    @Binds
    @IntoMap
    @ViewModelKey(CharacterDetailViewModel::class)
    abstract fun bindsCharacterDetailViewModel(characterDetailViewModel: CharacterDetailViewModel): ViewModel
}