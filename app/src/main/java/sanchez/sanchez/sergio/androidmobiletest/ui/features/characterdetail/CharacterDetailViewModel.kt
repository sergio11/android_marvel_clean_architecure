package sanchez.sanchez.sergio.androidmobiletest.ui.features.characterdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sanchez.sanchez.sergio.androidmobiletest.domain.interact.FindCharacterDetailByIdInteract
import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharacterDetail
import javax.inject.Inject

/**
 * Character Detail View Model
 * @param findCharacterDetailByIdInteract
 */
class CharacterDetailViewModel @Inject constructor(
    private val findCharacterDetailByIdInteract: FindCharacterDetailByIdInteract
): ViewModel() {

    /**
     * Live Data - Definitions
     */

    private val _characterDetailState by lazy {
        MutableLiveData<CharacterDetailState>()
    }

    val characterDetailState: LiveData<CharacterDetailState> = _characterDetailState

    /**
     * Public Methods
     */

    /**
     * Load By Id
     * @param characterId
     */
    fun loadById(characterId: Long) = viewModelScope.launch {
        findCharacterDetailByIdInteract.execute(
            params = FindCharacterDetailByIdInteract.Params(characterId),
            onSuccess = fun(character) {
                _characterDetailState.postValue(
                    CharacterDetailState.OnSuccess(character)
                )
            },
            onError = fun(ex: Exception) {
                _characterDetailState.postValue(
                    CharacterDetailState.OnError(ex)
                )
            }
        )
    }

}

sealed class CharacterDetailState {

    /**
     * On Success
     * @param character
     */
    data class OnSuccess(val character: CharacterDetail): CharacterDetailState()

    /**
     * On Error
     * @param ex
     */
    data class OnError(val ex: Exception): CharacterDetailState()

}