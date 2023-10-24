package com.tong.wanandroid.ui.home.child.item

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.ItemHomeArticleLayoutBinding

class HomeAdapter :
    PagingDataAdapter<ArticleModel, ArticleItemViewHolder>(ArticleComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding : ItemHomeArticleLayoutBinding = DataBindingUtil.inflate(inflater, R.layout.item_home_article_layout, parent, false)
        return ArticleItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleItemViewHolder, position: Int) {
        // 在这里绑定数据到列表项的视图元素
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    object ArticleComparator : DiffUtil.ItemCallback<ArticleModel>() {
        override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
            return oldItem == newItem
        }
    }
}

class ArticleItemViewHolder(private val binding: ItemHomeArticleLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    // 在这里进行列表项视图元素的初始化
    fun bind(item: ArticleModel) {
        // 绑定数据到视图'
        binding.article = item
        binding.executePendingBindings()

    }
}