package sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.core

import dagger.Module
import dagger.Provides
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.character.CharacterRepositoryImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.character.ICharacterRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.character.ICharacterDBRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.character.ICharacterNetworkRepository

/**
 * Character Repository Module
 */
@Module(includes = [CharacterDBModule::class, CharacterNetworkModule::class])
class CharacterRepositoryModule {

    /**
     * Provide Character Repository
     * @param characterNetworkRepository
     * @param characterDBRepository
     */
    @Provides
    @PerFragment
    fun provideCharacterRepository(
        characterNetworkRepository: ICharacterNetworkRepository,
        characterDBRepository: ICharacterDBRepository
    ): ICharacterRepository =
        CharacterRepositoryImpl(characterNetworkRepository, characterDBRepository)
}