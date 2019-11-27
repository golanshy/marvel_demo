package uk.co.applylogic.marvel.data.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class NonAuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("user-agent", "Android-Marvel-Demo-App")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "*/*")
            .build()
        return chain.proceed(request)
    }
}
