package com.tong.wanandroid.ui.group.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.tong.wanandroid.BasePagingSource
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.ui.project.child.tryOffer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow

class GroupChildViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    private val _fetchAuthors = Channel<Int>(Channel.CONFLATED)

    val getAuthorListFlow = _fetchAuthors.receiveAsFlow().flatMapLatest{
        getAuthorArticles(10, it)
    }

    fun getAuthorArticles(pageSize: Int, categoryId: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, size ->

                val articles = api.getAuthorArticles(categoryId,page,size).data
                with(ArrayList<Any>(articles.datas.size)) {
                    addAll(articles.datas)
                    this
                }
            }
        }.flow.cachedIn(viewModelScope)

    fun fetch(id: Int) {
        _fetchAuthors.tryOffer(id)
    }

}