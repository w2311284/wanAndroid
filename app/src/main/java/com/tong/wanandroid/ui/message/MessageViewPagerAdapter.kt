package com.tong.wanandroid.ui.message

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tong.wanandroid.ui.message.child.MessageChildFragment

class MessageViewPagerAdapter(var items: List<String>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    companion object {
        const val MESSAGE_TAB_NEW = "新消息"
        const val MESSAGE_TAB_HISTORY = "历史消息"
    }

    override fun getItemCount(): Int = items.size
    override fun createFragment(position: Int): Fragment {
        return MessageChildFragment.newInstance(items[position])
    }

}