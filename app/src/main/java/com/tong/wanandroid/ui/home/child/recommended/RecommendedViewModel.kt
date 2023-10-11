package com.tong.wanandroid.ui.home.child.recommended

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.BannerModel
import kotlinx.coroutines.launch


class RecommendedViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val api = RetrofitManager.create(ApiService::class.java)

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is notifications Fragment"
//    }
//    val text: LiveData<String> = _text
    private val _banners = MutableLiveData<List<BannerModel>?>()
    val banners : MutableLiveData<List<BannerModel>?> = _banners

    fun getBanner(){
        viewModelScope.launch {
            api.getBanner().let { response ->
                if (response.isSuccess()){
                    _banners.value = response.data
                }
            }
        }
    }
}