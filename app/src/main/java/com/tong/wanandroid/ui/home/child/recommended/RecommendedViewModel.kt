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
    private val _bannerResponse = MutableLiveData<List<BannerModel>>()
    val bannerResponse : MutableLiveData<List<BannerModel>> = _bannerResponse

    fun getBanner(){
        viewModelScope.launch {
            api.getBanner().let { response ->
                if (response.isSuccess()){
                    response.data.let {
                        _bannerResponse.value = it
                    }
                }
            }
        }
    }
}