package com.tong.wanandroid.ui.home.child.answer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tong.wanandroid.R

class AnswerFragment : Fragment() {

    companion object {
        fun newInstance() = AnswerFragment()
    }

    private lateinit var viewModel: AnswerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_answer, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AnswerViewModel::class.java)
        // TODO: Use the ViewModel
    }

}