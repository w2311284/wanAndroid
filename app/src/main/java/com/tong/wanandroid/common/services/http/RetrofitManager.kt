package com.tong.wanandroid.common.services.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    private const val BASE_URL = "https://www.wanandroid.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        //Scalar用于解析String数据
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(service: Class<T>):T = retrofit.create(service)
}