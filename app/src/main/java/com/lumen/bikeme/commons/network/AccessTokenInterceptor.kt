package com.lumen.bikeme.commons.network

import com.lumen.bikeme.commons.service.UserService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response


class AccessTokenInterceptor(
    private val userService: UserService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            userService.getAccessToken()
        }
        val original = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("auth", token)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)
        val request = requestBuilder.build()
        return chain.proceed(request);
    }
}