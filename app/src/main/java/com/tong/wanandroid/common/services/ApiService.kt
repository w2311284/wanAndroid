package com.tong.wanandroid.common.services

import com.tong.wanandroid.common.services.http.NetworkResponse
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.BannerModel
import com.tong.wanandroid.common.services.model.ClassifyModel
import com.tong.wanandroid.common.services.model.CoinHistoryModel
import com.tong.wanandroid.common.services.model.CoinModel
import com.tong.wanandroid.common.services.model.HotKeyModel
import com.tong.wanandroid.common.services.model.NavigationModel
import com.tong.wanandroid.common.services.model.PageModel
import com.tong.wanandroid.common.services.model.ProjectTitleModel
import com.tong.wanandroid.common.services.model.SeriesModel
import com.tong.wanandroid.common.services.model.UserBaseModel
import com.tong.wanandroid.common.services.model.UserModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
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


    /***********   公众号   ***********/

    /**
     * 公众号作者列表
     */
    @GET("wxarticle/chapters/json")
    suspend fun getAuthorTitleList(): NetworkResponse<List<ClassifyModel>>

    /**
     * 对于id作者的文章
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getAuthorArticles(
        @Path("id") id: Int,
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int
    ): NetworkResponse<PageModel<ArticleModel>>

    /***********   搜索   ***********/

    /**
     * 热搜词
     */
    @GET("hotkey/json")
    suspend fun getSearchHotKey(): NetworkResponse<List<HotKeyModel>>

    /**
     * 搜索
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    suspend fun queryBySearchKey(
        @Path("page") page: Int,
        @Field("k") key: String
    ): NetworkResponse<PageModel<ArticleModel>>


    /***********   用户中心   ***********/


    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): NetworkResponse<UserModel>

    /**
     * 登出
     */
    @GET("user/logout/json")
    suspend fun logout(): NetworkResponse<Any>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") confirmPassword: String
    ): NetworkResponse<Any>

    /**
     * 获取用户信息
     */
    @GET("user/lg/userinfo/json")
    suspend fun getUserInfo(): NetworkResponse<UserBaseModel>

    @GET("/lg/coin/list/{page}/json")
    suspend fun getMyCoinList(@Path("page") page: Int): NetworkResponse<PageModel<CoinHistoryModel>>

    @GET("coin/rank/{page}/json")
    suspend fun getCoinRanking(@Path("page") page: Int): NetworkResponse<PageModel<CoinModel>>


}