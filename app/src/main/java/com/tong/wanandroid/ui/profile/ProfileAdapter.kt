package com.tong.wanandroid.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ProfileItemModel
import com.tong.wanandroid.databinding.ItemProfileLayoutBinding


class ProfileAdapter(var items: List<ProfileItemModel>, private val onClick: (Int, ProfileItemModel) -> Unit) : RecyclerView.Adapter<BaseViewHolder<*>>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemProfileLayoutBinding>(layoutInflater, R.layout.item_profile_layout, parent, false)
        return ProfileViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        if (holder is ProfileViewHolder){
            holder.bind(items[position])
        }
    }
}

class ProfileViewHolder(val binding: ItemProfileLayoutBinding):BaseViewHolder<ProfileItemModel>(binding){
    override fun bind(item: ProfileItemModel) {
        binding.item = item
        binding.executePendingBindings()
    }

}