package com.tong.wanandroid.ui.register

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.NetworkResponse
import com.tong.wanandroid.common.services.http.RetrofitManager
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    val userNameObservable = ObservableField<String>()
    val passwordObservable = ObservableField<String>()
    val confirmPasswordObservable = ObservableField<String>()

    private val _registerLiveData = MutableLiveData<NetworkResponse<Any>>()
    val registerLiveData: LiveData<NetworkResponse<Any>> = _registerLiveData

    val loginEnable = object :
        ObservableBoolean(userNameObservable, passwordObservable, confirmPasswordObservable) {
        override fun get() =
            userNameObservable.get()?.trim().isNullOrBlank().not()
                    && passwordObservable.get()?.trim().isNullOrBlank().not()
                    && confirmPasswordObservable.get()?.trim().isNullOrBlank().not()
    }

    fun register(usermane: String,password: String,confirmPassword: String){
        viewModelScope.launch {
            _registerLiveData.value = api.register(usermane,password,confirmPassword)
        }
    }
}