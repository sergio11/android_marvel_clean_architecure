package sanchez.sanchez.sergio.androidmobiletest.persistence.network.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.models.APIResponse
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.models.CharacterDTO

/**
 * Characters API
 * ==================
 * GET /v1/public/characters Fetches lists of characters.
 * GET /v1/public/characters/{characterId} Fetches a single character by id.
 */

interface ICharactersService {

    /**
     * Get Character List
     */
    @GET("characters")
    suspend fun getCharacterList( // Item offset (default 0)
        @Query("offset") offset: Int,
        // Item limit (default 100, maximum 500)
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String = "name"): APIResponse<CharacterDTO>

    /**
     * Get Character Detail
     * @param characterId
     */
    @GET("characters/{characterId}")
    suspend fun getCharacterDetail(@Path("characterId") characterId: Long): APIResponse<CharacterDTO>

}