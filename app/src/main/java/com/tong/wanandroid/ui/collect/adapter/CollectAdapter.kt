package com.tong.wanandroid.ui.collect.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.CollectModel
import com.tong.wanandroid.databinding.ItemCollectArticleLayoutBinding

enum class ItemClickType {

    /**
     * 主体点击
     */
    CONTENT,

    /**
     * 收藏点击
     */
    COLLECT

}


class CollectAdapter(private val onClick: (Int, CollectModel, type: ItemClickType) -> Unit)  : PagingDataAdapter<Any, BaseViewHolder<*>>(
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
        if (holder is CollectViewHolder){
            val model =  item as CollectModel
            holder.bind(model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CollectViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_collect_article_layout,parent,false))
    }
}

class CollectViewHolder(val binding: ItemCollectArticleLayoutBinding) : BaseViewHolder<CollectModel>(binding) {
    override fun bind(item: CollectModel) {
        binding.collect = item
        binding.executePendingBindings()
    }
}