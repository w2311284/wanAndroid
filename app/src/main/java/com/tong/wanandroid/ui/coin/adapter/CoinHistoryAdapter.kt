package com.tong.wanandroid.ui.coin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.CoinHistoryModel
import com.tong.wanandroid.databinding.ItemCoinHistoryLayoutBinding


class CoinHistoryAdapter : PagingDataAdapter<Any, BaseViewHolder<*>>(
    DIFF_CALLBACK
){

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
        if (holder is CoinHistoryViewHolder){
            val model =  item as CoinHistoryModel
            holder.bind(model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CoinHistoryViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_coin_history_layout,parent,false))
    }
}

class CoinHistoryViewHolder(val binding: ItemCoinHistoryLayoutBinding) : BaseViewHolder<CoinHistoryModel>(binding) {
    override fun bind(item: CoinHistoryModel) {
        binding.coinHistory = item

        binding.executePendingBindings()
    }
}