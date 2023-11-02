package com.tong.wanandroid.ui.footer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.databinding.ItemFooterLayoutBinding

class FooterStateAdapter : LoadStateAdapter<FooterLoadStateViewHolder>(){
    override fun onBindViewHolder(holder: FooterLoadStateViewHolder, loadState: LoadState) {

        if (holder is FooterLoadStateViewHolder){
            holder.bind(loadState)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FooterLoadStateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FooterLoadStateViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_footer_layout,parent,false))

    }

}

class FooterLoadStateViewHolder(private val binding: ItemFooterLayoutBinding) : BaseViewHolder<LoadState>(binding){
    override fun bind(state: LoadState) {
        if (state is LoadState.Error) {
            binding.errorMsg.text = state.error.localizedMessage
        }
        binding.loadingProgress.isVisible = state is LoadState.Loading
        binding.retryButton.isVisible = state is LoadState.Error
        binding.errorMsg.isVisible = state is LoadState.Error
        binding.endTips.isVisible = state.endOfPaginationReached

    }

}