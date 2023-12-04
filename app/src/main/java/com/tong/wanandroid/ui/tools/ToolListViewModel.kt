package com.tong.wanandroid.ui.tools

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager

class ToolListViewModel : ViewModel() {
    private val service = RetrofitManager.create(ApiService::class.java)

    val toolsLiveData = liveData {
        emit(
            service.getToolList().data

        )
    }
}