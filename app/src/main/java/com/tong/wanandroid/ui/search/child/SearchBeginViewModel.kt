package com.tong.wanandroid.ui.search.child

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.HotKeyModel
import com.tong.wanandroid.common.services.model.ProjectTitleModel
import com.tong.wanandroid.common.utils.LimitedLruCache
import com.tong.wanandroid.common.utils.LruCache
import kotlinx.coroutines.launch

class SearchBeginViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    private val historyLruCache = LimitedLruCache<String>(20)

    private val _hotKeysLiveData = MutableLiveData<List<HotKeyModel>>()
    val hotKeysLiveData: LiveData<List<HotKeyModel>> = _hotKeysLiveData

    private val _historyLiveData = MutableLiveData<List<String>>()
    val historyLiveData: LiveData<List<String>> = _historyLiveData

    fun getHotKeys(){
        viewModelScope.launch {
            api.getSearchHotKey().let {
                if (it.isSuccess()){
                    it.data.let { titles ->
                        var keys = mutableListOf<HotKeyModel>().apply {
                            addAll(titles)
                        }
                        _hotKeysLiveData.value = keys
                    }
                }
            }
        }
    }

    fun historyPut(str: String) {
        historyLruCache.add(str)
        _historyLiveData.value = historyLruCache.toList()
    }

    fun historyRemove(str: String) {
        historyLruCache.remove(str)
        _historyLiveData.value = historyLruCache.toList()
    }

}