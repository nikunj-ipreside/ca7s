package com.music.ca7s.utils;


import com.music.ca7s.AppLevelClass;
import com.music.ca7s.apicall.iApiService;
import com.music.ca7s.contant.ApiParameter;
import com.music.ca7s.contant.SharedPref;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroClient {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .readTimeout(300, TimeUnit.SECONDS).build();

    private static Retrofit getRetrofitInstance() {

      /*  Gson gson = new GsonBuilder()
                .setLenient()
                .create();*/
        return new Retrofit.Builder()
                .baseUrl(ApiParameter.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static iApiService getApiService() {
        return getRetrofitInstance().create(iApiService.class);
    }

}
