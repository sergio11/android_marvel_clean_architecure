package sanchez.sanchez.sergio.androidmobiletest.domain.interact

import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharactersPage
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.character.ICharacterRepository

/**
 * Find paginated characters order by name desc
 */
class FindPaginatedCharactersOrderByNameDescInteract(
    private val characterRepository: ICharacterRepository
) {

    /**
     * Execute
     * @param onSuccess
     * @param onError
     */
    suspend fun execute(
        params: Params,
        onSuccess: (characters: CharactersPage) -> Unit,
        onError: (ex: Exception) -> Unit) = try {
        val characters = characterRepository.findPaginatedCharactersOrderByNameAsc(
            offset = params.offset,
            limit = params.limit
        )
        onSuccess(characters)
    } catch (ex: Exception) {
        onError(ex)
    }

    data class Params(
        val offset: Int,
        val limit: Int
    )
}