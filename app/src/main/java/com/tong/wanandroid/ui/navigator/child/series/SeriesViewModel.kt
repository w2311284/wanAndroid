package com.tong.wanandroid.ui.navigator.child.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import kotlinx.coroutines.launch

class SeriesViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    private val _seriesListLiveData = MutableLiveData<List<Any>>()
    val seriesListLiveData: LiveData<List<Any>> = _seriesListLiveData

    fun getSeriesList(){
        viewModelScope.launch {
            api.getTreeList().let {
                if (it.isSuccess()){
                    it.data.let { tags ->
                        _seriesListLiveData.value = tags
                    }
                }
            }
        }
    }

}