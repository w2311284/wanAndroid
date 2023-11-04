package com.tong.wanandroid.ui.navigator.child.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.NavigationModel
import com.tong.wanandroid.databinding.ItemNavigatorTagChildLayoutBinding


class TagChildrenAdapter(private val items : List<Any>) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemNavigatorTagChildLayoutBinding>(layoutInflater, R.layout.item_navigator_tag_child_layout, parent, false)
        return TagChildViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is TagChildViewHolder){
            val item = items[position]
            holder.bind(item)
        }
    }

}

class TagChildViewHolder(val binding: ItemNavigatorTagChildLayoutBinding) : BaseViewHolder<Any>(binding){
    override fun bind(item: Any) {
        binding.apply {
            executePendingBindings()
            tagChildrenLayout.removeAllViews()
            if(item is NavigationModel){
                tagChildTitle.text = item.name
                item.articles.forEach { article ->
                    val chip = Chip(this.root.context,null).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        setPadding(6)
                        gravity = Gravity.CENTER
                        textSize = 13F

                    }
                    chip.text = article.title
                    tagChildrenLayout.addView(chip)
                }
            }
        }



    }

}