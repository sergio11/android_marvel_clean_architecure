package sanchez.sanchez.sergio.androidmobiletest.persistence.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.utils.IAuthHashStrategy
import sanchez.sanchez.sergio.androidmobiletest.utils.IApplicationAware

/**
 * Auth Interceptor
 * Add Query Param "apikey" that allow us to authenticate against Marvel Back-End
 * Add Query Param "hash" with the following configuration ts + private key + public key
 */
class AuthInterceptor(
    private val applicationAware: IApplicationAware,
    private val authHashStrategy: IAuthHashStrategy
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val timestamp = System.currentTimeMillis().toString()
        val originalRequest = chain.request()
        val urlWithApiKey = originalRequest.url()
            .newBuilder()
            .addQueryParameter(TIMESTAMP_KEY_PARAM_NAME, timestamp)
            .addQueryParameter(API_KEY_KEY_PARAM_NAME, applicationAware.getApiPublicKey())
            .addQueryParameter(HASH_KEY_PARAM_NAME, authHashStrategy.generateHash(timestamp))
            .build()
        val requestWithApiKeyQueryParam = originalRequest.newBuilder()
            .url(urlWithApiKey)
            .build()
        return chain.proceed(requestWithApiKeyQueryParam)
    }

    companion object {

        /**
         * Timestamp Key Param Name
         */
        private const val TIMESTAMP_KEY_PARAM_NAME = "ts"

        /**
         * Hash Key Param Name
         */
        private const val HASH_KEY_PARAM_NAME = "hash"

        /**
         * API Key Param Name
         */
        private const val API_KEY_KEY_PARAM_NAME = "apikey"
    }
}

