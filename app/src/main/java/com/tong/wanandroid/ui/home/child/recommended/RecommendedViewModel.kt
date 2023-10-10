package com.tong.wanandroid.ui.home.child.recommended

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RecommendedViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val api = RetrofitManager.create(ApiService::class.java)

    fun getBanner(){
        viewModelScope.launch {
            flow<Int> {
                emit(api.getBanner())
            }
        }
    }
}