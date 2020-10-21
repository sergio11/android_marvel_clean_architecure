package sanchez.sanchez.sergio.androidmobiletest.persistence.api.character

import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharacterDetail
import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharactersPage
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.exception.RepoErrorException
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.exception.RepoNoResultException

/**
 * Character Repository
 */
interface ICharacterRepository {

    /**
     * Find Paginated Characters Order by name desc
     * @param offset
     * @param limit
     */
    @Throws(RepoNoResultException::class, RepoErrorException::class)
    suspend fun findPaginatedCharactersOrderByNameDesc(offset: Int, limit: Int): CharactersPage

    /**
     * Find Character By Id
     * @param characterId
     */
    @Throws(RepoNoResultException::class, RepoErrorException::class)
    suspend fun findCharacterById(characterId: Long): CharacterDetail

}