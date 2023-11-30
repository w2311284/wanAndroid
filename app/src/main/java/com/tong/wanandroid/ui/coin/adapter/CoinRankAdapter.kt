package com.tong.wanandroid.ui.coin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.CoinModel
import com.tong.wanandroid.databinding.ItemCoinRankLayoutBinding

class CoinRankAdapter: PagingDataAdapter<Any, BaseViewHolder<*>>(
    DIFF_CALLBACK
) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = getItem(position)
        if (holder is CoinRankViewHolder){
            val model =  item as CoinModel
            holder.bind(model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CoinRankViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_coin_rank_layout,parent,false))
    }
}

class CoinRankViewHolder(val binding: ItemCoinRankLayoutBinding) : BaseViewHolder<CoinModel>(binding) {
    override fun bind(item: CoinModel) {
        binding.coinInfo = item
        binding.executePendingBindings()
    }
}