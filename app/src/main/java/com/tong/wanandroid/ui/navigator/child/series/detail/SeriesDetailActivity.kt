package com.tong.wanandroid.ui.navigator.child.series.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.ActivitySeriesDetailBinding
import com.tong.wanandroid.ui.footer.FooterStateAdapter
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SeriesDetailActivity : AppCompatActivity() {

    companion object {
        const val series_detail_id = "series_detail_id"
        const val series_detail_title = "series_detail_title"
    }

    private var _binding: ActivitySeriesDetailBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: SeriesDetailViewModel

    private val detailsAdapter by lazy { HomeAdapter(this@SeriesDetailActivity::onItemClick) }

    private val currentId: Int by lazy{
        intent.getIntExtra(series_detail_id, -1)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        viewModel = ViewModelProvider(this)[SeriesDetailViewModel::class.java]
        _binding = ActivitySeriesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    fun initView(){
        binding.txtTitle.text = intent.getStringExtra(series_detail_title)
        viewModel.setDetailId(currentId)
        val recycleView = binding.seriesDetailList
        val swipeRefreshLayout = binding.seriesDetailRefreshLayout

        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = detailsAdapter.withLoadStateFooter(
                footer = FooterStateAdapter()
            )
        }

        lifecycleScope.launch {
            detailsAdapter.loadStateFlow.collectLatest(this@SeriesDetailActivity::updateLoadStates)
        }
        lifecycleScope.launch {
            viewModel.getSeriesDetailListFlow.collectLatest(detailsAdapter::submitData)
        }

        detailsAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    recycleView.scrollToPosition(0)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            detailsAdapter.refresh()
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
        WebActivity.loadUrl(this,article.id,article.link,article.collect)
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = detailsAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }

}