package com.tong.wanandroid.ui.navigator.child.series.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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
import com.tong.wanandroid.databinding.ActivitySeriesDetailBinding
import com.tong.wanandroid.ui.collect.CollectViewModel
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

    val collectViewModel: CollectViewModel by viewModels()

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

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

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

        collectViewModel.collectArticleEvent.observe(this) { event ->
            detailsAdapter.snapshot().run {
                val index = indexOfFirst { it is ArticleModel && it.id == event.id }
                if (index >= 0) {
                    (this[index] as? ArticleModel)?.collect = event.isCollected
                    index
                } else null
            }?.apply(detailsAdapter::notifyItemChanged)
        }
    }

    private fun onItemClick(action: ArticleAction) {
        when (action) {
            is ArticleAction.ItemClick -> WebActivity.loadUrl(this,action.article.id,action.article.link,action.article.collect)
            is ArticleAction.CollectClick -> collectViewModel.articleCollectAction(CollectEventModel(action.article.id,action.article.link,action.article.collect.not()))
            is ArticleAction.AuthorClick -> null
            else -> null
        }
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = detailsAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }

}