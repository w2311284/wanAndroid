package com.tong.wanandroid.common.services

import com.tong.wanandroid.common.services.http.NetworkResponse
import com.tong.wanandroid.common.services.model.BannerModel
import retrofit2.http.GET

interface ApiService {

    @GET("banner/json")
    suspend fun getBanner(): NetworkResponse<List<BannerModel>>


}