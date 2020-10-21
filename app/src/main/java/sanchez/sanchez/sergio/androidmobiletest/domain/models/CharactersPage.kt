package sanchez.sanchez.sergio.androidmobiletest.domain.models

/**
 * Character Page Model
 */
data class CharactersPage(
    val offset: Int,
    val limit: Int,
    val isFromCache: Boolean = false,
    val characterList: List<Character>
)