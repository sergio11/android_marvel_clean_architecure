package sanchez.sanchez.sergio.androidmobiletest.di.modules.characters.core

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.mapper.CharacterDetailNetworkMapper
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.mapper.CharacterNetworkMapper
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.character.CharacterNetworkRepositoryImpl
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.character.ICharacterNetworkRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.service.ICharactersService

/**
 * Character Network Module
 */
@Module
class CharacterNetworkModule {

    /**
     * Provide Character Network Service
     * @param retrofit
     */
    @Provides
    @PerFragment
    fun provideCharacterNetworkService(retrofit: Retrofit): ICharactersService =
        retrofit.create(ICharactersService::class.java)

    /**
     * Private Character Network Mapper
     */
    @Provides
    @PerFragment
    fun privateCharacterNetworkMapper(): CharacterNetworkMapper = CharacterNetworkMapper()

    /**
     * Private Character Detail Network Mapper
     */
    @Provides
    @PerFragment
    fun privateCharacterDetailNetworkMapper(): CharacterDetailNetworkMapper = CharacterDetailNetworkMapper()

    /**
     * Provide Character Network Repository
     * @param characterNetworkService
     * @param characterNetworkMapper
     *
     */
    @Provides
    @PerFragment
    fun provideCharacterNetworkRepository(
        characterNetworkService: ICharactersService,
        characterNetworkMapper: CharacterNetworkMapper,
        characterDetailNetworkMapper: CharacterDetailNetworkMapper
    ): ICharacterNetworkRepository =
        CharacterNetworkRepositoryImpl(characterNetworkService, characterNetworkMapper, characterDetailNetworkMapper)

}