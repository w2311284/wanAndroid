package com.tong.wanandroid.ui.search.child

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.tong.wanandroid.common.services.model.HotKeyModel
import com.tong.wanandroid.databinding.FragmentSearchBeginBinding
import com.tong.wanandroid.ui.search.SearchViewModel
import com.tong.wanandroid.ui.search.child.adapter.SearchHistoryAdaper


class SearchBeginFragment : Fragment() {

    private var _binding: FragmentSearchBeginBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = SearchBeginFragment()
    }

    private lateinit var viewModel: SearchViewModel

    private val historyAdapter = SearchHistoryAdaper(emptyList(), itemClick = { pos, search -> onHistoryClick(pos,search)}, deleteClick = { pos, search -> onDeleteClick(pos,search)})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]
        _binding = FragmentSearchBeginBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView(){
        binding.historyList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
        viewModel.hotKeysLiveData.observe(viewLifecycleOwner){
            generateHotGroup(it)
        }
        viewModel.searchLiveData.observe(viewLifecycleOwner){
            viewModel.historyPut(it)
        }
        viewModel.historyLiveData.observe(viewLifecycleOwner){
            historyAdapter.items = it
            historyAdapter.notifyDataSetChanged()
        }
        viewModel.getHotKeys()
    }

    fun generateHotGroup(hotKeys : List<HotKeyModel>){
        hotKeys.forEach { model ->
            val chip = Chip(context,null).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setPadding(10)
                gravity = Gravity.CENTER
                textSize = 13F

            }
            chip.setOnClickListener { onHotKeyClick(model) }
            chip.text = model.name
            binding.hotkeyList.addView(chip)
        }
    }

    private fun onHistoryClick(pos: Int,search: String) {
        viewModel.shortcutSearch(search)
    }
    private fun onDeleteClick(pos: Int,search: String) {
        viewModel.historyRemove(search)
    }

    private fun onHotKeyClick(hotKey: HotKeyModel) {
        viewModel.shortcutSearch(hotKey.name)
    }


}