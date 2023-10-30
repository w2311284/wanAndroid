package com.tong.wanandroid.ui.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.tong.wanandroid.databinding.FragmentProjectBinding

class ProjectFragment : Fragment() {

    private var _binding: FragmentProjectBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProjectViewModel

    private lateinit var childAdapter: ProjectViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this)[ProjectViewModel::class.java]
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    fun initView(){
        val tabLayout = binding.projectTabLayout
        val viewPager = binding.projectViewPager
        childAdapter = ProjectViewPagerAdapter(emptyList(),this.childFragmentManager,lifecycle)

        viewPager.adapter = childAdapter
        TabLayoutMediator(tabLayout,viewPager){ tab, position ->
            tab.text = childAdapter.items[position].name
        }.attach()

        viewModel.projectTitleList.observe(viewLifecycleOwner){
            childAdapter.items = it
            childAdapter.notifyDataSetChanged()
        }
        viewModel.getProjectTitles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}