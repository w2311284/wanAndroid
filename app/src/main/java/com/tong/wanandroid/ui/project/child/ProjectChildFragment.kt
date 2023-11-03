package com.tong.wanandroid.ui.project.child

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.FragmentProjectChildBinding
import com.tong.wanandroid.ui.footer.FooterStateAdapter
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectChildFragment : Fragment() {

    private var _binding: FragmentProjectChildBinding? = null
    private val binding get() = _binding!!

    private val projectAdapter by lazy { ProjectAdapter(this@ProjectChildFragment::onItemClick) }

    companion object {
        fun newInstance(categoryId: Int) = ProjectChildFragment().apply {
            arguments = bundleOf("key_project_child_category_id" to categoryId)
        }
    }

    private val categoryId by lazy { arguments?.getInt("key_project_child_category_id", -1) ?: -1 }

    private lateinit var viewModel: ProjectChildViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ProjectChildViewModel::class.java]
        _binding = FragmentProjectChildBinding.inflate(inflater, container, false)
        initView()
        if (savedInstanceState == null) {
            viewModel.fetch(categoryId)
        }
        return binding.root
    }

    fun initView(){
        val recycleView = binding.projectList
        val swipeRefreshLayout = binding.projectRefreshLayout

        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = projectAdapter.withLoadStateFooter(
                footer = FooterStateAdapter()
            )
        }
        lifecycleScope.launch {
            viewModel.getProjectListFlow.collectLatest(projectAdapter::submitData)
        }

        projectAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    recycleView.scrollToPosition(0)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            projectAdapter.refresh()
        }
    }

    private fun onItemClick(articleAction: ArticleAction) {
        when (articleAction) {
            is ArticleAction.ItemClick -> pushToDetailActivity(articleAction.article)
            is ArticleAction.CollectClick -> null
            is ArticleAction.AuthorClick -> null
            else -> null
        }
    }

    private fun pushToDetailActivity(article: ArticleModel) {
        // 跳转到详情页面
        context?.let { WebActivity.loadUrl(it,article.id,article.link,article.collect) }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}