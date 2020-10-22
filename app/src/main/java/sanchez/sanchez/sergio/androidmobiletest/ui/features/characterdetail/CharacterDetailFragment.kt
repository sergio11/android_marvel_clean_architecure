package sanchez.sanchez.sergio.androidmobiletest.ui.features.characterdetail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.character_detail.*
import sanchez.sanchez.sergio.androidmobiletest.R
import sanchez.sanchez.sergio.androidmobiletest.di.component.character.CharacterDetailFragmentComponent
import sanchez.sanchez.sergio.androidmobiletest.di.factory.DaggerComponentFactory
import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharacterDetail
import sanchez.sanchez.sergio.androidmobiletest.ui.core.SupportFragment
import sanchez.sanchez.sergio.androidmobiletest.ui.core.ext.loadFromCacheIfExists

/**
 * Character Detail Fragment
 */
class CharacterDetailFragment: SupportFragment<CharacterDetailViewModel>(CharacterDetailViewModel::class.java) {

    private val fragmentComponent: CharacterDetailFragmentComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerComponentFactory.getCharacterDetailFragmentComponent(requireActivity() as AppCompatActivity)
    }

    private val args by navArgs<CharacterDetailFragmentArgs>()

    override fun layoutId(): Int = R.layout.character_detail

    override fun onInject() {
        fragmentComponent.inject(this)
    }

    override fun onObserverLiveData(viewModel: CharacterDetailViewModel) {
        super.onObserverLiveData(viewModel)
        // Observe operation result
        viewModel.characterDetailState.observe(this, {
            when(it) {
                is CharacterDetailState.OnSuccess -> onLoadSuccessfully(it.character)
                is CharacterDetailState.OnError -> onErrorOccurred(it.ex)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        placeholderImageView.resume()
    }

    override fun onStop() {
        super.onStop()
        placeholderImageView.pause()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load Character Detail
        viewModel.loadById(args.characterId)
    }

    /**
     * Private Methods
     */

    /**
     * on Load Successfully
     * @param character
     */
    private fun onLoadSuccessfully(character: CharacterDetail) {
        characterNameTextView.text = character.name
        characterDescriptionTextView.text = character.description
        characterDetailTitleTextView.text = character.name
        characterThumbnailImageView.loadFromCacheIfExists(character.thumbnail)

    }

    /**
     * on Error Occurred
     * @param ex
     */
    private fun onErrorOccurred(ex: Exception) {

    }

}