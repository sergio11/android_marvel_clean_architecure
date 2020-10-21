package sanchez.sanchez.sergio.androidmobiletest.persistence.network.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.ext.isNetworkAvailable
import java.io.IOException

/**
 * Connectivity Interceptor
 */
class ConnectivityInterceptor constructor(private val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable(context)) {
            throw IOException()
        }
        val request = chain.request()
        return chain.proceed(request)
    }
}