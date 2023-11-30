package com.tong.wanandroid.ui.coin.rank

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.tong.wanandroid.databinding.ActivityCoinRankBinding
import com.tong.wanandroid.ui.coin.adapter.CoinRankAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CoinRankActivity : AppCompatActivity() {
    private var _binding: ActivityCoinRankBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: CoinRankViewModel

    private val coinRankAdapter by lazy { CoinRankAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        _binding = ActivityCoinRankBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[CoinRankViewModel::class.java]
        setContentView(binding.root)

        initView()
    }

    private fun initView(){
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.rankList.apply {
            adapter = coinRankAdapter
            setHasFixedSize(true)
        }
        lifecycleScope.launch {
            coinRankAdapter.loadStateFlow.collectLatest(this@CoinRankActivity::updateLoadStates)
        }
        lifecycleScope.launch {

            viewModel.coinRankFlow.collectLatest(coinRankAdapter::submitData)
        }

    }
    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = coinRankAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }

}