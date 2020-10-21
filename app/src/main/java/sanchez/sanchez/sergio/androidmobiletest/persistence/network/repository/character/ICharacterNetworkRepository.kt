package sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.character

import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharacterDetail
import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharactersPage
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.exception.NetworkException

interface ICharacterNetworkRepository {

    /**
     * Get Characters order by name desc
     * @param offset
     * @param limit
     */
    @Throws(NetworkException::class)
    suspend fun findPaginatedCharactersOrderByNameDesc(offset: Int, limit: Int): CharactersPage

    /**
     * Find Character By Id
     * @param characterId
     */
    @Throws(NetworkException::class)
    suspend fun findCharacterById(characterId: Long): CharacterDetail

}