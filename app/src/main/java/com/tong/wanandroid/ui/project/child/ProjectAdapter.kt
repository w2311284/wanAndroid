package com.tong.wanandroid.ui.project.child

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ArticleModel
import com.tong.wanandroid.databinding.ItemProjectArticleLayoutBinding
import com.tong.wanandroid.ui.home.child.adapter.ArticleAction


class ProjectAdapter(private val onClick: (ArticleAction) -> Unit) : PagingDataAdapter<Any, BaseViewHolder<*>>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = getItem(position)
        if (holder is ProjectArticleViewHolder){
            val model =  item as ArticleModel
            holder.bind(model)
            holder.binding.apply {
                root.setOnClickListener {
                    onClick(ArticleAction.ItemClick(position,model))
                }
                projectCollect.setOnClickListener {
                    onClick(ArticleAction.CollectClick(position,model))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProjectArticleViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_project_article_layout,parent,false))
    }

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


}

class ProjectArticleViewHolder(val binding: ItemProjectArticleLayoutBinding) : BaseViewHolder<ArticleModel>(binding) {
    override fun bind(item: ArticleModel) {
        binding.article = item

        binding.apply{
            Glide.with(binding.projectContainer)
                .load(item.envelopePic)
                .into(binding.projectCover)
        }
        binding.executePendingBindings()
    }
}