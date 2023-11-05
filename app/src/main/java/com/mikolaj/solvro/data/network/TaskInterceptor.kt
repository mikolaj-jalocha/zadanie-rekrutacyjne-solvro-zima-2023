package com.mikolaj.solvro.data.network

import okhttp3.Interceptor
import okhttp3.Response

class TaskInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader(
                name = AuthenticationKeys.userId.first,
                value = AuthenticationKeys.userId.second
            )
            .addHeader(
                name = AuthenticationKeys.secretKey.first,
                value = AuthenticationKeys.secretKey.second
            )
            .build()

        return chain.proceed(request)

    }
}