package com.tong.wanandroid.ui.message.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.BaseViewHolder
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.MessageModel
import com.tong.wanandroid.databinding.ItemMessageChildLayoutBinding

class MessageChildAdapter(private val onClick: (Int, MessageModel) -> Unit) : PagingDataAdapter<Any, BaseViewHolder<*>>(
DIFF_CALLBACK
) {
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

    fun isEmpty() = itemCount == 0

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = getItem(position)
        if (holder is MessageChildViewHolder){
            val model =  item as MessageModel
            holder.bind(model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MessageChildViewHolder(DataBindingUtil.inflate(layoutInflater, R.layout.item_message_child_layout,parent,false))
    }

}

class MessageChildViewHolder(val binding: ItemMessageChildLayoutBinding) : BaseViewHolder<MessageModel>(binding){
    override fun bind(item: MessageModel) {
        binding.msg = item
        binding.executePendingBindings()
    }

}