package com.tong.wanandroid.ui.coin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.tong.wanandroid.R
import com.tong.wanandroid.common.datastore.DataStoreManager
import com.tong.wanandroid.databinding.ActivityMyCoinInfoBinding
import com.tong.wanandroid.ui.coin.adapter.CoinHistoryAdapter
import com.tong.wanandroid.ui.coin.rank.CoinRankActivity
import com.tong.wanandroid.ui.project.child.ProjectAdapter
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyCoinInfoActivity : AppCompatActivity() {
    private var _binding: ActivityMyCoinInfoBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: MyCoinInfoViewModel

    private val coinHistoryAdapter by lazy { CoinHistoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        _binding = ActivityMyCoinInfoBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MyCoinInfoViewModel::class.java]

        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.coinRulesHelp.setOnClickListener {
            WebActivity.loadUrl(this,"https://www.wanandroid.com/blog/show/2653")
        }
        binding.coinRanking.setOnClickListener {
            startActivity(Intent(this, CoinRankActivity::class.java))
        }

        binding.coinList.apply {
            adapter = coinHistoryAdapter
            setHasFixedSize(true)
        }
        viewModel.userInfoLiveData.observe(this) {
            binding.userInfo = it
        }
        lifecycleScope.launch {
            coinHistoryAdapter.loadStateFlow.collectLatest(this@MyCoinInfoActivity::updateLoadStates)
        }
        lifecycleScope.launch {
            viewModel.coinHistoryFlow.collectLatest(coinHistoryAdapter::submitData)
        }
        viewModel.getUserInfo()
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = coinHistoryAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }
}