package sanchez.sanchez.sergio.androidmobiletest.di.modules.network

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import sanchez.sanchez.sergio.androidmobiletest.BuildConfig
import sanchez.sanchez.sergio.androidmobiletest.di.scopes.PerApplication
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.interceptors.AuthInterceptor
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.interceptors.ConnectivityInterceptor
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.serder.DateJsonAdapter
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.utils.IAuthHashStrategy
import sanchez.sanchez.sergio.androidmobiletest.persistence.network.utils.impl.AuthHashStrategyImpl
import sanchez.sanchez.sergio.androidmobiletest.utils.IApplicationAware
import java.util.concurrent.TimeUnit
import javax.inject.Named

/**
 * App Network Module
 */
@Module
class NetworkModule {

    /**
     * Provide Converter Factory
     */
    @Provides
    @PerApplication
    fun provideConverterFactory(): Converter.Factory =
        MoshiConverterFactory.create(Moshi.Builder()
            .add(DateJsonAdapter())
            .build())

    /**
     * Provide Auth Hash Generator
     * @param applicationAware
     */
    @Provides
    @PerApplication
    fun provideAuthHashGenerator(applicationAware: IApplicationAware): IAuthHashStrategy =
        AuthHashStrategyImpl(applicationAware)

    /**
     * Provide Network Interceptors
     */
    @Provides
    @ElementsIntoSet
    @PerApplication
    @Named("networkInterceptors")
    fun provideNetworkInterceptors(): Set<Interceptor> =
        setOf<Interceptor>(StethoInterceptor())

    /**
     * Provide Request Interceptors
     */
    @Provides
    @ElementsIntoSet
    @PerApplication
    @Named("requestInterceptors")
    fun provideRequestInterceptors(
        context: Context,
        applicationAware: IApplicationAware,
        authHashStrategy: IAuthHashStrategy): Set<Interceptor> =
        setOf(
            ConnectivityInterceptor(context),
            AuthInterceptor(applicationAware, authHashStrategy)
        )

    /**
     * Provide HTTP Client
     * @param networkInterceptors
     * @param requestInterceptors
     */
    @Provides
    @PerApplication
    fun provideHttpClient(
        context: Context,
        @Named("networkInterceptors") networkInterceptors: Set<@JvmSuppressWildcards Interceptor>,
        @Named("requestInterceptors") requestInterceptors: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient {

        val okHttpClientBuilder =  OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .cache(Cache(context.cacheDir, DEFAULT_CACHE_SIZE))

        networkInterceptors.forEach {
            okHttpClientBuilder.addNetworkInterceptor(it)
        }
        requestInterceptors.forEach {
            okHttpClientBuilder.addInterceptor(it)
        }
        return okHttpClientBuilder.build()
    }

    /**
     * Provide Retrofit
     */
    @Provides
    @PerApplication
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        httpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .build()


    companion object {
        const val DEFAULT_CACHE_SIZE: Long =  10 * 1024 * 1024 // 10 MB
    }


}