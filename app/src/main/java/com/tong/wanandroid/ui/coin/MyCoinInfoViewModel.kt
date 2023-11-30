package com.tong.wanandroid.ui.coin

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
import com.tong.wanandroid.common.services.model.UserBaseModel
import kotlinx.coroutines.launch

class MyCoinInfoViewModel : ViewModel() {

    private val service = RetrofitManager.create(ApiService::class.java)

    val coinHistoryFlow = getCoinHistory().cachedIn(viewModelScope)

    private val _userInfoLiveData = MutableLiveData<UserBaseModel>()
    val userInfoLiveData: LiveData<UserBaseModel> = _userInfoLiveData

    private fun getCoinHistory() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0) { page, _ ->

                val result = service.getMyCoinList(page).data
                with(ArrayList<Any>(result.datas.size)) {
                    addAll(result.datas)
                    this
                }
            }
        }.flow

    fun getUserInfo(){
        viewModelScope.launch {
            service.getUserInfo().let {
                if (it.isSuccess()){
                    it.data.let { m ->
                        _userInfoLiveData.value = m
                    }
                }
            }
        }
    }
}
