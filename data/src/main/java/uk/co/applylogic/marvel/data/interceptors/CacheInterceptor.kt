package uk.co.applylogic.marvel.data.interceptors

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

const val MAX_STALE = 5

class CacheInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val cacheBuilder = CacheControl.Builder()
        cacheBuilder.maxStale(MAX_STALE, TimeUnit.MINUTES)
        val cacheControl = cacheBuilder.build()

        var request = chain.request()
        if (checkInternet()) {
            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
        }
        return chain.proceed(request)
    }

    @SuppressLint("MissingPermission")
    private fun checkInternet(): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.activeNetworkInfo?.isConnectedOrConnecting == true
        } catch (e: Exception) {
            false
        }
    }
}
