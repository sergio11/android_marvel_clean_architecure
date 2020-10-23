package sanchez.sanchez.sergio.androidmobiletest.ui.features.characterdetail

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.character_detail.*
import sanchez.sanchez.sergio.androidmobiletest.R
import sanchez.sanchez.sergio.androidmobiletest.di.component.character.CharacterDetailFragmentComponent
import sanchez.sanchez.sergio.androidmobiletest.di.factory.DaggerComponentFactory
import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharacterDetail
import sanchez.sanchez.sergio.androidmobiletest.domain.models.ComicsItem
import sanchez.sanchez.sergio.androidmobiletest.ui.core.SupportFragment
import sanchez.sanchez.sergio.androidmobiletest.ui.core.ext.loadFromCacheIfExists
import sanchez.sanchez.sergio.androidmobiletest.ui.core.ext.popBackStack
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

/**
 * Character Detail Fragment
 */
class CharacterDetailFragment: SupportFragment<CharacterDetailViewModel>(CharacterDetailViewModel::class.java) {

    private val fragmentComponent: CharacterDetailFragmentComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerComponentFactory.getCharacterDetailFragmentComponent(requireActivity() as AppCompatActivity)
    }

    private val modifiedAtDateFormat by lazy {
        SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault())
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
        if(character.description.isNotEmpty())
            characterDescriptionListItem.valueText = character.description
        characterDetailTitleTextView.text = character.name
        character.modified?.let {
            characterModifiedAtItem.valueText = modifiedAtDateFormat.format(it)
        }

        characterComicItem.apply {
            valueText = String.format(Locale.getDefault(),
                getString(R.string.character_comic_value), character.comics.available)
            if(character.comics.items.isNotEmpty())
                addAction {
                    showDetailDialog(
                        titleRes = R.string.character_comic_dialog_detail_title,
                        items = character.comics.items
                    )
                }
        }

        characterSeriesItem.apply {
            valueText = String.format(Locale.getDefault(),
                getString(R.string.character_series_value), character.series.available)
            if(character.series.items.isNotEmpty())
                addAction {
                    showDetailDialog(
                        titleRes = R.string.character_series_dialog_detail_title,
                        items = character.series.items
                    )
                }
        }

        characterEventsItem.apply {
            valueText = String.format(Locale.getDefault(),
                getString(R.string.character_events_value), character.events.available)
            if(character.events.items.isNotEmpty())
                addAction {
                    showDetailDialog(
                        titleRes = R.string.character_events_dialog_detail_title,
                        items = character.events.items
                    )
                }
        }

        characterThumbnailImageView.loadFromCacheIfExists(character.thumbnail)

    }

    /**
     * on Error Occurred
     * @param ex
     */
    private fun onErrorOccurred(ex: Exception) {
        Timber.d("OnErrorOccurred -> ${ex.message}")
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(R.string.character_detail_error))
            .setPositiveButton(resources.getString(R.string.character_detail_error_accept_button)) { dialog, which ->
                popBackStack(R.id.characterListFragment)
            }
            .show()
    }

    /**
     * Show Detail Dialog
     * @param titleRes
     * @param items
     */
    private fun showDetailDialog(@StringRes titleRes: Int, items: List<ComicsItem>) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(titleRes)
            .setItems(items.map { it.name }.toTypedArray(), null)
            .show()
    }


}