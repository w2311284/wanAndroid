package com.tong.wanandroid.ui.navigator.child.series.detail

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

class SeriesDetailViewModel : ViewModel(){

    private val api = RetrofitManager.create(ApiService::class.java)

    private val _fetchSeriesDetails = Channel<Int>(Channel.CONFLATED)

    val getSeriesDetailListFlow = _fetchSeriesDetails.receiveAsFlow().flatMapLatest{
        getSeriesDetailList(it,20)
    }.cachedIn(viewModelScope)

    fun setDetailId(id: Int){
        _fetchSeriesDetails.tryOffer(id)
    }

    fun getSeriesDetailList(id: Int, size: Int) =
        Pager(
            PagingConfig(
                pageSize = size,
                initialLoadSize = size,
                enablePlaceholders = false
            )
        ) {
            BasePagingSource(
                0,
            ) { page, size ->

                val articles = api.getSeriesDetailList(page,id,size).data
                with(ArrayList<Any>(articles.datas.size)) {
                    addAll(articles.datas)
                    this
                }
            }
        }.flow

}