package com.tong.wanandroid.ui.search.child

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
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.CollectEventModel
import com.tong.wanandroid.databinding.FragmentSearchResultBinding
import com.tong.wanandroid.ui.collect.CollectViewModel
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import com.tong.wanandroid.ui.search.SearchViewModel
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val searchResultAdapter by lazy { HomeAdapter(this@SearchResultFragment::onItemClick) }

    companion object {
        fun newInstance() = SearchResultFragment()
    }

    private lateinit var viewModel: SearchViewModel

    val collectViewModel: CollectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView(){
        binding.searchResultList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchResultAdapter
            setHasFixedSize(true)
        }

        lifecycleScope.launch {
            searchResultAdapter.loadStateFlow.collectLatest(this@SearchResultFragment::updateLoadStates)
        }

        viewModel.searchResults.observe(viewLifecycleOwner){
            lifecycleScope.launch {
                it.collectLatest(searchResultAdapter::submitData)
            }
        }

        collectViewModel.collectArticleEvent.observe(viewLifecycleOwner) { event ->
            searchResultAdapter.snapshot().run {
                val index = indexOfFirst { it is ArticleModel && it.id == event.id }
                if (index >= 0) {
                    (this[index] as? ArticleModel)?.collect = event.isCollected
                    index
                } else null
            }?.apply(searchResultAdapter::notifyItemChanged)
        }
    }

    private fun onItemClick(action: ArticleAction) {
        when (action) {
            is ArticleAction.ItemClick -> WebActivity.loadUrl(requireContext(),action.article.id,action.article.link,action.article.collect)
            is ArticleAction.CollectClick -> collectViewModel.articleCollectAction(CollectEventModel(action.article.id,action.article.link,action.article.collect.not()))
            is ArticleAction.AuthorClick -> null
            is ArticleAction.BannerClick -> null
        }
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = searchResultAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }


}