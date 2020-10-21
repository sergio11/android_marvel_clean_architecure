package sanchez.sanchez.sergio.androidmobiletest.di.component.core

import dagger.Component
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ViewModelModule
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerFragment

@PerFragment
@Component(
    dependencies = [ ActivityComponent::class ],
    modules = [ViewModelModule::class])
interface FragmentComponent {

}