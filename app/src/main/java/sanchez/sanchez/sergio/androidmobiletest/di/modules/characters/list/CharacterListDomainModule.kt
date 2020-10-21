package sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.list

import dagger.Module
import dagger.Provides
import sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.core.CharacterRepositoryModule
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment
import sanchez.sanchez.sergio.androidmobiletest.domain.interact.FindPaginatedCharactersOrderByNameDescInteract
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.character.ICharacterRepository

@Module(includes = [CharacterRepositoryModule::class])
class CharacterListDomainModule {

    @Provides
    @PerFragment
    fun provideFindPaginatedCharactersOrderByNameDescInteract(
        characterRepository: ICharacterRepository
    ): FindPaginatedCharactersOrderByNameDescInteract = FindPaginatedCharactersOrderByNameDescInteract(characterRepository)
}