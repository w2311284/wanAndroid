package com.tong.wanandroid.ui.home.child.item

import android.annotation.SuppressLint
import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.common.services.model.Banners
import com.tong.wanandroid.databinding.ItemHomeArticleLayoutBinding
import com.tong.wanandroid.databinding.ItemHomeBannerLayoutBinding

class HomeAdapter :
    PagingDataAdapter<Any, BaseViewHolder<*>>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = when (viewType) {
          VIEW_TYPE_Article -> DataBindingUtil.inflate(layoutInflater, R.layout.item_home_article_layout, parent, false)
          VIEW_TYPE_Banner -> DataBindingUtil.inflate(layoutInflater, R.layout.item_home_banner_layout, parent, false)
          else -> throw IllegalArgumentException("Invalid view type")
        }

        return createVH(binding,viewType)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createVH(binding: ViewDataBinding, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            VIEW_TYPE_Article -> ArticleViewHolder(binding as ItemHomeArticleLayoutBinding)
            VIEW_TYPE_Banner -> BannerViewHolder(binding as ItemHomeBannerLayoutBinding)
            // 添加更多的 ViewHolder 类
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        // 在这里绑定数据到列表项的视图元素
        val item = getItem(position)
        when (holder){
            is ArticleViewHolder ->{
                holder.bind(item as ArticleModel)
            }
            is BannerViewHolder -> {
                holder.bind(item as Banners)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item) {
            is ArticleModel -> VIEW_TYPE_Article
            is Banners -> VIEW_TYPE_Banner
            // 添加更多的布局类型
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    companion object {
        private const val VIEW_TYPE_Banner = 1
        private const val VIEW_TYPE_Article = 2
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
}