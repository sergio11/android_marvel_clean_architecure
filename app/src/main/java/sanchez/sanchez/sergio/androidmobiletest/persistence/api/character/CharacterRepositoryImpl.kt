package sanchez.sanchez.sergio.androidmobiletest.persistence.api.character

import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharacterDetail
import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharactersPage
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.exception.RepoErrorException
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.exception.RepoNoResultException
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.character.ICharacterDBRepository
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.exception.DBNoResultException
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.exception.NetworkException
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.exception.NetworkNoResultException
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.repository.character.ICharacterNetworkRepository

/**
 * Character Repository Implementation
 * @param characterNetworkRepository
 * @param characterDBRepository
 */
class CharacterRepositoryImpl (
    private val characterNetworkRepository: ICharacterNetworkRepository,
    private val characterDBRepository: ICharacterDBRepository
): ICharacterRepository {

    /**
     * Find Paginated Characters Order By Name Des
     * @param offset
     * @param limit
     */
    override suspend fun findPaginatedCharactersOrderByNameAsc(offset: Int, limit: Int): CharactersPage =
        try {
            characterNetworkRepository.findPaginatedCharactersOrderByNameDesc(offset, limit).also {
                // Cache this data for offline access
                characterDBRepository.saveCharacters(it.characterList)
            }
        }
        catch (ex: NetworkNoResultException) {

            /**
             * This answer has not had results,
             * we delete all the cached information that belongs to previous answers
             */
            try {
                characterDBRepository.deleteAll()
            } catch (ex: Exception) {
                throw RepoErrorException(ex)
            }

            throw RepoNoResultException(ex)
        }
        catch(ex: NetworkException) {

            // Other Network errors (500, 403 ....)
            // Take Characters from last response
            try {
                val characterList = characterDBRepository.findCharactersOrderByNameAsc()
                CharactersPage(
                    isFromCache = true,
                    offset = 0,
                    limit = characterList.size,
                    characterList = characterList
                )
            } catch(ex: DBNoResultException) {
                throw RepoNoResultException(ex)
            } catch (ex: Exception) {
                throw RepoErrorException(ex)
            }
        } catch (ex: Exception) {
            throw RepoErrorException(ex)
        }


    /**
     * Find Character By Id
     * @param characterId
     */
    override suspend fun findCharacterById(characterId: Long): CharacterDetail =
        characterNetworkRepository.findCharacterById(characterId)
}