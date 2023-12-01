package com.tong.wanandroid.ui.message.child

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.tong.wanandroid.BasePagingSource
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager

class MessageChildViewModel : ViewModel() {

    private val service = RetrofitManager.create(ApiService::class.java)

    val getReadiedMsgFlow =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, _ ->

                val result = service.getReadiedMessageList(page).data
                with(ArrayList<Any>(result.datas.size)) {
                    addAll(result.datas)
                    this
                }
            }
        }.flow.cachedIn(viewModelScope)

    val getUnreadMsgFlow =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, _ ->

                val result = service.getUnReadMessageList(page).data
                with(ArrayList<Any>(result.datas.size)) {
                    addAll(result.datas)
                    this
                }
            }
        }.flow.cachedIn(viewModelScope)

}