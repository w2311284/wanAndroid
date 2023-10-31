package com.tong.wanandroid.ui.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tong.wanandroid.common.services.ApiService
import com.tong.wanandroid.common.services.http.RetrofitManager
import com.tong.wanandroid.common.services.model.ProjectTitleModel
import kotlinx.coroutines.launch

class ProjectViewModel : ViewModel() {

    private val api = RetrofitManager.create(ApiService::class.java)

    private val _projectTitles = MutableLiveData<List<ProjectTitleModel>>()
    val projectTitleList: LiveData<List<ProjectTitleModel>> = _projectTitles

    fun getProjectTitles(){
        viewModelScope.launch {
            api.getProjectTitleList().let {
                if (it.isSuccess()){
                    it.data.let { titles ->
                        var projects = mutableListOf<ProjectTitleModel>().apply {
                            add(generateNewestProjectBean())
                            addAll(titles)
                        }
                        _projectTitles.value = projects
                    }
                }
            }
        }
    }

    private fun generateNewestProjectBean() = ProjectTitleModel(
        id = 0, name = "最新项目"
    )

}