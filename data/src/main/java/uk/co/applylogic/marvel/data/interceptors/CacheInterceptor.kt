package uk.co.applylogic.marvel.data.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import okhttp3.Interceptor
import okhttp3.Response
import uk.co.applylogic.marvel.data.utils.NetworkUtils
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

const val MAX_STALE = 5 * 60

class CacheInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = if (NetworkUtils.isConnected(context))
            request.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=$MAX_STALE").build()
        else
            request.newBuilder()
                .header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$MAX_STALE"
                ).build()
        return chain.proceed(request)
    }
}
