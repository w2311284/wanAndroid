package com.tong.wanandroid.ui.home.child.recommended


import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.BannerModel
import com.tong.wanandroid.common.services.model.CollectEventModel
import com.tong.wanandroid.databinding.FragmentRecommendedBinding
import com.tong.wanandroid.ui.collect.CollectViewModel
import com.tong.wanandroid.ui.footer.FooterStateAdapter
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecommendedFragment : Fragment() {

    private var _binding: FragmentRecommendedBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = RecommendedFragment()
    }

    private lateinit var viewModel: RecommendedViewModel

    val collectViewModel: CollectViewModel by viewModels()
    private val homeAdapter by lazy { HomeAdapter(this@RecommendedFragment::onItemClick) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[RecommendedViewModel::class.java]
        _binding = FragmentRecommendedBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    fun initView(){
        val recycleView = binding.recommendedList
        val swipeRefreshLayout = binding.recommendedRefreshLayout
        binding.recommendedRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            homeAdapter.refresh()
        }

        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homeAdapter.withLoadStateFooter(
                footer = FooterStateAdapter()
            )
        }
        lifecycleScope.launch {
            homeAdapter.loadStateFlow.collectLatest(this@RecommendedFragment::updateLoadStates)
        }
        lifecycleScope.launch {
            viewModel.getArticlesFlow.collectLatest(homeAdapter::submitData)
        }

        homeAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    recycleView.scrollToPosition(0)
                }
            }
        })

        collectViewModel.collectArticleEvent.observe(viewLifecycleOwner) { event ->
            homeAdapter.snapshot().run {
                val index = indexOfFirst { it is ArticleModel && it.id == event.id }
                if (index >= 0) {
                    (this[index] as? ArticleModel)?.collect = event.isCollected
                    index
                } else null
            }?.apply(homeAdapter::notifyItemChanged)
        }
    }

    private fun onItemClick(action: ArticleAction) {
        when (action) {
            is ArticleAction.ItemClick -> WebActivity.loadUrl(requireContext(),action.article.id,action.article.link,action.article.collect)
            is ArticleAction.CollectClick -> collectViewModel.articleCollectAction(CollectEventModel(action.article.id,action.article.link,action.article.collect.not()))
            is ArticleAction.AuthorClick -> null
            is ArticleAction.BannerClick -> WebActivity.loadUrl(requireContext(),action.banner.id,action.banner.url,false)
        }
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = homeAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}