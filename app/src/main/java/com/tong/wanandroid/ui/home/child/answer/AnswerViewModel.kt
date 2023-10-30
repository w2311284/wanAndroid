package com.tong.wanandroid.ui.home.child.answer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.tong.wanandroid.BasePagingSource
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager

class AnswerViewModel : ViewModel() {
    private val api = RetrofitManager.create(ApiService::class.java)

    val getAnswerFlow = getAnswerPageData(10)

    fun getAnswerPageData(pageSize: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, size ->

                val articles = api.getAnswerPageList(page).data
                with(ArrayList<Any>(articles.datas.size)) {
                    addAll(articles.datas)
                    this
                }
            }
        }.flow.cachedIn(viewModelScope)
}