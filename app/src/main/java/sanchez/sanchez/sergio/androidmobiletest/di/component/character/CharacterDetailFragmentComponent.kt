package sanchez.sanchez.sergio.androidmobiletest.di.component.character

import dagger.Subcomponent
import sanchez.sanchez.sergio.androidmobiletest.di.component.core.FragmentComponent
import sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.detail.CharacterDetailDomainModule
import sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.detail.CharacterDetailUIModule
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment
import sanchez.sanchez.sergio.androidmobiletest.ui.features.characterdetail.CharacterDetailFragment

@PerFragment
@Subcomponent(modules = [ CharacterDetailUIModule::class, CharacterDetailDomainModule::class ])
interface CharacterDetailFragmentComponent: FragmentComponent {

    /**
     * Inject into Character Detail Fragment
     */
    fun inject(characterDetailFragment: CharacterDetailFragment)
}