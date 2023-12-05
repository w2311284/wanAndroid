package com.tong.wanandroid.ui.project.child

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.CollectEventModel
import com.tong.wanandroid.databinding.FragmentProjectChildBinding
import com.tong.wanandroid.ui.collect.CollectViewModel
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

    val collectViewModel: CollectViewModel by viewModels()

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
            projectAdapter.loadStateFlow.collectLatest(this@ProjectChildFragment::updateLoadStates)
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

        collectViewModel.collectArticleEvent.observe(viewLifecycleOwner) { event ->
            projectAdapter.snapshot().run {
                val index = indexOfFirst { it is ArticleModel && it.id == event.id }
                if (index >= 0) {
                    (this[index] as? ArticleModel)?.collect = event.isCollected
                    index
                } else null
            }?.apply(projectAdapter::notifyItemChanged)
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
            loadingProgress.isVisible = projectAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}