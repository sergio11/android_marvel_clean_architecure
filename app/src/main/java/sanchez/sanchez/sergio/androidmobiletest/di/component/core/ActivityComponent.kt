package sanchez.sanchez.sergio.androidmobiletest.di.component.core

import dagger.Component
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import sanchez.sanchez.sergio.androidmobiletest.di.component.ApplicationComponent
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ActivityModule
import sanchez.sanchez.sergio.androidmobiletest.di.modules.core.ViewModelModule
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerActivity

/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 *
 */
@PerActivity
@Component(
    dependencies = [ ApplicationComponent::class ],
            modules = [ ActivityModule::class, ViewModelModule::class ] )
interface ActivityComponent {

    //Exposed to sub-graphs.
    fun context(): Context
    fun activity(): AppCompatActivity
}