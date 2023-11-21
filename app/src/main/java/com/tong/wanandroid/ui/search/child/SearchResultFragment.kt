package com.tong.wanandroid.ui.search.child

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.FragmentSearchResultBinding
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
    }

    private fun onItemClick(articleAction: ArticleAction) {
        when (articleAction) {
            is ArticleAction.ItemClick -> pushToDetailActivity(articleAction.article)
            is ArticleAction.CollectClick -> null
            is ArticleAction.AuthorClick -> null
            is ArticleAction.BannerClick -> null
        }
    }

    private fun pushToDetailActivity(article: ArticleModel) {
        // 跳转到详情页面
        context?.let { WebActivity.loadUrl(it,article.id,article.link,article.collect) }
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = searchResultAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }


}