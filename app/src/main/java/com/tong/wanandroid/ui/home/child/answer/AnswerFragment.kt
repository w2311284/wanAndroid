package com.tong.wanandroid.ui.home.child.answer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tong.wanandroid.databinding.FragmentAnswerBinding
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import com.tong.wanandroid.ui.home.child.square.SquareViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AnswerFragment : Fragment() {

    companion object {
        fun newInstance() = AnswerFragment()
    }

    private lateinit var viewModel: AnswerViewModel

    private var _binding: FragmentAnswerBinding? = null

    private val binding get() = _binding!!

    private val answerAdapter by lazy { HomeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[AnswerViewModel::class.java]
        _binding = FragmentAnswerBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    fun initView(){
        val recycleView = binding.answerList
        val swipeRefreshLayout = binding.answerRefreshLayout

        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = answerAdapter
        }
        lifecycleScope.launch {
            viewModel.getAnswerFlow.collectLatest(answerAdapter::submitData)
        }

        answerAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    recycleView.scrollToPosition(0)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            answerAdapter.refresh()
        }
    }

}