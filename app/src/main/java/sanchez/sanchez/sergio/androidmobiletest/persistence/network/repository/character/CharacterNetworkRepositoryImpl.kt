package sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.character

import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharacterDetail
import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharactersPage
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.exception.NetworkNoResultException
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.mapper.CharacterDetailNetworkMapper
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.mapper.CharacterNetworkMapper
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.core.SupportNetworkRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.service.ICharactersService

/**
 * Character Network Repository Impl
 * @param charactersService
 * @param characterNetworkMapper
 */
open class CharacterNetworkRepositoryImpl(
    private val charactersService: ICharactersService,
    private val characterNetworkMapper: CharacterNetworkMapper,
    private val characterDetailNetworkMapper: CharacterDetailNetworkMapper
): SupportNetworkRepository(), ICharacterNetworkRepository {


    override suspend fun findPaginatedCharactersOrderByNameDesc(offset: Int, limit: Int): CharactersPage = safeNetworkCall {
        val dataResult = charactersService.getCharacterList(offset, limit).data
        if(dataResult.count == 0 || dataResult.results.isEmpty())
            throw NetworkNoResultException("Not characters found")
        characterNetworkMapper.dtoToModel(dataResult)
    }

    override suspend fun findCharacterById(characterId: Long): CharacterDetail = safeNetworkCall {
        val dataResult =  charactersService.getCharacterDetail(characterId).data
        if(dataResult.count == 0 || dataResult.results.isEmpty())
            throw NetworkNoResultException("Not characters found")
        characterDetailNetworkMapper.dtoToModel(dataResult.results.first())
    }

}