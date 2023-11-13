package com.tong.wanandroid.ui.group

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.ClassifyModel
import kotlinx.coroutines.launch

class GroupViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    private val _groupTitles = MutableLiveData<List<ClassifyModel>>()
    val groupTitles: LiveData<List<ClassifyModel>> = _groupTitles

    fun getProjectTitles(){
        viewModelScope.launch {
            api.getAuthorTitleList().let {
                if (it.isSuccess()){
                    it.data.let { titles ->
                        var items = mutableListOf<ClassifyModel>().apply {
                            addAll(titles)
                        }
                        _groupTitles.value = items
                    }
                }
            }
        }
    }

}