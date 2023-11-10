package com.tong.wanandroid.ui.navigator.child.tutorial

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.tong.wanandroid.R
import com.tong.wanandroid.common.services.model.ClassifyModel
import com.tong.wanandroid.databinding.FragmentSeriesBinding
import com.tong.wanandroid.databinding.FragmentTutorialBinding
import com.tong.wanandroid.ui.navigator.child.adapter.TutorialAdapter

class TutorialFragment : Fragment() {

    private var _binding: FragmentTutorialBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = TutorialFragment()
    }

    private lateinit var viewModel: TutorialViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[TutorialViewModel::class.java]
        _binding = FragmentTutorialBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView(){
        viewModel.tutorialListLiveData.observe(viewLifecycleOwner){
            binding.loadingContainer.loadingProgress.isVisible = false
            binding.tutorialList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = TutorialAdapter(it, onClick = { pos,model -> onItemClick(pos,model) })
            }
        }
        viewModel.getTutorialList()
    }

    private fun onItemClick(pos: Int,m: ClassifyModel) {

    }

}