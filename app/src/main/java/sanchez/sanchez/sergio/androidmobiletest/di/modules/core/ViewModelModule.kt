package sanchez.sanchez.sergio.androidmobiletest.di.modules.core

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.viewmodel.ViewModelFactory

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}