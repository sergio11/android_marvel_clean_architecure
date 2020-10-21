package sanchez.sanchez.sergio.androidmobiletest.ui.features.characterlist

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.characters_fragment.*
import kotlinx.android.synthetic.main.character_fragment_content_layout.view.*
import sanchez.sanchez.sergio.androidmobiletest.R
import sanchez.sanchez.sergio.androidmobiletest.di.component.character.CharacterListFragmentComponent
import sanchez.sanchez.sergio.androidmobiletest.di.factory.DaggerComponentFactory
import sanchez.sanchez.sergio.androidmobiletest.domain.models.Character
import sanchez.sanchez.sergio.androidmobiletest.domain.models.CharactersPage
import sanchez.sanchez.sergio.androidmobiletest.ui.core.SupportFragment
import sanchez.sanchez.sergio.androidmobiletest.ui.core.ext.navigate
import timber.log.Timber
import java.lang.Exception

/**
 * Character List Fragment
 */
class CharacterListFragment: SupportFragment<CharactersListViewModel>(CharactersListViewModel::class.java),
    IPaginationCallBack, CharacterListAdapter.OnCharacterClickListener {

    private val fragmentComponent: CharacterListFragmentComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerComponentFactory.getCharactersFragmentComponent(requireActivity() as AppCompatActivity)
    }

    lateinit var recyclerViewAdapter: CharacterListAdapter

    override fun layoutId(): Int = R.layout.characters_fragment

    override fun onInject() {
        fragmentComponent.inject(this)
    }

    override fun onObserverLiveData(viewModel: CharactersListViewModel) {
        super.onObserverLiveData(viewModel)
        // Observe operation result
        viewModel.charactersState.observe(this, {
            when(it) {
                is CharactersState.OnSuccess -> onCharactersLoaded(it.characterPage)
                is CharactersState.OnError -> onErrorOccurred(it.ex)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated CALLED")
        // Recycler View Adapter
        recyclerViewAdapter = CharacterListAdapter(requireContext(), mutableListOf(), this, this)
        // Configure Recycler View
        contentView.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.bottom = resources.getDimension(R.dimen.item_vertical_separation).toInt()
                }
            })
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            adapter = recyclerViewAdapter
        }

        swipeRefreshLayout.setOnRefreshListener { loadCharacters() }

        viewModel.charactersState.value?.let {
            if(it is CharactersState.OnSuccess)
                onCharactersLoaded(it.characterPage)
            else
                loadCharacters()
        } ?: loadCharacters()
    }

    /**
     * Private Methods
     */

    /**
     * On Characters Loaded
     * @param charactersPage
     */
    private fun onCharactersLoaded(charactersPage: CharactersPage) {
        Timber.d("onCharactersLoaded CALLED, characters -> %d", charactersPage.characterList.size)
        loadingView.visibility = View.GONE
        errorView.visibility = View.GONE
        contentView.visibility = View.VISIBLE
        swipeRefreshLayout.isRefreshing = false

        if(charactersPage.isFromCache) {
            // Data from cache, If there is no character in the list, the cached information is displayed
            if(recyclerViewAdapter.itemCount == 0)
                recyclerViewAdapter.replaceData(charactersPage.characterList)
            Snackbar.make(requireView(), getString(R.string.response_from_cache), Snackbar.LENGTH_LONG).show()
        } else {
            val currentSize = recyclerViewAdapter.itemCount
            recyclerViewAdapter.addData(charactersPage.characterList)
            contentView.recyclerView.scrollToPosition(currentSize)
        }
    }

    /**
     * On Error Occurred
     * @param ex
     */
    private fun onErrorOccurred(ex: Exception) {
        Timber.d("onErrorOccurred CALLED, message -> %s", ex.message)
        swipeRefreshLayout.isRefreshing = false
        loadingView.visibility = View.GONE
        errorView.visibility = View.VISIBLE
        contentView.visibility = View.GONE
        Snackbar.make(requireView(), getString(R.string.error_occurred_message), Snackbar.LENGTH_LONG).show()
    }

    /**
     * Load Characters
     */
    private fun loadCharacters() {
        loadingView.visibility = View.VISIBLE
        errorView.visibility = View.GONE
        contentView.visibility= View.GONE
        recyclerViewAdapter.clearData()
        viewModel.load()
    }

    /**
     * On Load Next Page
     */
    override fun onLoadNextPage() {
        swipeRefreshLayout.isRefreshing = true
        viewModel.loadNextPage()
    }

    /**
     * Is Pagination Enabled
     */
    override fun isPaginationEnabled(): Boolean = viewModel.charactersState.value?.let {
        it !is CharactersState.OnNotFound
    } ?: true

    /**
     * On Character Clicked
     * @param character
     */
    override fun onCharacterClicked(character: Character) {
        Timber.d("onCharacterClicked CALLED, name -> %s", character.name)
        navigate(CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(characterId = character.id))
    }
}