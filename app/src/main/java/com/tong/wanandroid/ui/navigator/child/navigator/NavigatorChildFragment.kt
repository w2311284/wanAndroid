package com.tong.wanandroid.ui.navigator.child.navigator

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.NavigationModel
import com.tong.wanandroid.databinding.FragmentNavigatorChildBinding
import com.tong.wanandroid.ui.navigator.child.adapter.TagChildrenAdapter
import com.tong.wanandroid.ui.navigator.child.adapter.TagTitleAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NavigatorChildFragment : Fragment() {

    private var _binding: FragmentNavigatorChildBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = NavigatorChildFragment()
    }

    private lateinit var viewModel: NavigatorChildViewModel

    private lateinit var tagChildrenAdapter: TagChildrenAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[NavigatorChildViewModel::class.java]
        _binding = FragmentNavigatorChildBinding.inflate(inflater, container, false)

        initView()

        return binding.root
    }

    fun initView(){
        viewModel.navigationTagListLiveData.observe(viewLifecycleOwner){
            binding.loadingContainer.loadingProgress.isVisible = false
            generateLeftGroup(it)
            generateChildList(it)
        }
        viewModel.getNavigationList()
    }

    fun generateChildList(tags: List<Any>){
        tagChildrenAdapter = TagChildrenAdapter(tags)
        binding.tagChildrenList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tagChildrenAdapter
        }
    }



    fun generateLeftGroup(tags: List<Any>){
        binding.leftTagList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = TagTitleAdapter(tags)
        }
    }

}