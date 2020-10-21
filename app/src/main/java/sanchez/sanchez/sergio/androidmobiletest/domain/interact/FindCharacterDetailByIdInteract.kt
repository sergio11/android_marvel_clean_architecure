package sanchez.sanchez.sergio.androidmobiletest.domain.interact

import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharacterDetail
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.character.ICharacterRepository

/**
 * Find character detail by id use case
 */
class FindCharacterDetailByIdInteract(
    private val characterRepository: ICharacterRepository
) {

    /**
     * Execute
     * @param onSuccess
     * @param onError
     */
    suspend fun execute(
        params: Params,
        onSuccess: (character: CharacterDetail) -> Unit,
        onError: (ex: Exception) -> Unit) = try {
        val character = characterRepository.findCharacterById(params.characterId)
        onSuccess(character)
    } catch (ex: Exception) {
        onError(ex)
    }

    /**
     * Params
     */
    data class Params(
        val characterId: Long
    )
}