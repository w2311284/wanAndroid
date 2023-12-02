package com.tong.wanandroid.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.datastore.DataStoreManager
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.UserBaseModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

enum class CollapsingToolBarState{

    /**
     * 展开
     */
    EXPANDED,

    /**
     * 折叠
     */
    COLLAPSED,

    /**
     * 过渡态
     */
    INTERMEDIATE

}
class ProfileViewModel: ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    private val _userInfoLiveData = MutableLiveData<UserBaseModel>()
    val userInfoLiveData: LiveData<UserBaseModel> = _userInfoLiveData

    var collapsingToolBarStateFlow =
        MutableStateFlow(CollapsingToolBarState.EXPANDED)

    var isLogin : Boolean = false

    var userId : String = ""

    fun getLoginCache(context: Context) : Boolean{
        return DataStoreManager.getInstance(context).getLoginCache()
    }


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

