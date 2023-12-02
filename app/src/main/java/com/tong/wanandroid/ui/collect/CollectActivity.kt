package com.tong.wanandroid.ui.collect

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.CollectModel
import com.tong.wanandroid.databinding.ActivityCollectBinding
import com.tong.wanandroid.databinding.ActivityShareBinding
import com.tong.wanandroid.ui.coin.adapter.CoinHistoryAdapter
import com.tong.wanandroid.ui.collect.adapter.CollectAdapter
import com.tong.wanandroid.ui.collect.adapter.ItemClickType
import com.tong.wanandroid.ui.share.ShareViewModel
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CollectActivity : AppCompatActivity() {
    private var _binding: ActivityCollectBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: CollectViewModel

    private val collectAdapter by lazy { CollectAdapter(this@CollectActivity::onCollectClick) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        _binding = ActivityCollectBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[CollectViewModel::class.java]
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.collectSwipeRefresh.apply {
            setOnRefreshListener {
                collectAdapter.refresh()
            }
        }
        binding.collectList.apply {
            adapter = collectAdapter
            setHasFixedSize(true)
        }

        lifecycleScope.launch {
            collectAdapter.loadStateFlow.collectLatest(this@CollectActivity::updateLoadStates)
        }
        lifecycleScope.launch {
            viewModel.collectFlow.collectLatest(collectAdapter::submitData)
        }
    }

    private fun onCollectClick(position: Int, m : CollectModel, type: ItemClickType) {
        when (type) {
            ItemClickType.CONTENT ->  WebActivity.loadUrl(this,m.originId,m.link,m.collect)
            ItemClickType.COLLECT -> null
        }
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = collectAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }
}