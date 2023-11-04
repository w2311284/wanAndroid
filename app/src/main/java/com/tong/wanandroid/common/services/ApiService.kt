package com.tong.wanandroid.common.services

import com.tong.wanandroid.common.services.http.NetworkResponse
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.BannerModel
import com.tong.wanandroid.common.services.model.ClassifyModel
import com.tong.wanandroid.common.services.model.NavigationModel
import com.tong.wanandroid.common.services.model.PageModel
import com.tong.wanandroid.common.services.model.ProjectTitleModel
import com.tong.wanandroid.common.services.model.SeriesModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /***********   首页   ***********/

    /**
     * 首页banner
     */
    @GET("banner/json")
    suspend fun getBanner(): NetworkResponse<List<BannerModel>>

    /**
     * 首页文章
     */
    @GET("article/list/{pageNo}/json")
    suspend fun getArticleList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): NetworkResponse<PageModel<ArticleModel>>

    /**
     * 首页置顶文章
     */
    @GET("article/top/json")
    suspend fun getArticleTopList(): NetworkResponse<List<ArticleModel>>

    /**
     * 广场文章
     */
    @GET("user_article/list/{pageNo}/json")
    suspend fun getSquarePageList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): NetworkResponse<PageModel<ArticleModel>>


    /**
     * 问答列表
     */
    @GET("wenda/list/{pageNo}/json")
    suspend fun getAnswerPageList(@Path("pageNo") pageNo: Int): NetworkResponse<PageModel<ArticleModel>>



    /***********   项目   ***********/


    /**
     * 项目分类数据
     */
    @GET("project/tree/json")
    suspend fun getProjectTitleList(): NetworkResponse<List<ProjectTitleModel>>

    /**
     * 项目文章列表
     */
    @GET("project/list/{pageNo}/json")
    suspend fun getProjectPageList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int,
        @Query("cid") categoryId: Int
    ): NetworkResponse<PageModel<ArticleModel>>

    /**
     * 最新项目列表
     */
    @GET("article/listproject/{pageNo}/json")
    suspend fun getNewProjectPageList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): NetworkResponse<PageModel<ArticleModel>>



    /***********   导航   ***********/


    /**
     * 导航数据
     */
    @GET("navi/json")
    suspend fun getNavigationList(): NetworkResponse<List<NavigationModel>>

    /**
     * 体系数据
     */
    @GET("tree/json")
    suspend fun getTreeList(): NetworkResponse<List<SeriesModel>>

    /**
     * 教程列表
     */
    @GET("chapter/547/sublist/json")
    suspend fun getTutorialList(): NetworkResponse<List<ClassifyModel>>


    /**
     * 对应教程的章节列表
     */
    @GET("article/list/0/json")
    suspend fun getTutorialChapterList(
        @Query("cid") id: Int,
        @Query("order_type") orderType: Int = 1
    ): NetworkResponse<PageModel<ArticleModel>>


    /**
     * 系列对应Tag的文章列表
     */
    @GET("article/list/{page}/json")
    suspend fun getSeriesDetailList(
        @Path("page") page: Int,
        @Query("cid") id: Int,
        @Query("page_size") size: Int
    ): NetworkResponse<PageModel<ArticleModel>>


}