package com.tong.wanandroid.ui.home.child.recommended

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
import com.tong.wanandroid.common.services.model.BannerModel
import com.tong.wanandroid.databinding.FragmentRecommendedBinding
import com.tong.wanandroid.ui.footer.FooterStateAdapter
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RecommendedFragment : Fragment() {

    private var _binding: FragmentRecommendedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = RecommendedFragment()
    }

    private lateinit var viewModel: RecommendedViewModel
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


        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            homeAdapter.refresh()
        }
    }

    private fun onItemClick(articleAction: ArticleAction) {
        when (articleAction) {
            is ArticleAction.ItemClick -> pushToDetailActivity(articleAction.article)
            is ArticleAction.CollectClick -> null
            is ArticleAction.AuthorClick -> null
            is ArticleAction.BannerClick -> pushToBanner(articleAction.banner)
        }
    }

    private fun pushToDetailActivity(article: ArticleModel) {
        // 跳转到详情页面
        val intent = Intent(context, WebActivity::class.java)
        intent.putExtra("id", article.id)
        intent.putExtra("link", article.link)
        intent.putExtra("collect", article.collect)
        startActivity(intent)
    }

    private fun pushToBanner(banner: BannerModel) {
        // 跳转到详情页面
        val intent = Intent(context, WebActivity::class.java)
        intent.putExtra("url", banner.url)
        startActivity(intent)
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