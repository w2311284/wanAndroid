package com.tong.wanandroid.ui.navigator.child.navigator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.ProjectTitleModel
import kotlinx.coroutines.launch


class NavigatorChildViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

//    val navigationTagListLiveData : LiveData<List<Any>> = liveData {
//        api.getNavigationList().data
//    }


    private val _navigationTagListLiveData = MutableLiveData<List<Any>>()
    val navigationTagListLiveData: LiveData<List<Any>> = _navigationTagListLiveData

    fun getNavigationList(){
        viewModelScope.launch {
            api.getNavigationList().let {
                if (it.isSuccess()){
                    it.data.let { tags ->
                        _navigationTagListLiveData.value = tags
                    }
                }
            }
        }
    }

}