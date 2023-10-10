package com.tong.wanandroid.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tong.wanandroid.ui.home.child.answer.AnswerFragment
import com.tong.wanandroid.ui.home.child.recommended.RecommendedFragment
import com.tong.wanandroid.ui.home.child.square.SquareFragment

class HomeViewPagerAdapter(var items: List<String>,fragmentManager: FragmentManager,lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager,lifecycle) {

    companion object {
        const val HOME_TAB_RECOMMENDED = "推荐"
        const val HOME_TAB_SQUARE = "广场"
        const val HOME_TAB_ANSWER = "问答"
    }
    override fun getItemCount(): Int = items.size
    override fun createFragment(position: Int): Fragment {
        return when (items[position]) {
            HOME_TAB_RECOMMENDED -> RecommendedFragment.newInstance()
            HOME_TAB_SQUARE -> SquareFragment.newInstance()
            HOME_TAB_ANSWER -> AnswerFragment.newInstance()
            else -> RecommendedFragment.newInstance()
        }
    }
}