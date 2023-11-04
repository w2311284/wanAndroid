package com.tong.wanandroid.ui.navigator.child.navigator

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager


class NavigatorChildViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    val navigationTagListLiveData : LiveData<List<Any>> = liveData {
        api.getNavigationList().data
    }

}