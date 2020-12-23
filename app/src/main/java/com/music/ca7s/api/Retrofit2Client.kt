package com.music.ca7s.api

import com.music.ca7s.BuildConfig
import com.music.ca7s.utils.AppConstants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Retrofit2Client(val s: String) {
    private val retrofit: Retrofit
    private val client: OkHttpClient
    val exploreService: ExploreService
    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
            .readTimeout(5, TimeUnit.MINUTES); // read timeout
        okHttpBuilder.addInterceptor(TokenInterceptor(s))
        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(loggingInterceptor)
        }
        client = okHttpBuilder.build()
        retrofit = Retrofit.Builder().baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
        exploreService = retrofit.create<ExploreService>(ExploreService::class.java)
    }
    companion object {
        private var instance: Retrofit2Client? = null
        fun getInstance(s : String) : Retrofit2Client {
                instance = Retrofit2Client(s)
            return instance!!
        }
    }
}