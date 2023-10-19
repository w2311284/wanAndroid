package com.tong.wanandroid.common.services

import com.tong.wanandroid.common.services.http.NetworkResponse
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.BannerModel
import com.tong.wanandroid.common.services.model.PageModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("banner/json")
    suspend fun getBanner(): NetworkResponse<List<BannerModel>>

    @GET("article/list/{pageNo}/json")
    suspend fun getArticleList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): NetworkResponse<PageModel<ArticleModel>>

}