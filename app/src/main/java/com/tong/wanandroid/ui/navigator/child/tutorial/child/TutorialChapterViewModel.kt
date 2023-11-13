package com.tong.wanandroid.ui.navigator.child.tutorial.child

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.ArticleModel
import kotlinx.coroutines.launch

class TutorialChapterViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    private val _chapterListLiveData = MutableLiveData<List<ArticleModel>>()
    val chapterListLiveData: LiveData<List<ArticleModel>> = _chapterListLiveData

    fun getChapterList(tutorialId: Int) {
        viewModelScope.launch {
            api.getTutorialChapterList(tutorialId).let {
                if (it.isSuccess()) {
                    it.data.let { tags ->
                        _chapterListLiveData.value = tags.datas
                    }
                }

            }
        }
    }
}