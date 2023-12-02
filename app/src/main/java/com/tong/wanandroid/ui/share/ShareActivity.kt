package com.tong.wanandroid.ui.share

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.tong.wanandroid.R
import com.tong.wanandroid.databinding.ActivityMyCoinInfoBinding
import com.tong.wanandroid.databinding.ActivityShareBinding
import com.tong.wanandroid.ui.coin.MyCoinInfoViewModel
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import com.tong.wanandroid.ui.profile.CollapsingToolBarState
import com.tong.wanandroid.ui.web.WebActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlin.math.abs

class ShareActivity : AppCompatActivity() {
    private var _binding: ActivityShareBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: ShareViewModel

    private val shareAdapter by lazy { HomeAdapter(this@ShareActivity::onItemClick) }

    private val userId by lazy {
        intent.getStringExtra(extra_user_id) ?: ""
    }

    companion object {
        private const val extra_user_id = "user_id"

        fun load(context: Context, userId: String) {
            val intent = Intent(context, ShareActivity::class.java)
            intent.putExtra(extra_user_id, userId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        _binding = ActivityShareBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[ShareViewModel::class.java]

        setContentView(binding.root)

        initView()
        viewModel.fetch(userId)
    }

    private fun initView(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.shareList.apply {
            adapter = shareAdapter
            setHasFixedSize(true)
        }
        binding.appBarLayout.addOnOffsetChangedListener{ appBarLayout, verticalOffset ->
            when {
                verticalOffset == 0 -> viewModel.collapsingToolBarStateFlow.value =
                    CollapsingToolBarState.EXPANDED
                abs(verticalOffset) >= appBarLayout.totalScrollRange -> viewModel.collapsingToolBarStateFlow.value =
                    CollapsingToolBarState.COLLAPSED
                else -> viewModel.collapsingToolBarStateFlow.value =
                    CollapsingToolBarState.INTERMEDIATE
            }
        }

        lifecycleScope.launch {
            viewModel.collapsingToolBarStateFlow.distinctUntilChanged { old, new ->
                old == new
            }.collectLatest {
                if (it == CollapsingToolBarState.COLLAPSED) {
                    binding.collapsingToolbarLayout.title =
                        binding.shareModel?.coinInfo?.nickname
                } else binding.collapsingToolbarLayout.title = ""
            }
        }

        lifecycleScope.launch {
            shareAdapter.loadStateFlow.collectLatest(this@ShareActivity::updateLoadStates)
        }
        lifecycleScope.launch {
            viewModel.shareListFlow.collectLatest(shareAdapter::submitData)
        }
        lifecycleScope.launch {
            viewModel.shareBeanFlow.collectLatest {
                binding.shareModel = it
            }
        }
    }

    private fun onItemClick(articleAction: ArticleAction) {
        when (articleAction) {
            is ArticleAction.ItemClick -> WebActivity.loadUrl(this,articleAction.article.id,articleAction.article.link,articleAction.article.collect)
            is ArticleAction.CollectClick -> null
            is ArticleAction.AuthorClick -> null
            is ArticleAction.BannerClick -> null
        }
    }

    private fun updateLoadStates(loadStates: CombinedLoadStates) {
        binding.loadingContainer.apply {
            loadingProgress.isVisible = shareAdapter.itemCount == 0 && loadStates.source.refresh is LoadState.Loading
        }
    }
}