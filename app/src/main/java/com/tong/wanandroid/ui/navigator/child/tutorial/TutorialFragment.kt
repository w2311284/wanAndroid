package com.tong.wanandroid.ui.navigator.child.tutorial

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tong.wanandroid.R

class TutorialFragment : Fragment() {

    companion object {
        fun newInstance() = TutorialFragment()
    }

    private lateinit var viewModel: TutorialViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial, container, false)
    }

}