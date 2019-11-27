package uk.co.applylogic.marvel.data.di.module

import android.content.Context
import com.openpayd.core.android.di.qualifier.HttpCache
import com.openpayd.core.android.di.qualifier.HttpLogging
import com.openpayd.core.android.di.qualifier.UnauthorizedInterceptor
import com.openpayd.core.android.di.qualifier.UnauthorizedOkHttpClient
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.applylogic.marvel.core_android.handler.NetworkErrorHandler
import uk.co.applylogic.marvel.core_android.handler.NetworkErrorHandlerInterface
import uk.co.applylogic.marvel.data.BuildConfig
import uk.co.applylogic.marvel.data.di.scope.ContentRetrofit
import uk.co.applylogic.marvel.data.interceptors.CacheInterceptor
import uk.co.applylogic.marvel.data.interceptors.NonAuthInterceptor
import uk.co.applylogic.marvel.data.repository.ContentInterface
import uk.co.applylogic.marvel.data.repository.ContentRepository
import uk.co.applylogic.marvel.data.repository.ContentRepositoryContract
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val TEN_MB: Long = 10 * 1024 * 1024

@Module
class NetworkModule(val context: Context) {

    @Provides
    @Singleton
    internal fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    internal fun provideNetworkErrorHandler(context: Context): NetworkErrorHandlerInterface {
        return NetworkErrorHandler(context)
    }

    @Provides
    @Singleton
    internal fun provideContentRepository(): ContentRepositoryContract {
        return ContentRepository()
    }

    @Provides
    @Singleton
    fun provideContentInterface(@ContentRetrofit retrofit: Retrofit): ContentInterface {
        return retrofit.create(ContentInterface::class.java)
    }

    @Provides
    @Singleton
    @ContentRetrofit
    fun provideRetrofit(@UnauthorizedOkHttpClient okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.MARVEL_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @UnauthorizedOkHttpClient
    internal fun provideOkHttpClient(
        @HttpLogging logger: HttpLoggingInterceptor,
        @UnauthorizedInterceptor unAuthorizedInterceptor: NonAuthInterceptor,
        @Named("cache_interceptor") cacheInterceptor: Interceptor,
        @HttpCache cache: Cache?
    ): OkHttpClient {
        return getOkHttpClient(
            logger, unAuthorizedInterceptor, cacheInterceptor, cache
        )
    }

    private fun getOkHttpClient(
        logger: HttpLoggingInterceptor,
        interceptor: Interceptor,
        cacheInterceptor: Interceptor,
        cache: Cache?
    ): OkHttpClient {

        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder
            .addInterceptor(cacheInterceptor)
            .addInterceptor(interceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)

        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(logger)
        }
        return okHttpBuilder.build().newBuilder().cache(cache).build()
    }

    @Provides
    @Singleton
    @UnauthorizedInterceptor
    internal fun provideNonAuthInterceptor(): NonAuthInterceptor {
        return NonAuthInterceptor()
    }

    @Provides
    @Singleton
    @HttpCache
    internal fun provideCache(): Cache? {
        try {
            return Cache(File(context.cacheDir, "http-cache"), TEN_MB)
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
        return null
    }

    @Provides
    @Singleton
    @HttpLogging
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BODY
        return logger
    }


    @Provides
    @Singleton
    @Named("cache_interceptor")
    internal fun provideCacheInterceptor(): Interceptor {
        return CacheInterceptor(context)
    }

//    @Provides
//    @Singleton
//    internal fun provideContentDatabase(): ContentDatabase {
//        return ContentDatabase.getInstance(context)
//    }
}
