package com.tong.wanandroid.ui.navigator

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tong.wanandroid.ui.navigator.child.navigator.NavigatorChildFragment
import com.tong.wanandroid.ui.navigator.child.series.SeriesFragment
import com.tong.wanandroid.ui.navigator.child.tutorial.TutorialFragment

class NavigatorViewPagerAdapter(var items: List<String>, fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {

    companion object {
        const val NAVIGATOR_TAB_NAVIGATOR = "导航"
        const val NAVIGATOR_TAB_SERIES = "体系"
        const val NAVIGATOR_TAB_TUTORIAL = "教程"
    }

    override fun getItemCount(): Int = items.size
    override fun createFragment(position: Int): Fragment {
        return when (items[position]) {
            NAVIGATOR_TAB_NAVIGATOR -> NavigatorChildFragment.newInstance()
            NAVIGATOR_TAB_SERIES -> SeriesFragment.newInstance()
            NAVIGATOR_TAB_TUTORIAL -> TutorialFragment.newInstance()
            else -> NavigatorChildFragment.newInstance()
        }
    }
}