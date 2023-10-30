package com.tong.wanandroid.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.tong.wanandroid.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val tabLayout = binding.homeTabLayout
        val viewPager = binding.homeViewPager
        val adapter = HomeViewPagerAdapter(generateHomeTabs(),this.childFragmentManager,lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout,viewPager){ tab, position ->
            tab.text = adapter.items[position]
        }.attach()
        return root
    }

    private fun generateHomeTabs() = listOf(
        HomeViewPagerAdapter.HOME_TAB_RECOMMENDED,
        HomeViewPagerAdapter.HOME_TAB_SQUARE,
        HomeViewPagerAdapter.HOME_TAB_ANSWER
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}