package com.tong.wanandroid.ui.group
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.tong.wanandroid.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {

    private var _binding: FragmentGroupBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = GroupFragment()
    }

    private lateinit var viewModel: GroupViewModel

    private lateinit var childAdapter: GroupViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this)[GroupViewModel::class.java]
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView(){
        val tabLayout = binding.groupTabLayout
        val viewPager = binding.groupViewPager
        childAdapter = GroupViewPagerAdapter(emptyList(),this.childFragmentManager,lifecycle)

        viewPager.adapter = childAdapter
        TabLayoutMediator(tabLayout,viewPager){ tab, position ->
            tab.text = childAdapter.items[position].name
        }.attach()


        viewModel.groupTitles.observe(viewLifecycleOwner){
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