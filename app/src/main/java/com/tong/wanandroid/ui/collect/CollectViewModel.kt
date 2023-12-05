package com.tong.wanandroid.ui.collect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.tong.wanandroid.BasePagingSource
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.CollectEventModel
import kotlinx.coroutines.launch

class CollectViewModel : ViewModel() {

    private val service = RetrofitManager.create(ApiService::class.java)

    val collectFlow = getCollectList().cachedIn(viewModelScope)

    /**
     * 全局收藏事件
     */
    private val _collectArticleLiveData = MutableLiveData<CollectEventModel>()
    val collectArticleEvent: LiveData<CollectEventModel> = _collectArticleLiveData

    fun articleCollectAction(event: CollectEventModel){
        viewModelScope.launch {
            if (event.isCollected){
                service.collectArticle(event.id).let {
                    if (it.isSuccess()){
                        _collectArticleLiveData.value = event
                    }
                }
            }else{
                service.unCollectArticle(event.id).let {
                    if (it.isSuccess()){
                        _collectArticleLiveData.value = event
                    }
                }
            }
        }
    }

    private fun getCollectList() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, _ ->
                val result = service.getCollectList(page).data
                with(ArrayList<Any>(result.datas.size)) {
                    addAll(result.datas)
                    this
                }
            }
        }.flow

}