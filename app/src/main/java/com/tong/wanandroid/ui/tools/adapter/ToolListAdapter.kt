package com.tong.wanandroid.ui.tools.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ToolModel
import com.tong.wanandroid.databinding.ItemToolLayoutBinding


class ToolListAdapter(var items: List<ToolModel>,private val onClick: (Int, ToolModel) -> Unit) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemToolLayoutBinding>(layoutInflater, R.layout.item_tool_layout, parent, false)
        return ToolListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is ToolListViewHolder){
            val item =  items[position]
            holder.bind(item)
            holder.binding.apply {
                root.setOnClickListener {
                    onClick(holder.bindingAdapterPosition, item)
                }
            }
        }
    }
}

class ToolListViewHolder(val binding: ItemToolLayoutBinding):BaseViewHolder<ToolModel>(binding){
    override fun bind(item: ToolModel) {
        binding.tool = item
        Glide.with(binding.toolIcon)
            .load(item.getIconUrl())
            .into(binding.toolIcon)
//        binding.toolIcon.setImageURI(item.)
        binding.executePendingBindings()
    }

}