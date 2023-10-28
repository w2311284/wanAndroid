package com.tong.wanandroid.ui.home.child.square

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tong.wanandroid.BasePagingSource
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.ArticleModel
import kotlinx.coroutines.flow.Flow

class SquareViewModel : ViewModel() {
    private val api = RetrofitManager.create(ApiService::class.java)

    val getSquareFlow = getSquarePageData(20)

    fun getSquarePageData(pageSize: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, size ->
                api.getSquarePageList(page,size).data.datas
            }
        }.flow.cachedIn(viewModelScope)
}