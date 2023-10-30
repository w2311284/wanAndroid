package com.tong.wanandroid.ui.project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tong.wanandroid.common.services.model.ProjectTitleModel
import com.tong.wanandroid.ui.project.child.ProjectChildFragment

class ProjectViewPagerAdapter(var items: List<ProjectTitleModel>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int = items.size
    override fun createFragment(position: Int): Fragment {
        return ProjectChildFragment.newInstance(items[position].id)
    }
}