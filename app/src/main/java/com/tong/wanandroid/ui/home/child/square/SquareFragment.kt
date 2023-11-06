package com.tong.wanandroid.ui.home.child.square

import android.content.Context
import android.content.Intent
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
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.FragmentSquareBinding
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SquareFragment : Fragment() {

    companion object {
        fun newInstance() = SquareFragment()
    }

    private var _binding: FragmentSquareBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: SquareViewModel

    private val squareAdapter by lazy { HomeAdapter(this@SquareFragment::onItemClick) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[SquareViewModel::class.java]
        _binding = FragmentSquareBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    fun initView(){
        val recycleView = binding.squareList
        val swipeRefreshLayout = binding.squareRefreshLayout

        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = squareAdapter
        }

        lifecycleScope.launch {
            squareAdapter.loadStateFlow.collectLatest(this@SquareFragment::updateLoadStates)
        }

        lifecycleScope.launch {
            viewModel.getSquareFlow.collectLatest(squareAdapter::submitData)
        }
        squareAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    recycleView.scrollToPosition(0)
                }
            }
        })
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            squareAdapter.refresh()
        }

    }

    private fun onItemClick(articleAction: ArticleAction) {
        when (articleAction) {
            is ArticleAction.ItemClick -> pushToDetailActivity(requireContext(),articleAction.article)
            is ArticleAction.CollectClick -> null
            is ArticleAction.AuthorClick -> null
            else -> null
        }
    }

    private fun pushToDetailActivity(context: Context, article: ArticleModel) {
        // 跳转到详情页面
        context?.let { WebActivity.loadUrl(it,article.id,article.link,article.collect) }
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = squareAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}