package com.tong.wanandroid.common.services.http

import com.tong.wanandroid.common.services.cookie.CookieJarImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {
    const val BASE_URL = "https://www.wanandroid.com"

    val cookieJar = CookieJarImpl()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //Scalar用于解析String数据
        .addConverterFactory(GsonConverterFactory.create())
        .client(createOkHttpClient())
        .build()

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS).build()
    }

    fun <T> create(service: Class<T>):T = retrofit.create(service)
}