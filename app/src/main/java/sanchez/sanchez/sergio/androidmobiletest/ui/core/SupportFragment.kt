package sanchez.sanchez.sergio.androidmobiletest.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Support Fragment
 */
abstract class SupportFragment<VM : ViewModel>(private val mViewModelClass: Class<VM>): Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var parentActivity: AppCompatActivity

    lateinit var viewModel: VM

    /**
     * on Create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        onInject()
        super.onCreate(savedInstanceState)
        viewModel = initViewModel()
        onObserverLiveData(viewModel)
    }

    /**
     * On Create View
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId(), container, false)
    }

    /**
     * Layout Id
     */
    @LayoutRes
    abstract fun layoutId(): Int

    /**
     * On Inject
     */
    abstract fun onInject()

    /**
     * Private Methods
     */

    /**
     * Get View Model
     */
    private fun initViewModel(): VM = ViewModelProvider(this, viewModelFactory).get(mViewModelClass)

    /**
     * On Observer Live Data
     * @param viewModel
     */
    protected open fun onObserverLiveData(viewModel: VM){}

}