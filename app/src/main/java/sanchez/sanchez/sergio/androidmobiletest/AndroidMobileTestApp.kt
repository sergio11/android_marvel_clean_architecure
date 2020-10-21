package sanchez.sanchez.sergio.androidmobiletest

import android.app.Application
import com.facebook.stetho.Stetho
import sanchez.sanchez.sergio.androidmobiletest.utils.IApplicationAware
import timber.log.Timber

class AndroidMobileTestApp: Application(), IApplicationAware {

    override fun getApiPublicKey(): String = BuildConfig.MARVEL_API_KEY

    override fun getApiPrivateKey(): String = BuildConfig.MARVEL_PRIVATE_API_KEY

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        if (BuildConfig.DEBUG) {
            onDebugConfig()
        } else {
            onReleaseConfig()
        }

    }

    /**
     * Private Methods
     */

    /**
     * On Debug Config
     */
    private fun onDebugConfig() {
        Stetho.initializeWithDefaults(this)
        Timber.plant(Timber.DebugTree())
    }

    /**
     * On Release Config
     */
    private fun onReleaseConfig(){}

    companion object {

        @JvmStatic
        lateinit var INSTANCE: AndroidMobileTestApp

    }
}