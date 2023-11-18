package com.tong.wanandroid.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tong.wanandroid.BasePagingSource
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.HotKeyModel
import com.tong.wanandroid.common.utils.LimitedLruCache
import com.tong.wanandroid.ui.project.child.tryOffer
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class SearchViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

//    private val _shortcutSearch = Channel<String>(Channel.CONFLATED)
//    val shortcutSearch = _shortcutSearch.receiveAsFlow()

    private val _shortcutSearchLiveData = MutableLiveData<String>()
    val shortcutSearchLiveData: LiveData<String> = _shortcutSearchLiveData

    private val historyLruCache = LimitedLruCache<String>(20)

    private val _searchLiveData = MutableLiveData<String>()
    val searchLiveData: LiveData<String> = _searchLiveData

    private val _historyLiveData = MutableLiveData<List<String>>()
    val historyLiveData: LiveData<List<String>> = _historyLiveData

    private val _hotKeysLiveData = MutableLiveData<List<HotKeyModel>>()
    val hotKeysLiveData: LiveData<List<HotKeyModel>> = _hotKeysLiveData

    private val _searchResults: MutableLiveData<Flow<PagingData<Any>>> = MutableLiveData()
    val searchResults: LiveData<Flow<PagingData<Any>>> = _searchResults

    fun search(keywords: String){
        _searchLiveData.value = keywords
        val dataFlow = Pager(
            PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                enablePlaceholders = false
            )
        ){
            BasePagingSource(0){ page, size ->
                val articles = api.queryBySearchKey(page,keywords).data
                with(ArrayList<Any>(articles.datas.size)) {
                    addAll(articles.datas)
                    this
                }
            }
        }.flow
        _searchResults.value = dataFlow.cachedIn(viewModelScope)
    }

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

    fun shortcutSearch(keywords: String) {
        _shortcutSearchLiveData.value = keywords
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