package com.tong.wanandroid.ui.project.child

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tong.wanandroid.databinding.FragmentProjectChildBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProjectChildFragment : Fragment() {

    private var _binding: FragmentProjectChildBinding? = null
    private val binding get() = _binding!!

    private val projectAdapter by lazy { ProjectAdapter() }

    companion object {
        fun newInstance(categoryId: Int) = ProjectChildFragment().apply {
            arguments = bundleOf("key_project_child_category_id" to categoryId)
        }
    }

    private val categoryId by lazy { arguments?.getInt("key_project_child_category_id", -1) ?: -1 }

    private lateinit var viewModel: ProjectChildViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ProjectChildViewModel::class.java]
        _binding = FragmentProjectChildBinding.inflate(inflater, container, false)
        initView()
        if (savedInstanceState == null) {
            viewModel.fetch(categoryId)
        }
        return binding.root
    }

    fun initView(){
        val recycleView = binding.projectList
        val swipeRefreshLayout = binding.projectRefreshLayout

        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = projectAdapter
        }
        lifecycleScope.launch {
            viewModel.getProjectListFlow.collectLatest(projectAdapter::submitData)
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            projectAdapter.refresh()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}