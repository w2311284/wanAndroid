package com.tong.wanandroid.ui.navigator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.tong.wanandroid.databinding.FragmentNavigatorBinding

class NavigatorFragment : Fragment() {

    private var _binding: FragmentNavigatorBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNavigatorBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    fun initView(){
        val tabLayout = binding.NavigatorTabLayout
        val viewPager = binding.NavigatorViewPager
        val adapter = NavigatorViewPagerAdapter(generateTabs(),this.childFragmentManager,lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout,viewPager){ tab, position ->
            tab.text = adapter.items[position]
        }.attach()
    }

    private fun generateTabs() = listOf(
        NavigatorViewPagerAdapter.NAVIGATOR_TAB_NAVIGATOR,
        NavigatorViewPagerAdapter.NAVIGATOR_TAB_SERIES,
        NavigatorViewPagerAdapter.NAVIGATOR_TAB_TUTORIAL
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}