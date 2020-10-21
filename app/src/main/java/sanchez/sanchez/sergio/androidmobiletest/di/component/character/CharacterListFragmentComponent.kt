package sanchez.sanchez.sergio.androidmobiletest.di.component.character

import dagger.Subcomponent
import sanchez.sanchez.sergio.androidmobiletest.di.component.core.FragmentComponent
import sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.list.CharacterListDomainModule
import sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.list.CharacterListUIModule
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment
import sanchez.sanchez.sergio.androidmobiletest.ui.features.characterlist.CharacterListFragment

@PerFragment
@Subcomponent(modules = [ CharacterListUIModule::class, CharacterListDomainModule::class ])
interface CharacterListFragmentComponent: FragmentComponent {

    /**
     * Inject into Character Fragment
     */
    fun inject(characterListFragment: CharacterListFragment)
}