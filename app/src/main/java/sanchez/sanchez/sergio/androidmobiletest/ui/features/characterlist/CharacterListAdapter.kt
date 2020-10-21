package sanchez.sanchez.sergio.androidmobiletest.ui.features.characterlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import sanchez.sanchez.sergio.androidmobiletest.R
import sanchez.sanchez.sergio.androidmobiletest.domain.models.Character
import sanchez.sanchez.sergio.androidmobiletest.ui.core.ext.loadFromCacheIfExists
import timber.log.Timber

/**
 * Character List Adapter
 */
class CharacterListAdapter(
    private val context: Context,
    private val data: MutableList<Character>,
    private val characterItemListener: OnCharacterClickListener,
    private val paginationCallBack: IPaginationCallBack? = null
): RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>() {


    /**
     * Create View Holder
     * @param parent
     * @param viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder =
        CharacterViewHolder(inflateLayout(R.layout.character_item_list, parent))

    /**
     * On Bind Model to View Holder
     * @param holder
     * @param position
     */
    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holder.bind(it)
        }
        paginationCallBack?.let { callback ->
            if(callback.isPaginationEnabled()) {
               if(position+1 == data.size) {
                   Timber.d("PAGINATION -> position: %d, total size: %d", position, data.size)
                   callback.onLoadNextPage()
               }
            }
        }
    }

    override fun getItemCount(): Int = data.size

    /**
     * Update Adapter Data
     * @param newData
     */
    fun addData(newData: List<Character>) {
        data.apply {
            addAll(newData)
        }
        notifyDataSetChanged()
    }

    /**
     * Clear Data
     */
    fun clearData() {
        data.clear()
        notifyDataSetChanged()
    }

    /**
     * Replace Data
     * @param newData
     */
    fun replaceData(newData: List<Character>) {
        data.clear()
        addData(newData)
    }

    /**
     * Private Methods
     */

    /**
     * Inflate Layout
     */
    private fun inflateLayout(@LayoutRes layoutRest: Int, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(layoutRest, parent, false)
    }

    interface OnCharacterClickListener {
        fun onCharacterClicked(character: Character)
    }

    /**
     * Character View Holder
     * @param itemView
     */
    inner class CharacterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(character: Character) {
            itemView.apply {
                setOnClickListener { characterItemListener.onCharacterClicked(character) }
                findViewById<ImageView>(R.id.characterThumbnail).loadFromCacheIfExists(character.thumbnail)
                findViewById<TextView>(R.id.characterNameTextView).text = character.name
                findViewById<TextView>(R.id.comicsCountTextView).text = String.format(context.getString(R.string.character_item_comics), character.comics)
                findViewById<TextView>(R.id.seriesCountTextView).text = String.format(context.getString(R.string.character_item_series), character.series)
                findViewById<TextView>(R.id.eventsCountTextView).text = String.format(context.getString(R.string.character_item_events), character.events)
            }
        }
    }

}