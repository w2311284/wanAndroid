package com.tong.wanandroid.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.tong.wanandroid.databinding.FragmentHomeBinding
import com.tong.wanandroid.ui.search.SearchActivity

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

        binding.searchIcon.apply {
            setOnClickListener {
                startActivity(Intent(this@HomeFragment.context, SearchActivity::class.java))
            }
        }

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