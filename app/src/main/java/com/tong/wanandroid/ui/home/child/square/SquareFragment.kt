package com.tong.wanandroid.ui.home.child.square

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tong.wanandroid.R
import com.tong.wanandroid.databinding.FragmentSquareBinding
import com.tong.wanandroid.ui.home.child.adapter.HomeAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SquareFragment : Fragment() {

    companion object {
        fun newInstance() = SquareFragment()
    }

    private var _binding: FragmentSquareBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: SquareViewModel

    private val squareAdapter by lazy { HomeAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SquareViewModel::class.java)
        _binding = FragmentSquareBinding.inflate(inflater, container, false)
        initView()
        return inflater.inflate(R.layout.fragment_square, container, false)
    }

    fun initView(){
        val recycleView = binding.squareList
        val swipeRefreshLayout = binding.squareRefreshLayout

        recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = squareAdapter
        }
        lifecycleScope.launch {
            viewModel.getSquareFlow.collectLatest(squareAdapter::submitData)
        }

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            squareAdapter.refresh()
        }

    }


}