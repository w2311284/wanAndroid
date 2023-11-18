package com.tong.wanandroid.ui.search.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.databinding.ItemSearchHistoryLayoutBinding

class SearchHistoryAdaper(var items : List<String>,private val itemClick: (Int, String) -> Unit,private val deleteClick: (Int, String) -> Unit) : RecyclerView.Adapter<BaseViewHolder<*>>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemSearchHistoryLayoutBinding>(layoutInflater, R.layout.item_search_history_layout, parent, false)
        return SearchHistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is SearchHistoryViewHolder){
            val item = items[position]
            holder.binding.apply {
                root.setOnClickListener {
                    itemClick(holder.bindingAdapterPosition, item)
                }
                historyDelete.setOnClickListener {
                    deleteClick(holder.bindingAdapterPosition, item)
                }
            }
            holder.bind(item)
        }
    }
}

class SearchHistoryViewHolder(val binding: ItemSearchHistoryLayoutBinding) : BaseViewHolder<String>(binding) {
    override fun bind(item: String) {
        binding.apply {
            binding.historyItem.text = item
        }
        binding.executePendingBindings()
    }
}