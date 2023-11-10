package com.tong.wanandroid.ui.navigator.child.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ClassifyModel
import com.tong.wanandroid.databinding.ItemNavigatorChildTutorialLayoutBinding

class TutorialAdapter(private val items : List<ClassifyModel>,private val onClick: (Int, ClassifyModel) -> Unit): RecyclerView.Adapter<BaseViewHolder<*>>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemNavigatorChildTutorialLayoutBinding>(layoutInflater, R.layout.item_navigator_child_tutorial_layout, parent, false)
        return TutorialViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is TutorialViewHolder){
            val item = items[position]
            holder.bind(item)
        }
    }

}

class TutorialViewHolder(val binding: ItemNavigatorChildTutorialLayoutBinding) : BaseViewHolder<ClassifyModel>(binding){
    override fun bind(item: ClassifyModel) {
        binding.classify = item
//        binding.apply{
//            Glide.with(binding.ivProject)
//                .load(item.cover)
//                .into(binding.ivProject)
//        }
        binding.executePendingBindings()
    }
}