package com.tong.wanandroid.ui.home.child.viewHolder
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.ItemHomeArticleLayoutBinding
class ArticleViewHolder(val binding: ItemHomeArticleLayoutBinding) : BaseViewHolder<ArticleModel>(binding) {
    override fun bind(item: ArticleModel) {
        binding.article = item
        binding.executePendingBindings()
    }
}