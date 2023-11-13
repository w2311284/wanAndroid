package com.tong.wanandroid.ui.group.child

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.FragmentGroupChildBinding
import com.tong.wanandroid.ui.footer.FooterStateAdapter
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import com.tong.wanandroid.ui.project.child.ProjectChildFragment
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class GroupChildFragment : Fragment() {

    private var _binding: FragmentGroupChildBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val key_group_child_id = "key_group_child_id"
        fun newInstance(id: Int) = GroupChildFragment().apply {
            arguments = bundleOf(key_group_child_id to id)
        }
    }

    private val childId by lazy { arguments?.getInt(key_group_child_id, -1) ?: -1 }

    private lateinit var viewModel: GroupChildViewModel

    private val groupAdapter by lazy { HomeAdapter(this@GroupChildFragment::onItemClick) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[GroupChildViewModel::class.java]
        _binding = FragmentGroupChildBinding.inflate(inflater, container, false)
        initView()
        if (savedInstanceState == null) {
            viewModel.fetch(childId)
        }
        return binding.root
    }

    private fun initView(){
        val recycleView = binding.groupList
        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = groupAdapter.withLoadStateFooter(
                footer = FooterStateAdapter()
            )
        }
        lifecycleScope.launch {
            groupAdapter.loadStateFlow.collectLatest(this@GroupChildFragment::updateLoadStates)
        }
        lifecycleScope.launch {
            viewModel.getAuthorListFlow.collectLatest(groupAdapter::submitData)
        }
        groupAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    recycleView.scrollToPosition(0)
                }
            }
        })


        val swipeRefreshLayout = binding.groupRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            groupAdapter.refresh()
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

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = groupAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }

}