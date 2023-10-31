package com.tong.wanandroid.ui.project.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.tong.wanandroid.BasePagingSource
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow

class ProjectChildViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    private val _fetchProjects = Channel<Int>(Channel.CONFLATED)

    val getProjectListFlow = _fetchProjects.receiveAsFlow().flatMapLatest{
        if (it == 0) getNewProjectListFlow(
            10
        ) else getProjectList(10, it)
    }

    fun getProjectList(pageSize: Int, categoryId: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, size ->

                val articles = api.getProjectPageList(page,size,categoryId).data
                with(ArrayList<Any>(articles.datas.size)) {
                    addAll(articles.datas)
                    this
                }
            }
        }.flow.cachedIn(viewModelScope)

    fun getNewProjectListFlow(pageSize: Int) =
        Pager(
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            BasePagingSource(
                0
            ) { page, size ->
                val articles = api.getNewProjectPageList(page,size).data
                with(ArrayList<Any>(articles.datas.size)) {
                    addAll(articles.datas)
                    this
                }
            }
        }.flow

    fun fetch(categoryId: Int) {
        _fetchProjects.tryOffer(categoryId)
    }
}

fun <E> SendChannel<E>.tryOffer(element: E): Boolean = try {
    trySend(element).isSuccess
} catch (t: Throwable) {
    false // Ignore
}