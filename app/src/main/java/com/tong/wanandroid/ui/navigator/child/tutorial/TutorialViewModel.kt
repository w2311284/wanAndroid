package com.tong.wanandroid.ui.navigator.child.tutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.ClassifyModel
import kotlinx.coroutines.launch

class TutorialViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    private val _tutorialListLiveData = MutableLiveData<List<ClassifyModel>>()
    val tutorialListLiveData: LiveData<List<ClassifyModel>> = _tutorialListLiveData

    fun getTutorialList(){
        viewModelScope.launch {
            api.getTutorialList().let {
                if (it.isSuccess()){
                    it.data.let { tags ->
                        _tutorialListLiveData.value = tags
                    }
                }
            }
        }
    }


}