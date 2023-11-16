package com.tong.wanandroid.ui.search.child

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tong.wanandroid.R

class SearchBeginFragment : Fragment() {

    companion object {
        fun newInstance() = SearchBeginFragment()
    }

    private lateinit var viewModel: SearchBeginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_begin, container, false)
    }


}