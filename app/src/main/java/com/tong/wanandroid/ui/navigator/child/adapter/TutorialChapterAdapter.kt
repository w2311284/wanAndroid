package com.tong.wanandroid.ui.navigator.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.ItemHomeArticleLayoutBinding
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction
import com.tong.wanandroid.ui.home.child.viewHolder.ArticleViewHolder


class TutorialChapterAdapter(private val items : List<ArticleModel>, private val onClick: (Int, ArticleModel) -> Unit): RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemHomeArticleLayoutBinding>(layoutInflater, R.layout.item_home_article_layout, parent, false)
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is ArticleViewHolder){
            val item = items[position]
            holder.bind(item)
            holder.binding.apply {
                root.setOnClickListener {
                    onClick(holder.bindingAdapterPosition, item)
                }
                ivCollect.isVisible = false
            }
        }
    }
}