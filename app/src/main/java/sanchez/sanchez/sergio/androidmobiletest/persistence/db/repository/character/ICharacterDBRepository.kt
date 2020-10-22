package sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.character

import sanchez.sanchez.sergio.androidmobiletest.domain.models.Character
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.exception.RepoErrorException
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.exception.RepoNoResultException

/**
 * Character DB Repository
 */
interface ICharacterDBRepository {

    @Throws(RepoNoResultException::class, RepoErrorException::class)
    suspend fun findCharactersOrderByNameAsc(): List<Character>

    @Throws(RepoNoResultException::class, RepoErrorException::class)
    suspend fun saveCharacters(characterList: List<Character>)

    @Throws(RepoErrorException::class)
    suspend fun deleteAll()

}