package com.tong.wanandroid.ui.navigator.child.navigator

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.NavigationModel
import com.tong.wanandroid.databinding.FragmentNavigatorChildBinding
import com.tong.wanandroid.ui.navigator.child.adapter.TagChildrenAdapter
import com.tong.wanandroid.ui.navigator.child.adapter.TagTitleAdapter

class NavigatorChildFragment : Fragment() {

    private var _binding: FragmentNavigatorChildBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = NavigatorChildFragment()
    }

    private lateinit var viewModel: NavigatorChildViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[NavigatorChildViewModel::class.java]
        _binding = FragmentNavigatorChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun initView(){
        viewModel.navigationTagListLiveData.observe(viewLifecycleOwner){
            generateLeftGroup(it)
            generateChildList(it)
        }

    }

    fun generateChildList(tags: List<Any>){
        binding.tagChildList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TagChildrenAdapter(tags)
        }
    }



    fun generateLeftGroup(tags: List<Any>){
        binding.leftTagList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TagTitleAdapter(tags)
        }
    }


}