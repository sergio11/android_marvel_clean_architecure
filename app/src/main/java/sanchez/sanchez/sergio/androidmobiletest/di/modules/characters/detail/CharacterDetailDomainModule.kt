package sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.detail

import dagger.Module
import dagger.Provides
import sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.core.CharacterRepositoryModule
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment
import sanchez.sanchez.sergio.androidmobiletest.domain.interact.FindCharacterDetailByIdInteract
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.character.ICharacterRepository

@Module(includes = [CharacterRepositoryModule::class])
class CharacterDetailDomainModule {

    @Provides
    @PerFragment
    fun provideFindCharacterByIdInteract(
        characterRepository: ICharacterRepository
    ): FindCharacterDetailByIdInteract = FindCharacterDetailByIdInteract(characterRepository)

}