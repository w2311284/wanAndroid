package com.tong.wanandroid.ui.group.child

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.CollectEventModel
import com.tong.wanandroid.databinding.FragmentGroupChildBinding
import com.tong.wanandroid.ui.collect.CollectViewModel
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

    val collectViewModel: CollectViewModel by viewModels()

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

        collectViewModel.collectArticleEvent.observe(viewLifecycleOwner) { event ->
            groupAdapter.snapshot().run {
                val index = indexOfFirst { it is ArticleModel && it.id == event.id }
                if (index >= 0) {
                    (this[index] as? ArticleModel)?.collect = event.isCollected
                    index
                } else null
            }?.apply(groupAdapter::notifyItemChanged)
        }
    }

    private fun onItemClick(action: ArticleAction) {
        when (action) {
            is ArticleAction.ItemClick -> WebActivity.loadUrl(requireContext(),action.article.id,action.article.link,action.article.collect)
            is ArticleAction.CollectClick -> collectViewModel.articleCollectAction(CollectEventModel(action.article.id,action.article.link,action.article.collect.not()))
            is ArticleAction.AuthorClick -> null
            else -> null
        }
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = groupAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }

}