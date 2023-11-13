package com.tong.wanandroid.ui.group

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tong.wanandroid.common.services.model.ClassifyModel
import com.tong.wanandroid.ui.group.child.GroupChildFragment


class GroupViewPagerAdapter(var items: List<ClassifyModel>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int = items.size
    override fun createFragment(position: Int): Fragment {
        return GroupChildFragment.newInstance(items[position].id)
    }
}