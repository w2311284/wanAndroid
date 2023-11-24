package com.tong.wanandroid.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.NetworkResponse
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.UserBaseModel
import com.tong.wanandroid.common.services.model.UserModel
import com.tong.wanandroid.common.store.UserInfoManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    var isLogin: Boolean = false

    private val _userInfoLiveData = MutableLiveData<UserBaseModel>()
    val userInfoLiveData: LiveData<UserBaseModel> = _userInfoLiveData


    fun getUserInfo(){
        viewModelScope.launch {
            api.getUserInfo().let {
                if (it.isSuccess()){
                    it.data.let { m ->
                        _userInfoLiveData.value = m
                    }
                }
            }
        }
    }


}