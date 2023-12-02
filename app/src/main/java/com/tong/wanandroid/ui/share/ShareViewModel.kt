package com.tong.wanandroid.ui.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.tong.wanandroid.BasePagingSource
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.ShareModel
import com.tong.wanandroid.ui.profile.CollapsingToolBarState
import com.tong.wanandroid.ui.project.child.tryOffer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow

class ShareViewModel : ViewModel() {
    private val service = RetrofitManager.create(ApiService::class.java)

    private val _fetchShare = Channel<String>(Channel.CONFLATED)

    var collapsingToolBarStateFlow =
        MutableStateFlow(CollapsingToolBarState.EXPANDED)

    private val _shareBeanFlow = MutableSharedFlow<ShareModel>(1)
    val shareBeanFlow: SharedFlow<ShareModel> = _shareBeanFlow

    /**
     * 分享列表数据流
     */
    val shareListFlow = _fetchShare.receiveAsFlow().flatMapLatest {
        getShareList(it, true)
    }.cachedIn(viewModelScope)

    fun getShareList(userId: String, isMe: Boolean) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, _ ->

                val result = if (isMe) {
                    service.getMyShareList(page)
                }else{
                    service.getMyShareList(page)
                }
                if (result.isSuccess()){
                    _shareBeanFlow.emit(result.data)
                }
                with(ArrayList<Any>(result.data.shareArticles.datas.size)) {
                    addAll(result.data.shareArticles.datas)
                    this
                }
            }
        }.flow


    fun fetch(userId: String) {
        _fetchShare.tryOffer(userId)
    }
}