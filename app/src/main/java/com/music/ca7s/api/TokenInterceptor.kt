package com.music.ca7s.api

import android.util.Log
import com.music.ca7s.contant.ApiParameter
import okhttp3.Interceptor
import okhttp3.Response




class TokenInterceptor(var s: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val builder = original.newBuilder()
//        Log.e("TokenInterceptor","Bearer "+ s)
            builder.header(ApiParameter.USER_AGENT,  ApiParameter.CA7S_APP)
             builder.addHeader(ApiParameter.COOKIE,s)
        val request = builder.method(original.method, original.body)
            .build()
        return chain.proceed(request)
    }

}
