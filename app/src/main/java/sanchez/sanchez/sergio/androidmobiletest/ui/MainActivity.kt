package sanchez.sanchez.sergio.androidmobiletest.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import sanchez.sanchez.sergio.androidmobiletest.R
import sanchez.sanchez.sergio.androidmobiletest.di.component.MainActivityComponent
import sanchez.sanchez.sergio.androidmobiletest.di.factory.DaggerComponentFactory


class MainActivity: AppCompatActivity() {

    private val activityComponent: MainActivityComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerComponentFactory.getMainActivityComponent(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

}