package com.tong.wanandroid.ui.navigator.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.NavigationModel
import com.tong.wanandroid.common.services.model.SeriesModel
import com.tong.wanandroid.databinding.ItemNavigatorTagLayoutBinding

class TagTitleAdapter(private val items : List<Any>) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemNavigatorTagLayoutBinding>(layoutInflater, R.layout.item_navigator_tag_layout, parent, false)
        return TagViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is TagViewHolder){
            val item = items[position]
            holder.bind(item)
        }
    }

}

class TagViewHolder(val binding: ItemNavigatorTagLayoutBinding) : BaseViewHolder<Any>(binding){
    override fun bind(item: Any) {
        if (item is NavigationModel){
            binding.tagTitle.text = item.name
        }
        if (item is SeriesModel){
            binding.tagTitle.text = item.name
        }

        binding.executePendingBindings()
    }
}

