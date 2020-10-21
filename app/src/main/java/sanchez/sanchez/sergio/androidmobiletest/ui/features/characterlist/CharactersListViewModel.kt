package sanchez.sanchez.sergio.androidmobiletest.ui.features.characterlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sanchez.sanchez.sergio.androidmobiletest.domain.interact.FindPaginatedCharactersOrderByNameDescInteract
import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharactersPage
import sanchez.sanchez.sergio.androidmobiletest.persistence.api.exception.RepoNoResultException
import javax.inject.Inject

/**
 * Characters View Model
 */
class CharactersListViewModel @Inject constructor(
    private val findPaginatedCharactersOrderByNameDescInteract: FindPaginatedCharactersOrderByNameDescInteract
): ViewModel() {

    /**
     * Live Data - Definitions
     */

    private val _charactersState by lazy {
        MutableLiveData<CharactersState>()
    }

    val charactersState: LiveData<CharactersState> = _charactersState

    /**
     * Public Methods
     */

    fun loadNextPage() = viewModelScope.launch {
        // Get Offset for this request
        val offset = _charactersState.value?.let {
            if(it is CharactersState.OnSuccess)
                it.characterPage.offset + DEFAULT_PAGE_LIMIT
            else
                DEFAULT_START_OFFSET
        } ?: DEFAULT_START_OFFSET

        load(offset)
    }

    /**
     * Load Character List
     */
    fun load(offset: Int = 0) = viewModelScope.launch {
        findPaginatedCharactersOrderByNameDescInteract.execute(
            params = FindPaginatedCharactersOrderByNameDescInteract.Params(
                offset, DEFAULT_PAGE_LIMIT
            ),
            onSuccess = fun(characterPage) {
                _charactersState.postValue(
                    CharactersState.OnSuccess(characterPage)
                )
            },
            onError = fun(ex: Exception) {
                _charactersState.postValue(
                    if(ex is RepoNoResultException)
                        CharactersState.OnNotFound
                    else
                        CharactersState.OnError(ex))
            }
        )
    }

    companion object {

        private const val DEFAULT_PAGE_LIMIT = 20
        private const val DEFAULT_START_OFFSET = 0

    }

}

sealed class CharactersState {

    /**
     * On Success
     * @param characterPage
     */
    data class OnSuccess(val characterPage: CharactersPage): CharactersState()

    /**
     * On Error
     * @param ex
     */
    data class OnError(val ex: Exception): CharactersState()

    /**
     * On Not Found
     */
    object OnNotFound: CharactersState()

}