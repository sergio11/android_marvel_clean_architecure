package sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.character

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sanchez.sanchez.sergio.androidmobiletest.domain.models.Character
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.exception.DBErrorException
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.repository.exception.DBNoResultException
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.dao.character.ICharacterDAO
import sanchez.sanchez.sergio.androidmobiletest.persistence.db.room.mapper.CharacterDBMapper

/**
 * Character DB Repository
 */
open class CharacterDBRepositoryImpl (
    private val characterDAO: ICharacterDAO,
    private val characterDBMapper: CharacterDBMapper
): ICharacterDBRepository {

    override suspend fun findCharactersOrderByNameAsc(): List<Character> = withContext(Dispatchers.IO) {
        try {
            val characters = characterDAO.findAllOrderByNameAsc()
            // If not transactions found, throw exception
            if(characters.isEmpty())
                throw DBNoResultException("No characters have been found")
            characterDBMapper.entityToModel(characters)
        } catch (ex: DBNoResultException) {
            throw ex
        }catch (ex: Exception) {
            throw DBErrorException("Error occurred when query characters", ex)
        }
    }

    override suspend fun saveCharacters(characterList: List<Character>): Unit = withContext(Dispatchers.IO) {
        try {
            // First, remove old data
            characterDAO.deleteAll()
            // Insert new transactions data
            characterDAO.insert(characterDBMapper.modelToEntity(characterList))
        } catch (ex: Exception) {
            throw DBErrorException("Error occurred when Save transactions", ex)
        }
    }

    override suspend fun deleteAll() = withContext(Dispatchers.IO)  {
        try {
            characterDAO.deleteAll()
        } catch (ex: Exception) {
            throw DBErrorException("Error occurred when Delete characters", ex)
        }
    }

}