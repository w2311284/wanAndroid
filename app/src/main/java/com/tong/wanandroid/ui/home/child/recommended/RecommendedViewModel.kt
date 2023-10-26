package com.tong.wanandroid.ui.home.child.recommended


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.tong.wanandroid.BasePagingSource
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.BannerModel
import com.tong.wanandroid.common.services.model.Banners
import com.tong.wanandroid.common.services.model.PageModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope


class RecommendedViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val api = RetrofitManager.create(ApiService::class.java)

//    private val _bannerResponse = MutableLiveData<List<BannerModel>>()
//    val bannerResponse : MutableLiveData<List<BannerModel>> = _bannerResponse
//    private val _articleResponse = MutableLiveData<PageModel<ArticleModel>>()
//    val articleResponse : MutableLiveData<PageModel<ArticleModel>> = _articleResponse

    val getArticlesFlow = getHomePageData(20).cachedIn(viewModelScope)

    fun getHomePageData(pageSize: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, size ->
                if (page == 0) {
                    val (articlesDeferred, topsDeferred, bannersDeferred) =
                        supervisorScope{
                            Triple(
                                async { api.getArticleList(page, size) },
                                async { api.getArticleTopList() },
                                async { api.getBanner() })
                        }
                    val articles = articlesDeferred.await().data
                    val tops = topsDeferred.await().data
                    val banners = bannersDeferred.await().data
                    with(ArrayList<Any>(1 + tops.size + articles.datas.size)) {
                        if (banners.isNotEmpty()) {
                            add(Banners(banners))
                        }
                        addAll(tops)
                        addAll(articles.datas)
                        this
                    }

                } else {
                    // page不为0则只是加载更多即可
                    api.getArticleList(page,size).data.datas
                }
            }
        }.flow

//    fun getBanner(){
//        viewModelScope.launch {
//            api.getBanner().let { response ->
//                if (response.isSuccess()){
//                    response.data.let {
//                        _bannerResponse.value = it
//                    }
//                }
//            }
//        }
//    }
//    fun getArticleList(pageNo: Int){
//        viewModelScope.launch {
//            api.getArticleList(pageNo,20).let { response ->
//                if (response.isSuccess()){
//                    response.data.let {
//                        _articleResponse.value = it
//                    }
//                }
//            }
//        }
//    }
}