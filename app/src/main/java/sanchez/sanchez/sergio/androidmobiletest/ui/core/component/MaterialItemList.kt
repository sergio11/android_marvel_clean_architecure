package sanchez.sanchez.sergio.androidmobiletest.ui.core.component

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.material_list_item_layout.view.*
import sanchez.sanchez.sergio.androidmobiletest.R

/**
 * Material List Item
 */
class MaterialListItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    /**
     * Label Text
     */
    var labelText: String? = null
        set(value) {
            field = value
            labelTextView.text = value
        }


    /**
     * Help Text
     */
    var helpText: String? = null
        set(value) {
            field = value
            helpTextView.text = value
        }


    /**
     * Value Text
     */
    var valueText: String? = null
        set(value) {
            field = value
            valueTextView.text = value
        }
        get() = valueTextView.text.toString()

    /**
     * Value Text Color
     */
    @ColorRes
    var valueTextColor: Int? = null
        set(value) {
            field = value
            field?.let {
                valueTextView.setTextColor(ContextCompat.getColor(context, it))
            }
        }

    init {
        LayoutInflater.from(context)?.inflate(R.layout.material_list_item_layout, this, true)
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.MaterialListItem, defStyleAttr, 0
        ).also {
            setAttributes(it)
            it.recycle()
        }
    }


    /**
     * Set Attributes
     * @param attrs
     */
    private fun setAttributes(attrs: TypedArray) {
        //labelText
        val labelResId = attrs.getResourceId(R.styleable.MaterialListItem_labelText, DEFAULT_NO_RESOURCE_ID)
        if(labelResId != DEFAULT_NO_RESOURCE_ID)
            labelText = context.getString(labelResId)
        else
            labelTextView.visibility = View.GONE

        val valueResId = attrs.getResourceId(R.styleable.MaterialListItem_valueText, DEFAULT_NO_RESOURCE_ID)
        if(valueResId != DEFAULT_NO_RESOURCE_ID)
            valueText = context.getString(valueResId)

        //helpText
        val helperResId = attrs.getResourceId(R.styleable.MaterialListItem_helpText, DEFAULT_NO_RESOURCE_ID)
        if(helperResId != DEFAULT_NO_RESOURCE_ID)
            helpText = context.getString(helperResId)
        else
            helpTextView.visibility = View.GONE

    }

    companion object {
        const val DEFAULT_NO_RESOURCE_ID = -1
    }
}