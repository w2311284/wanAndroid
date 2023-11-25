package com.tong.wanandroid.ui.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.NetworkResponse
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.HotKeyModel
import com.tong.wanandroid.common.services.model.UserModel
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val service = RetrofitManager.create(ApiService::class.java)

    val userNameObservable = ObservableField<String>()
    val passwordObservable = ObservableField<String>()

    val loginEnable = object : ObservableBoolean(userNameObservable, passwordObservable) {

        override fun get() =
            userNameObservable.get()?.trim().isNullOrBlank().not() && passwordObservable.get()
                ?.trim().isNullOrBlank().not()
    }

    private val _loginLiveData = MutableLiveData<NetworkResponse<UserModel>>()
    val loginLiveData: LiveData<NetworkResponse<UserModel>> = _loginLiveData

    fun loginIn(usermane: String,password: String){
        viewModelScope.launch {
            service.login(usermane,password).let {
                if (it.isSuccess()){
                    _loginLiveData.value = it
                }
            }
        }
    }
}